package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "notificationaction", uniqueConstraints = { @UniqueConstraint(name = "uk_notificationaction", columnNames = { "notificationType", "company_id" }), })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class NotificationAction extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6056637824140654371L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "notificationType", nullable = false, updatable = true, length = 100)
	@Builder.Default private NotificationTypeEnum notificationType = NotificationTypeEnum.OTHER;
	
	
	// Létrehozható ?
    @Column(name = "createable", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    @Builder.Default private Boolean createable = true;
	
    // Küldhető levél ?
    @Column(name = "sendable", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    @Builder.Default private Boolean sendable = true;
	
    // Felületen megjeleníthető ?
    @Column(name = "showable", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    @Builder.Default private Boolean showable = true;
	
	
    @Nullable
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_notificationaction_company_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
}
