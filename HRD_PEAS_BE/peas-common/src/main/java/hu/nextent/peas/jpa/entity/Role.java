package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 * Szabályok
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "role",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_name", columnNames = {"name"}),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Role extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = -2373268188609721092L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "name", nullable = false, updatable = true, length = 100)
	@ColumnDefault(value = "'USER'")
	@Builder.Default private RoleEnum name = RoleEnum.USER;
	
    @Column(name = "external", nullable = true, length = 100)
    private String external;

    @Size(max=255)
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Singular("roleXRoleItem")
	@Nullable
	@OneToMany(
			mappedBy="role",
	        cascade = CascadeType.ALL
	    )
	private List<RoleXRoleItem> roleXRoleItems;
}
