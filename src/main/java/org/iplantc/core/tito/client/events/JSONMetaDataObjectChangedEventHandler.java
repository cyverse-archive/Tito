package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;
/**
 * 
 * An event handler for PropertyChangedEvent
 * @author sriram
 * 
 */
public interface JSONMetaDataObjectChangedEventHandler extends EventHandler {
    /**
     * invoked when a property is changed
     * @param event
     */
    void onFire(JSONMetaDataObjectChangedEvent event);
}
