package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Entity
@Table(name = "factor"
        //, uniqueConstraints = {
		//        @UniqueConstraint(name = "uk_factor", columnNames = {"company_id", "code"}),
		//}
)
@org.hibernate.annotations.Table( appliesTo = "factor", comment = "Értékelési szempontok" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Factor extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = -7675073360223824085L;

	@NotNull
	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 100)
	private String name;

	@Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
	@Builder.Default private Boolean active = true;
	
	@Column(name = "automatic", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "0")
	@Builder.Default private Boolean automatic = false;

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_factor_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
	@Singular("factorOption")
	@NotNull
	@OneToMany(
			mappedBy="factor",
	        cascade = CascadeType.ALL
	    )
	private List<FactorOption> factorOptions;

}
