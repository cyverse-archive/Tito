package org.iplantc.js.integrate.client.widgets.validation;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * An interface to a UI component that lets the user edit values of a given type.
 * 
 * @author hariolf
 * 
 * @param <T> the type of data to edit
 */
public interface PropertyEditor<T> {
    void setValue(T value);

    T getValue();

    boolean validate();

    Component getEditorComponent();

    void addChangeListener(PropertyChangeListener<T> listener);
}
