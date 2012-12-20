package uk.co.badgersinfoil.metaas;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import junit.framework.Assert;

/**
 * Helper for unit tests.
 */
public class EnumAssert extends Assert {
	public static void assertValidEnumConstants(Class clazz) throws IllegalArgumentException, IllegalAccessException {
		// Ensure toString() values match constant names,
		Field[] fields = clazz.getFields();
		for (int i=0; i<fields.length; i++) {
			Field f = fields[i];
			int mod = f.getModifiers();
			if (Modifier.isPublic(mod)
			 && Modifier.isStatic(mod)
			 && Modifier.isFinal(mod))
			{
				assertEquals(f.getName(), f.get(null).toString());
			}
		}
	}
}
