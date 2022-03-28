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

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "leadervirtue")
@org.hibernate.annotations.Table( appliesTo = "leadervirtue", comment = "Csoport értékek" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class LeaderVirtue extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6801212172861328962L;
	
	@NotNull
	@Column(name = "value", nullable = true, length = 255)
	private String value;

	@Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
	@Builder.Default private Boolean active = true;

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "owner_id", 
			foreignKey = @ForeignKey(name = "fk_leadervirtue_user_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private User owner;  // Tulajdonos

}
