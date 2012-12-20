package uk.co.badgersinfoil.metaas;

import java.util.Collection;
import junit.framework.Assert;

public class ExtraAssertions {
	public static void assertInstanceof(Object instance, Class type) {
		Assert.assertTrue("expected an instance of <"+type.getName()+">, but got <"+instance.getClass().getName()+">",
		                  type.isInstance(instance));
	}
	
	public static void assertSize(int size, Collection c) {
		Assert.assertEquals(size, c.size());
	}
}
