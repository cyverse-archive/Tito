package org.iplantc.core.tito.client.widgets.validation;

/**
 * Listens for changes to property values.
 * 
 * @see PropertyEditor
 * @author hariolf
 * 
 * @param <T>
 */
public interface PropertyChangeListener<T> {

    /**
     * Called when a value changes.
     * 
     * @param newValue the new value
     */
    void propertyChange(T newValue);
}
