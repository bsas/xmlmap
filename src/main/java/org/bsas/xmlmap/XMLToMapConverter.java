package org.bsas.xmlmap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Converter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * <b>XMLToMapConverter</b> is a utility class that converts any XML to a simple
 * Map<String, Object> type.
 * </p>
 * <p>
 * The idea is to easily parse and extract simple information, avoiding the
 * complexity of using XPath or any heavy-weight XML tool
 * </p>
 * <p>
 * It can be used with Apache Camel as a Converter.
 * </p>
 * 
 * @author Bernardo Silva (bernardo.silva@gmail.com)
 */
@Converter
public final class XMLToMapConverter {
	private XMLToMapConverter() {
	}

	@Converter
	public static Map<String, Object> fromInputStream(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		return fromInputStream(inputStream, true);
	}

	public static Map<String, Object> fromInputStream(InputStream inputStream, boolean addAttributes) throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(inputStream);
		return fromDocumentToMap(doc, addAttributes);
	}

	@Converter
	public static Map<String, Object> fromDocumentToMap(Document doc) {
		return fromDocumentToMap(doc, true);
	}

	public static Map<String, Object> fromDocumentToMap(Document doc, boolean addAttributes) {
		return fromNodeListToMap(doc.getChildNodes(), addAttributes);
	}

	@Converter
	public static Map<String, Object> fromXMLToMap(String xml) throws ParserConfigurationException, SAXException, IOException {
		return fromXMLToMap(xml, true);
	}

	public static Map<String, Object> fromXMLToMap(String xml, boolean addAttributes) throws ParserConfigurationException, SAXException, IOException {
		return fromInputStream(new ByteArrayInputStream(xml.getBytes()), addAttributes);
	}

	@Converter
	public static Map<String, Object> fromNodeListToMap(NodeList nodeList) {
		return fromNodeListToMap(nodeList, true);
	}

	public static Map<String, Object> fromNodeListToMap(NodeList nodeList, boolean addAttributes) {
		final Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			final String nodeName = node.getNodeName();
			if (node.hasChildNodes()) {
				final NodeList childNodeList = node.getChildNodes();
				if (childNodeList.getLength() == 1 && childNodeList.item(0).getNodeType() == Node.TEXT_NODE) {
					addValueToMap(nodeName, childNodeList.item(0).getNodeValue(), map);
				} else {
					addValueToMap(nodeName, fromNodeListToMap(childNodeList, addAttributes), map);
				}
			}
			if (addAttributes && node.hasAttributes()) {
				final NamedNodeMap attributes = node.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					final String attributeName = attributes.item(j).getNodeName();
					final String attributeValue = attributes.item(j).getNodeValue();
					addValueToMap(nodeName + '@' + attributeName, attributeValue, map);
				}
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addValueToMap(String nodeName, Object nodeValue, Map<String, Object> map) {
		if (map.containsKey(nodeName)) {
			final Object oldValue = map.get(nodeName);
			if (oldValue instanceof List) {
				((List) oldValue).add(nodeValue);
			} else {
				map.put(nodeName, new ArrayList(Arrays.asList(new Object[] { oldValue, nodeValue })));
			}
		} else {
			map.put(nodeName, nodeValue);
		}
	}

}
