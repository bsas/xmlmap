package org.bsas.xmlmap.camel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Converter;
import org.bsas.xmlmap.XMLToMapConverter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Converter
public final class CamelXMLToMapConverter {
	private CamelXMLToMapConverter() {}

	@Converter
	public static Map<String, Object> fromInputStream(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		return XMLToMapConverter.fromInputStream(inputStream, true);
	}

	@Converter
	public static Map<String, Object> fromDocumentToMap(Document doc) {
		return XMLToMapConverter.fromDocumentToMap(doc, true);
	}

	@Converter
	public static Map<String, Object> fromXMLToMap(String xml) throws ParserConfigurationException, SAXException, IOException {
		return XMLToMapConverter.fromXMLToMap(xml, true);
	}

	@Converter
	public static Map<String, Object> fromNodeListToMap(NodeList nodeList) {
		return XMLToMapConverter.fromNodeListToMap(nodeList, true);
	}

}
