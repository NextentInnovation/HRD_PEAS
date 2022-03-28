package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
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
@Table(name = "label"
        , uniqueConstraints = {
                @UniqueConstraint(name = "uk_label_language_code", columnNames = {"language", "code"}),
		}
        , indexes = {
        		@Index(name = "idx_label_code", columnList = "code")
        }
)
@org.hibernate.annotations.Table( appliesTo = "label", comment = "Application Labels" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Label extends AuditableBaseEntity implements Serializable {


	private static final long serialVersionUID = -7642762173391371194L;

	@Size(max=100)
	@NotNull
    @Column(name = "language", nullable = false, length = 100)
    private String language;

    @Size(max=100)
	@NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;
	
    @Size(max=255)
    @Column(name = "label", nullable = true, length = 255)
    private String label;

}
