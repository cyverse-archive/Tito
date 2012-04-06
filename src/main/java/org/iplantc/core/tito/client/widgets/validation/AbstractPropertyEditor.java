package org.iplantc.core.tito.client.widgets.validation;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Abstract base class for PropertyEditors that provides a default addChangeListener implementation.
 * 
 * @author hariolf
 * 
 * @param <T>
 */
public abstract class AbstractPropertyEditor<T> implements PropertyEditor<T> {

    @Override
    public void addChangeListener(PropertyChangeListener<T> listener) {
        addChangeListener(Events.Change, listener);
        addChangeListener(Events.OnKeyUp, listener);
    }

    protected void addChangeListener(EventType eventType, final PropertyChangeListener<T> listener) {
        Listener<BaseEvent> componentListener = new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                listener.propertyChange(getValue());
            }
        };
        getEditorComponent().addListener(eventType, componentListener);
    }
}
