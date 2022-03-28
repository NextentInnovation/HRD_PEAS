package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "companyparameter", uniqueConstraints = { 
		@UniqueConstraint(name = "uk_companyparameter_name", columnNames = { "company_id","code" }), })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class CompanyParameter extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = 4626450651160507082L;
	
    @Size(max=100)
	@NotNull
    @Column(name = "code", nullable = false, length = 100)
	private String code;
    
    @Size(max=100)
    @Column(name = "value", nullable = true, length = 100)
    private String value;
	
    @Size(max=255)
    @Column(name = "name", nullable = true, length = 255)
    private String name;

    
    @NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_companyparameter_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;

}
