package org.bsas.xmlmap.camel;

import java.util.Map;

import org.bsas.xmlmap.XMLToMapConverter;
import org.bsas.xmlmap.camel.CamelXMLToMapConverter;
import org.junit.Test;

public class CamelXMLToMapConverterTest {

	@Test
	public void testSimpleFile() throws Exception {
		Map<?, ?> map = CamelXMLToMapConverter.fromInputStream(XMLToMapConverter.class.getResourceAsStream("/Test1.xml"));
		System.out.println(map);
	}

	@Test
	public void testComplexFile() throws Exception {
		Map<?, ?> map = CamelXMLToMapConverter.fromInputStream(XMLToMapConverter.class.getResourceAsStream("/Test2.xml"));
		System.out.println(map);
	}

}
