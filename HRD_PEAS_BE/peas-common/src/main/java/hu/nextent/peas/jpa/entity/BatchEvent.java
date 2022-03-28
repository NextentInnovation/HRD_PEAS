package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "batchevent"
	//, uniqueConstraints = { @UniqueConstraint(name = "uk_company_name", columnNames = { "name" }), }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class BatchEvent extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 1443080146144311602L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "batchEventType", nullable = false, updatable = true, length = 100)
	@Builder.Default 
	private BatchEventTypeEnum batchEventType = null;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@Builder.Default 
	private BatchStatusEnum status = BatchStatusEnum.NEW;
	
	// Batch elvégzése
	@Nullable
	@Column(name = "done", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime done;
	
	@Nullable
	@Column(name = "error", nullable = true, insertable = true, updatable = true, length = 1000)
	private String error;
	
	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_batchevent_company_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
	@Nullable
	@Column(updatable = false, nullable = true)
	private Long parameterId;
}
