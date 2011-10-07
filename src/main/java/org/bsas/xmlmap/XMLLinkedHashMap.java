package org.bsas.xmlmap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class XMLLinkedHashMap extends LinkedHashMap<String, Object> {

	public List<?> getList(String key) {
		if (containsKey(key)) {
			Object value = get(key);
			if (!(value instanceof List)) {
				value = Collections.singleton(value);
			}
			return (List<?>) value;
		}
		return null;
	}

	public Object getValue(String key) {
		if (containsKey(key)) {
			Object value = get(key);
			if (value instanceof List) {
				value = ((List<?>) value).get(0);
			}
			return value instanceof List || value instanceof Map ? null : value;
		}
		return null;
	}

	public XMLLinkedHashMap getMap(String key) {
		if (containsKey(key)) {
			Object value = get(key);
			if (value instanceof List) {
				value = ((List<?>) value).get(1);
			}
			return value instanceof XMLLinkedHashMap ? (XMLLinkedHashMap) value : null;
		}
		return null;
	}

}
