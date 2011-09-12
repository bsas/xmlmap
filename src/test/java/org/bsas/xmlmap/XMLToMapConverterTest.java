package org.bsas.xmlmap;

import java.util.Map;

import org.junit.Test;

public class XMLToMapConverterTest {

	@Test
	public void testSimpleFile() throws Exception {
		Map<?, ?> map = XMLToMapConverter.fromInputStream(XMLToMapConverter.class.getResourceAsStream("/Test1.xml"));
		System.out.println(map);
	}

}
