package hu.nextent.peas.jpa.entity.base;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import hu.nextent.peas.jpa.constans.HibernateConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@MappedSuperclass
@EntityListeners(AuditListener.class)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AuditableBaseEntity 
extends BaseEntity 
implements JpaEntityAudit<Long>, Serializable 
{

	private static final long serialVersionUID = 3084179873070273495L;

	@Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    @ColumnDefault( value = "CURRENT_TIMESTAMP" )
    private OffsetDateTime createdDate;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    @LastModifiedDate
    private OffsetDateTime modifiedDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    @ColumnDefault( value = "'" + HibernateConstant.UNKNOW_USER + "'" )
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

}
