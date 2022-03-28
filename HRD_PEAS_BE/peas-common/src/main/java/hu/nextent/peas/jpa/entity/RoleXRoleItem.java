package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "rolexroleitem",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rolexroleitem", columnNames = {"role_id", "roleitem_id"}),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class RoleXRoleItem extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = -7224404390524890449L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "role_id", 
			foreignKey = @ForeignKey(name = "fk_rolexroleitem_role_id"), 
			nullable = false, insertable = true,updatable = false
			)
	@ToString.Exclude
	private Role role;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "roleitem_id", 
			foreignKey = @ForeignKey(name = "fk_rolexroleitem_roleitem_id"), 
			nullable = false, insertable = true,updatable = false
			)
	@ToString.Exclude
	private RoleItem roleItem;

}
