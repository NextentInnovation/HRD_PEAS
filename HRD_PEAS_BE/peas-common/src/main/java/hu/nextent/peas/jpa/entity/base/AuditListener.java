package hu.nextent.peas.jpa.entity.base;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import hu.nextent.peas.jpa.constans.HibernateConstant;
import hu.nextent.peas.jpa.utils.LoggedPlacholder;

public class AuditListener {
	
	@PrePersist
    public void onCreate(Object auditable) {
		touchCreate(getAudit(auditable));
    }
 
    @PreUpdate
    public void onUpdate(Object auditable) {
		touchUpdate(getAudit(auditable));
    }
    
    Optional<JpaEntityAudit> getAudit(Object auditable) {
    	try {
    		return Optional.ofNullable((JpaEntityAudit)auditable);
    	} catch(ClassCastException e) {
    		return Optional.empty();
    	}
    }
    
    OffsetDateTime getUtcNow() {
    	return OffsetDateTime.now(ZoneOffset.UTC);
    }
    
    void touchCreate(Optional<JpaEntityAudit> auditable) {
    	if (auditable == null || !auditable.isPresent()) {
    		return;
    	}
    	if (auditable.get().getCreatedDate() == null) {
    		auditable.get().setCreatedDate(getUtcNow());
    	}
    	if (auditable.get().getCreatedBy() == null) {
    		auditable.get().setCreatedBy(Optional.ofNullable(LoggedPlacholder.getUserName()).orElse(HibernateConstant.UNKNOW_USER) );
    	}
    }
    
    void touchUpdate(Optional<JpaEntityAudit> auditable) {
    	if (auditable == null || !auditable.isPresent()) {
    		return;
    	}
    	auditable.get().setModifiedDate(getUtcNow());
    	auditable.get().setModifiedBy( Optional.ofNullable(LoggedPlacholder.getUserName()).orElse(HibernateConstant.UNKNOW_USER) );
    }
    
}
