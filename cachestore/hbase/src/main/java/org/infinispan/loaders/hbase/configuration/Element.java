package org.infinispan.loaders.hbase.configuration;

import java.util.HashMap;
import java.util.Map;

import org.infinispan.loaders.hbase.HBaseCacheStore;

/**
 * An enumeration of all the recognized XML element local names for the {@link HBaseCacheStore}
 *
 * @author Tristan Tarrant
 * @since 5.2
 */
public enum Element {
    // must be first
    UNKNOWN(null),

    HBASE_STORE("hbaseStore"),
    ;

    private final String name;

    Element(final String name) {
        this.name = name;
    }

    /**
     * Get the local name of this element.
     *
     * @return the local name
     */
    public String getLocalName() {
        return name;
    }

    private static final Map<String, Element> MAP;

    static {
        final Map<String, Element> map = new HashMap<String, Element>(8);
        for (Element element : values()) {
            final String name = element.getLocalName();
            if (name != null) {
               map.put(name, element);
            }
        }
        MAP = map;
    }

    public static Element forName(final String localName) {
        final Element element = MAP.get(localName);
        return element == null ? UNKNOWN : element;
    }
}
