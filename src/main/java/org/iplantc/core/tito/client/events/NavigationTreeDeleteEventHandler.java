package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;
/**
 * An event handler for NavigationTreeDeleteEvent
 * 
 * @author sriram
 *
 */
public interface NavigationTreeDeleteEventHandler extends EventHandler {
    void onDelete(NavigationTreeDeleteEvent event);
}
