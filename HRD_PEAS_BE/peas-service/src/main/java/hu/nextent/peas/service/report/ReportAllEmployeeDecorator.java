package hu.nextent.peas.service.report;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.model.ReportAllEmployeePageModel;
import hu.nextent.peas.model.ReportAllEmployeeQueryParameterModel;
import hu.nextent.peas.model.ReportAllEmployeeRowModel;
import hu.nextent.peas.model.UserSimpleModel;

@Service
@Transactional
public class ReportAllEmployeeDecorator
extends AbstractReportDecorator {

    private static List<String> SORTS = new ArrayList<String>();
    static {
    	SORTS.add("employee.fullName");
    	SORTS.add("leader.fullName");
    	SORTS.add("employee.organization");
    	SORTS.add("score");
    	SORTS.add("asLeaderOrganization");
    	SORTS.add("asLeaderOrganizationScore");
    };

	public ResponseEntity<ReportAllEmployeePageModel> reportAllEmployee(ReportAllEmployeeQueryParameterModel body) {
		// adatok osszegyujtese
		Period period = getPeriod(REPORT_ALL_EMPLOYEE, body.getPeriodId());
		if (body.getUserId() != null) {
			getUser(REPORT_ALL_EMPLOYEE, body.getUserId());
		}

		// jogosultsagok vizsgalata
		List<RoleEnum> roleEnumList = getCurrentUserRoleEnum();

		boolean isLeader = roleEnumList.contains(RoleEnum.LEADER);
		if (!roleEnumList.contains(RoleEnum.HR) && !isLeader) {
			throw new BadCredentialsException("Not Roles");
		}

		// valasz osszeallitasa
		ReportAllEmployeePageModel model = new ReportAllEmployeePageModel();

		Query query = entityManager.createNativeQuery(new StringBuilder()
				.append("select * from (select ")
						// felhasznalo adatai
				.append("   employee.id a0")
				.append(" , employee.active a1")
				.append(" , employee.userName a2")
				.append(" , employee.fullName a3")
				.append(" , employee.email a4")
				.append(" , employee.organization a5")
						// vezeto adatai
				.append(" , leader.id a6")
				.append(" , leader.active a7")
				.append(" , leader.userName a8")
				.append(" , leader.fullName a9")
				.append(" , leader.email a10")
				.append(" , leader.organization a11")
						// felhasznalo periodus pontszama
				.append(" , r1.periodScore as score")
						// felhasznalo mint vezeto csoportja
				.append(" , (select u3.organization from user as u3 where u3.leader_id = employee.id limit 1) as asLeaderOrganization ")
						// felhasznalo mint vezeto csoportjanak atlagpontszama
                .append(" , (select avg(r2.periodScore) from rating as r2 inner join user as u9 on u9.id = r2.user_id " +
                        " where r2.periodScore is not null and r2.periodScore > 0 and r2.period_id = :periodId " +
                        " and 0 < (select count(*) from user as u3 where u3.leader_id = employee.id) " +
                        " and u9.organizationPath like concat(employee.organizationPath, '_%')) as asLeaderOrganizationScore ")
						// from
				.append(" from user as employee ")
                .append(" left outer join rating as r1 on r1.period_id = :periodId and r1.user_id = employee.id ")
                .append(" left outer join user as leader on employee.leader_id = leader.id ")
						// where
				.append(" where employee.company_id = :companyId ")
				.append(noData(body.getUserId()) ? "" : " and employee.id = :userId ")
				.append(isLeader ? " and (employee.leader_id = :currentUserId or employee.id = :currentUserId)" : "")
				.append(noData(body.getLeaderId()) ? "" : " and leader.id = :leaderId ")
				.append(noData(body.getOrganization()) ? "" : " and LOWER(employee.organization) like :organization ")
				.append(body.getScore() == null ? "" :
						(body.getScore().getMin() == null ? "" : " and r1.periodScore >= :scoreMin ") +
						(body.getScore().getMax() == null ? "" : " and r1.periodScore <= :scoreMax "))
						// order by
				.append((SORTS.contains(body.getSort()) ? " order by " + body.getSort() + " " +
						(body.getOrder() != null ? " " + body.getOrder() : "") : ""))
                        // having
                .append(") x where 1 = 1 ")
                .append(noData(body.getAsLeaderOrganization()) ? "" : " and LOWER(asLeaderOrganization) like :asLeaderOrganization ")
                .append(body.getAsLeaderOrganizationScore() == null ? "" :
                        (body.getAsLeaderOrganizationScore().getMin() == null ? "" : " and asLeaderOrganizationScore >= :asLeaderOrganizationScoreMin ") +
                        (body.getAsLeaderOrganizationScore().getMax() == null ? "" : " and asLeaderOrganizationScore <= :asLeaderOrganizationScoreMax "))
				.toString()
				);

		query.setParameter("companyId", getCurrentCompany().getId());
		query.setParameter("periodId", period.getId());
		if (isLeader) query.setParameter("currentUserId", getCurrentUser().getId());
		if (!noData(body.getUserId())) query.setParameter("userId", body.getUserId());
		if (!noData(body.getLeaderId())) query.setParameter("leaderId", body.getLeaderId());
		if (!noData(body.getOrganization())) query.setParameter("organization", "%" + body.getOrganization().toLowerCase() + "%");
		if (!noData(body.getAsLeaderOrganization())) query.setParameter("asLeaderOrganization", "%" + body.getAsLeaderOrganization().toLowerCase() + "%");
		if (body.getScore() != null && body.getScore().getMin() != null) query.setParameter("scoreMin", body.getScore().getMin());
		if (body.getScore() != null && body.getScore().getMax() != null) query.setParameter("scoreMax", body.getScore().getMax());
		if (body.getAsLeaderOrganizationScore() != null && body.getAsLeaderOrganizationScore().getMin() != null) query.setParameter("asLeaderOrganizationScoreMin", body.getAsLeaderOrganizationScore().getMin());
		if (body.getAsLeaderOrganizationScore() != null && body.getAsLeaderOrganizationScore().getMax() != null) query.setParameter("asLeaderOrganizationScoreMax", body.getAsLeaderOrganizationScore().getMax());

		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();

		// atalakitas

		List<ReportAllEmployeeRowModel> rows = new ArrayList<>();

		result.forEach(r -> rows.add(convertResult(r)));

		// osszegzesek
		model
				.companyScoreAvg(
						rows.stream()
								.filter(r -> r.getScore() != null)
								.mapToDouble(r -> r.getScore()).average().orElse(0)
				)
				.organizationScoreAvg(
						rows.stream()
								.filter(r -> r.getAsLeaderOrganizationScore() != null)
								.mapToDouble(r -> r.getAsLeaderOrganizationScore()).average().orElse(0)
				);

		// lapozas
		List<ReportAllEmployeeRowModel> pagedRows = new ArrayList<>();

		if (body.getNumber() != null && body.getSize() != null) {
			for(int
					r = body.getNumber() * body.getSize(),
					i = 0,
					l = body.getSize(),
					k = rows.size();
					i < l && i + r < k;
					i++) {
				pagedRows.add(rows.get(r + i));
			}
            model
                .totalPages(((int) (rows.size() / body.getSize())) + (rows.size() % body.getSize() > 0 ? 1 : 0))
                .number(body.getNumber())
            ;
		} else {
			pagedRows.addAll(rows);
            model
                .totalPages(1)
                .number(0)
            ;
		}

		model
            .content(pagedRows)
            .totalElements((long) rows.size())
        ;

		if (rows.isEmpty()) {
			throw ExceptionList.no_content();
		} else {
			return ResponseEntity.ok(model);
		}
	}

	private ReportAllEmployeeRowModel convertResult(Object[] r) {
		return new ReportAllEmployeeRowModel()
				.employee(convertUser(r, 0))
				.leader(convertUser(r, 6))
				.score(bigDecimalToDouble(r[12]))
				.asLeaderOrganization((String) r[13])
				.asLeaderOrganizationScore(bigDecimalToDouble(r[14]))
				;
	}

	private UserSimpleModel convertUser(Object[] r, int startIndex) {
		return new UserSimpleModel()
				.id(bigIntegerToLong(r[startIndex]))
				.active((Boolean) r[startIndex + 1])
				.userName((String) r[startIndex + 2])
				.fullName((String) r[startIndex + 3])
				.email((String) r[startIndex + 4])
				.organization((String) r[startIndex + 5])
				;
	}

}
