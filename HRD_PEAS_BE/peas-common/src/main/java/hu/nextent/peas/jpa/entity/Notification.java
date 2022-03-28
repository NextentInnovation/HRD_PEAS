package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(
		name = "notification"
		)
@org.hibernate.annotations.Table( 
		appliesTo = "notification", 
		comment = 
		"Notifikációs megjegyzések" 
		)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Notification extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 3638618446968251644L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "notificationType", nullable = false, updatable = true, length = 100)
	@Builder.Default private NotificationTypeEnum notificationType = NotificationTypeEnum.OTHER;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@Builder.Default private NotificationStatusEnum status = NotificationStatusEnum.OPEN;
	
	// Jelzi, hogy szükség van-e szöveg generálásra
	// A teszt adatok feltöltésekor szükséges, hogy generáljon-e szöveget
	@NotNull
    @Column(name = "needGenerate", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "0")
    @Builder.Default private Boolean needGenerate = false;
	
	// Nyelvesített üzenet kód
	@NotNull
	@Column(name = "subject", nullable = false, insertable = true, updatable = true, length = 1000)
	private String subject;

	@NotNull
	@Column(name = "body", nullable = false, insertable = true, updatable = true, length = 1000)
	private String body;

	// Olvasott notifikáció
	@Nullable
	@Column(name = "readed", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
	private OffsetDateTime readed;
	
	// Notifikáció napja
	@Nullable
	@Column(name = "notifacededDay", nullable = true, updatable = true)
	private LocalDate notifacededDay;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "referenceType", nullable = false, updatable = true, length = 20)
	private ReferenceTypeEnum referenceType;
	
	// Jelzi, hogy email el lett küldve
	@NotNull
	@Enumerated(EnumType.STRING)
    @Column(name = "sendedStatus", nullable = false, updatable = true, length = 20)
    @ColumnDefault(value = "'NEW'")
    @Builder.Default private NotificationSendStatusEnum sendedStatus = NotificationSendStatusEnum.NEW;
	
	@Nullable
	@Column(name = "sendedError", nullable = true, insertable = true, updatable = true, length = 1000)
	private String sendedError;
	
	// Redundás értéke, a gyors keresés miatt lett betével
	// Ez mind megtalálható vagy a task-ban vagy az értékelésben
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "to_user_id", 
			foreignKey = 
			@ForeignKey(name = "fk_notification_to_user_id"), 
			nullable = false, insertable = true, updatable = true)
	@ToString.Exclude
	private User toUser;
	
	@Nullable
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "task_id", 
			foreignKey = @ForeignKey(name = "fk_notification_task_id"), 
			nullable = true, 
			insertable = true, 
			updatable = true
			)
	private Task task;
	
	@Nullable
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "evaluation_id", 
			foreignKey = @ForeignKey(name = "fk_notification_evaluation_id"), 
			nullable = true, 
			insertable = true, 
			updatable = true
			)
	private Evaluation evaluation;

	@Nullable
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "rating_id", 
			foreignKey = @ForeignKey(name = "fk_notification_rating_id"), 
			nullable = true, 
			insertable = true, 
			updatable = true
			)
	private Rating rating;

	@Nullable
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "leadervirtue_id", 
			foreignKey = @ForeignKey(name = "fk_notification_leadervirtue_id"), 
			nullable = true, 
			insertable = true, 
			updatable = true
			)
	private LeaderVirtue leaderVirtue;

	@Nullable
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "period_id", 
			foreignKey = @ForeignKey(name = "fk_notification_period_id"), 
			nullable = true, 
			insertable = true, 
			updatable = true
			)
	private Period period;
	
    @NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_notification_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = true
			)
	private Company company;
}
