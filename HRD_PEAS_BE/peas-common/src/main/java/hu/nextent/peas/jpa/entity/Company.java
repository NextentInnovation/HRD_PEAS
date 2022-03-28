package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "company", uniqueConstraints = { @UniqueConstraint(name = "uk_company_name", columnNames = { "name" }), })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Company extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = 3059694142144718863L;

	@NotNull
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@NotNull
	@Column(name = "fullName", nullable = true, length = 100)
	private String fullName;
	
    @Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    @Builder.Default private Boolean active = true;
    
	// Email
	@Column(name = "email", nullable = true, length = 100)
	private String email;
}
