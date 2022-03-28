package hu.nextent.peas.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumToList {

	
	public static <E extends Enum<?>> List<String> enumNames(Class<E> enumClass) {
		return Stream.of(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
	}

	public static <E extends Enum<?>> List<E> enums(Class<E> enumClass) {
		return Arrays.asList(enumClass.getEnumConstants());
	}

}
