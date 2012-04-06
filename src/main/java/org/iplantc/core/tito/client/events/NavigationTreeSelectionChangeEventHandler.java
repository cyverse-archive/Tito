package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An event handler for NavigationTreeSelectionChangeEvent
 * 
 * @author sriram
 *
 */
public interface NavigationTreeSelectionChangeEventHandler extends EventHandler {

    /**
     * invoked when selection change happens for Navigation tree selection change
     * 
     * @param event
     */
    void onSelectionChange(NavigationTreeSelectionChangeEvent event);

}
