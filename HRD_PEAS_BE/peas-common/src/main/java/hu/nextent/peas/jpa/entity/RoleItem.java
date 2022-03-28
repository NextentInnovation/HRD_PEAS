package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Entity
@Table(name = "roleitem",
        uniqueConstraints = {
                @UniqueConstraint(name = "fk_roleitem_name", columnNames = {"name"}),
        }
)
@org.hibernate.annotations.Table( appliesTo = "roleitem", comment = "Role Item" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class RoleItem extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = -2679729126797287962L;

	@NotNull
    @Column(name = "name", nullable = false, length = 100)
	private String name;

    @Singular("roleXRoleItem")
	@Nullable
	@OneToMany(
			mappedBy="roleItem",
	        cascade = CascadeType.ALL
	    )
	private List<RoleXRoleItem> roleXRoleItems;

}
