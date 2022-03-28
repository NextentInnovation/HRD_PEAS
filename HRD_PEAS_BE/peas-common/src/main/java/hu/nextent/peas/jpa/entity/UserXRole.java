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
@Table(name = "userxrole",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_userxrole", columnNames = {"user_id", "role_id"}),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserXRole extends AuditableBaseEntity implements Serializable {


	private static final long serialVersionUID = 6656889711001618146L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "user_id", 
			foreignKey = 
			@ForeignKey(name = "fk_userxrole_user_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private User user;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "role_id", 
			foreignKey = @ForeignKey(name = "fk_userxrole_role_id"), 
			nullable = false, insertable = true,updatable = false
			)
	@ToString.Exclude
	private Role role;
	

	    
}
