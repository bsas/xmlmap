package org.bsas.xmlmap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * <b>XMLToMapConverter</b> is a utility class that converts any XML to
 * XMLLinkedHashMap type.
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
public final class XMLToMapConverter {

	private XMLToMapConverter() {
	}

	public static XMLLinkedHashMap fromInputStream(InputStream inputStream, boolean addAttributes) throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(inputStream);
		return fromDocumentToMap(doc, addAttributes);
	}

	public static XMLLinkedHashMap fromDocumentToMap(Document doc, boolean addAttributes) {
		return fromNodeListToMap(doc.getChildNodes(), addAttributes);
	}

	public static XMLLinkedHashMap fromXMLToMap(String xml, boolean addAttributes) throws ParserConfigurationException, SAXException, IOException {
		return fromInputStream(new ByteArrayInputStream(xml.getBytes()), addAttributes);
	}

	public static XMLLinkedHashMap fromNodeListToMap(NodeList nodeList, boolean addAttributes) {
		final XMLLinkedHashMap map = new XMLLinkedHashMap();
		for (int i=0; i<nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			final String nodeName = node.getNodeName();

			Object value = null;
			if (node.hasChildNodes()) {
				final NodeList childNodeList = node.getChildNodes();
				if (childNodeList.getLength() == 1 && childNodeList.item(0).getNodeType() == Node.TEXT_NODE) {
					value = childNodeList.item(0).getNodeValue();
				} else {
					value = fromNodeListToMap(childNodeList, addAttributes);
				}
			}
			if (value != null) {
				addValueToMap(nodeName, value, map);
			}

			if (addAttributes) {
				fromAttributesToMap(node, nodeName, map, value);
			}
		}
		return map;
	}

	private static void fromAttributesToMap(Node node, String nodeName, XMLLinkedHashMap map, Object value) {
		Object obj = value;
		if (node.hasAttributes()) {
			final NamedNodeMap attributes = node.getAttributes();
			for (int j=0; j<attributes.getLength(); j++) {
				final String attributeName = attributes.item(j).getNodeName();
				final String attributeValue = attributes.item(j).getNodeValue();
				if (!(obj instanceof Map)) {
					obj = new XMLLinkedHashMap();
					addValueToMap(nodeName, obj, map);
				}
				addValueToMap('@' + attributeName, attributeValue, (XMLLinkedHashMap) obj);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addValueToMap(String nodeName, Object nodeValue, XMLLinkedHashMap map) {
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
