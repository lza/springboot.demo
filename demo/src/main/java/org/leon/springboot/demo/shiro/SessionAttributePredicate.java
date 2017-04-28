package org.leon.springboot.demo.shiro;

import com.hazelcast.query.Predicate;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Map;

/**
 * Hazelcast query predicate for Shiro session attributes.
 */
public class SessionAttributePredicate<T> implements Predicate<Serializable, Session> {

    private final String attributeName;
    private final T attributeValue;

    public SessionAttributePredicate(String attributeName, T attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public T getAttributeValue() {
        return attributeValue;
    }

    @Override
    public boolean apply(Map.Entry<Serializable, Session> sessionEntry) {
        final T attribute = (T) sessionEntry.getValue().getAttribute(attributeName);
        return attribute.equals(attributeValue);
    }

}
