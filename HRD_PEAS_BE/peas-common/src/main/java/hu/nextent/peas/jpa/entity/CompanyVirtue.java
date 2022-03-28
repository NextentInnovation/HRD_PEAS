package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "companyvirtue")
@org.hibernate.annotations.Table( appliesTo = "companyvirtue", comment = "Vállalati értékek" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class CompanyVirtue extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6801212172861328962L;
	
	// Megjelenített érték
	@Nullable
	@Column(name = "value", nullable = true, length = 255)
	private String value;

	// szerkeszthető érték
	@Nullable
	@Column(name = "editvalue", nullable = true, length = 255)
	private String editvalue;

	@Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
	@Builder.Default private Boolean active = true;

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_companyvirtue_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;


}
