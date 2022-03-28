package hu.nextent.peas.converters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.AutocompletItemModel;
import lombok.val;

@Component
public class AutocompletConverter
implements GenericConverter, InitializingBean {


	private static Set<ConvertiblePair> convertibleTypes = 
			new HashSet<>(
					Arrays.asList(
							new ConvertiblePair(Task.class, AutocompletItemModel.class),
							new ConvertiblePair(User.class, AutocompletItemModel.class),
							new ConvertiblePair(Company.class, AutocompletItemModel.class),
							new ConvertiblePair(Difficulty.class, AutocompletItemModel.class),
							new ConvertiblePair(CompanyVirtue.class, AutocompletItemModel.class),
							new ConvertiblePair(LeaderVirtue.class, AutocompletItemModel.class),
							new ConvertiblePair(Factor.class, AutocompletItemModel.class)
							)
					);
	
	@Autowired
	private ConverterRegistry converterRegistry;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		converterRegistry.addConverter(this);
	}
	
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return convertibleTypes;
	}

	
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (sourceType.getType().equals(Task.class)) {
			Task task = (Task)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", task.getId());
			autocomplet.put("name", task.getName());
			autocomplet.put("description", task.getDescription());
			return autocomplet;
		} else if (sourceType.getType().equals(User.class)) {
			User user = (User)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", user.getId());
			autocomplet.put("name", user.getUserName());
			autocomplet.put("description", user.getFullName());
			autocomplet.put("email", user.getEmail());
			return autocomplet;
		} else if (sourceType.getType().equals(Company.class)) {
			Company company = (Company)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", company.getId());
			autocomplet.put("name", company.getName());
			autocomplet.put("description", company.getFullName());
			return autocomplet;			
		} else if (sourceType.getType().equals(Difficulty.class)) {
			Difficulty diff = (Difficulty)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", diff.getId());
			autocomplet.put("name", diff.getName());
			autocomplet.put("description", diff.getDescription());
			return autocomplet;
		} else if (sourceType.getType().equals(CompanyVirtue.class)) {
			CompanyVirtue companyVirtue = (CompanyVirtue)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", companyVirtue.getId());
			autocomplet.put("name", companyVirtue.getValue());
			autocomplet.put("description", companyVirtue.getValue());
			return autocomplet;
		} else if (sourceType.getType().equals(LeaderVirtue.class)) {
			LeaderVirtue leaderVirtue = (LeaderVirtue)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", leaderVirtue.getId());
			autocomplet.put("name", leaderVirtue.getValue());
			autocomplet.put("description", leaderVirtue.getValue());
			return autocomplet;
		} else if (sourceType.getType().equals(Factor.class)) {
			Factor factor = (Factor)source;
			val autocomplet = new AutocompletItemModel();
			autocomplet.put("id", factor.getId());
			autocomplet.put("name", factor.getName());
			autocomplet.put("description", factor.getName());
			return autocomplet;
		}
		return null;
	}

}
