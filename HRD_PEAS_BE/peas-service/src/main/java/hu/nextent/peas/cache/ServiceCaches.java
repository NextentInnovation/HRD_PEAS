package hu.nextent.peas.cache;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.NotificationAction;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;

public interface ServiceCaches {

	public String getSystemParam(@NotNull String key);
	public <T> T getSystemParam(@NotNull String key, @NotNull Class<T> targetClass);
	
	public String getCompanyParam(@NotNull String key, @Nullable Long companyId);
	public String getCompanyParam(@NotNull String key, @Nullable Company company);
	public <T> T getCompanyParam(@NotNull String key, @NotNull Class<T> targetClass, @Nullable Long companyId);
	public <T> T getCompanyParam(@NotNull String key, @NotNull Class<T> targetClass, @Nullable Company company);
	
	public String getLabel(@NotNull String code, @NotNull String language);
	public @NotNull Map<String, String> labelMapProxy(@NotNull String language);
	
	public @NotNull NotificationAction getNotificationAction(@NotNull NotificationTypeEnum notificationType, @Nullable Long companyId);
	public @NotNull NotificationAction getNotificationAction(@NotNull NotificationTypeEnum notificationType, @Nullable Company company);
	 
}