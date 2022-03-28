package hu.nextent.peas.service.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.CompanyParameterRepository;
import hu.nextent.peas.jpa.dao.CompanyVirtueRepository;
import hu.nextent.peas.jpa.dao.LeaderVirtueRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.SystemParameterRepository;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.model.CompanyVirtueModel;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.PeasAppInfoModel;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.service.base.BaseDecorator;
import hu.nextent.peas.utils.EnumToList;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class InfoDecorator extends BaseDecorator {
	
	@Autowired
	private SystemParameterRepository systemParameterRepository;
	
	@Autowired
	private CompanyParameterRepository companyParameterRepository;
	
	@Autowired
	private CompanyVirtueRepository companyVirtueRepository;
	
	@Autowired
	private LeaderVirtueRepository leaderVirtueRepository;
	
	@Autowired
	private PeriodRepository periodRepository;

	public ResponseEntity<PeasAppInfoModel> getInfo() {
		log.debug("create model");
		val model = 
				new PeasAppInfoModel()
					.parameters(getCompanyParameters())
					.currentUser(getCurrentUserModel())
					.leader(getCurrentLeaderModel())
					.difficulties(getCompanyDifficultyModels())
					.taskstatuses(EnumToList.enumNames(TaskStatusEnum.class))
					.tasktypes(EnumToList.enumNames(TaskTypeEnum.class))
					.evaluationstatuses(EnumToList.enumNames(EvaluationStatusEnum.class))
					.companyVirtues(getCurrentCompanyVirtueModels())
					.leaderVirtues(getCurrentLeaderVirtueModels())
					.activePeriod(getActivePeriod())
					.periodstatuses(EnumToList.enumNames(PeriodStatusEnum.class))
					.factors(getCompanyFactorModels());
		
		return ResponseEntity.ok(model);
	}


	
	private List<CompanyVirtue> getCurrentCompanyVirtues() {
		return companyVirtueRepository.findAllByActiveTrueAndCompanyAndValueIsNotNullOrderByValue(getCurrentCompany());
	}

	private List<CompanyVirtueModel> getCurrentCompanyVirtueModels() {
		return getCurrentCompanyVirtues()
					.stream().map(p -> conversionService.convert(p, CompanyVirtueModel.class))
					.collect(Collectors.toList());
	}

	private List<LeaderVirtue> getCurrentLeaderVirtues() {
		val leader = getCurrentLeader();
		if (leader == null) {
			return Collections.emptyList();
		}
		val result = leaderVirtueRepository.findAllByActiveTrueAndOwnerOrderByValue(getCurrentLeader());
		return result;
	}

	private List<LeaderVirtueModel> getCurrentLeaderVirtueModels() {
		return getCurrentLeaderVirtues()
					.stream()
					.map(p -> conversionService.convert(p, LeaderVirtueModel.class))
					.collect(Collectors.toList());
	}
	
	private PeriodModel getActivePeriod() {
		val company = getCurrentCompany();
		val optPeriod = periodRepository.findByCompanyAndStatus(company, PeriodStatusEnum.ACTIVE);
		return optPeriod.isPresent() ? conversionService.convert(optPeriod.get(), PeriodModel.class) : null;
	}
	
	private Map<String, String> getCompanyParameters() {
		val currentCompany = getCurrentCompany();
		if (currentCompany == null) {
			return Collections.emptyMap();
		}
		Map<String, String> companyParameters = new HashMap<String, String>();
		val listOfSysVal = systemParameterRepository.findAll();
		listOfSysVal
			.stream()
			.filter(p -> !p.getCode().startsWith("secret"))
			.forEach(p -> companyParameters.put(p.getCode(), p.getValue()));
		
		val listOfCompVal = companyParameterRepository.findAllByCompany(currentCompany);
		listOfCompVal.stream().forEach(p -> companyParameters.put(p.getCode(), p.getValue()));
		return companyParameters;
	}
}
