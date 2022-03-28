package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
@Table(name = "emailtemplate"
        , uniqueConstraints = {
                @UniqueConstraint(name = "uk_emailtemplate", columnNames = {"language", "code"}),
        }
)
@org.hibernate.annotations.Table( appliesTo = "emailtemplate", comment = "Email minta" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class EmailTemplate extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 738983660050713403L;

	@Size(max=100)
	@NotNull
    @Column(name = "language", nullable = false, length = 100)
    private String language;
	
    @Size(max=100)
	@NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;
	
	 /**
     * Üzenet tárgya, rövid leírása
     */
    @NotNull
    @Column(name = "subject", nullable = false, length = 1000)
    private String subject;

    /**
     * Üzenet tartalma
     */
    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;
	
}
