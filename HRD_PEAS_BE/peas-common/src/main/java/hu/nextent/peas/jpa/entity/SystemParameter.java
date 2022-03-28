package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 * Adatbázisba tárolt rendszer paraméterek
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "systemparameter",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_systemparameter_code", columnNames = {"code"}),
        }
)
@org.hibernate.annotations.Table( appliesTo = "systemparameter", comment = "System parameters" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class SystemParameter extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 5153369061550652668L;

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

}
