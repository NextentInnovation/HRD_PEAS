package hu.nextent.peas.jpa.entity.base;

import java.time.OffsetDateTime;

public interface JpaEntityAudit<T> extends JpaEntity<T> {

	OffsetDateTime getCreatedDate();

	void setCreatedDate(OffsetDateTime createdAt);

	OffsetDateTime getModifiedDate();

	void setModifiedDate(OffsetDateTime modifiedAt);

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	String getModifiedBy();

	void setModifiedBy(String modifiedBy);

}