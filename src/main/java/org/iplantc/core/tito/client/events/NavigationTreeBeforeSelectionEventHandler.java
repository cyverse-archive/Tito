package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author sriram
 *
 */
public interface NavigationTreeBeforeSelectionEventHandler extends EventHandler {

    /**
     * invoked before selection change happens
     * 
     * @param event NavigationTreeBeforeSelectionEvent
     */
    void beforeSelectionChange(NavigationTreeBeforeSelectionEvent event);

}
