package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

// Meghívott külső értékelők egy adott értékeléshez
//TODO Ezeket majd egy automattikus folyamatnak tisztítania kell
@Entity
@Table(name = "virtualuser", uniqueConstraints = {
		@UniqueConstraint(name = "uk_virtualuser_email", columnNames = { "company_id", "email" }), 
		})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class VirtualUser extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 1425531820882971767L;

	@NotNull
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "name", nullable = true, length = 100)
	private String name;

	// Ide kell majd az értékelést megadni
	@NotNull
	@Column(name = "componentRef", nullable = false, length = 100)
	private String componentRef;
	
	@NotNull
    @Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    private Boolean active;
	
	@Column(name = "validTo", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private OffsetDateTime validTo;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_virtualuser_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
}
