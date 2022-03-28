package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tokencache"
	//uniqueConstraints = { @UniqueConstraint(name = "uk_tokencache", columnNames = { "userid" }), }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class TokenCache extends AuditableBaseEntity implements Serializable {


	private static final long serialVersionUID = 1L;

	@Column(name = "remoteAddress", nullable = true, updatable = true, length = 50)
	private String remoteAddress;

	@Column(name = "exp", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    @ColumnDefault( value = "CURRENT_TIMESTAMP" )
    private OffsetDateTime exp;
	
	@Column(name = "nbf", nullable = true, updatable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    @ColumnDefault( value = "CURRENT_TIMESTAMP" )
    private OffsetDateTime nbf;
	
	@Column(name = "iat", nullable = true, updatable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    @ColumnDefault( value = "CURRENT_TIMESTAMP" )
	private OffsetDateTime iat;

	@NotNull
	@Column(name = "iss", nullable = false, updatable = false, length = 100)
	@ColumnDefault( value = "'Nextent'" )
	private String iss;

	@NotNull
	@Column(name = "sub", nullable = false, updatable = false, length = 100)
	private String sub;

	
	@ManyToOne
	@JoinColumn(
			name = "user_id", 
			foreignKey = @ForeignKey(name = "fk_tokencache_user_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false)
	@ToString.Exclude
	private User user;
	
}
