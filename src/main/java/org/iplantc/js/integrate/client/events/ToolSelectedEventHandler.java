package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * An event handler for ToolSelectedEvent 
 * @author sriram
 * 
 */
public interface ToolSelectedEventHandler extends EventHandler {
    /**
     * 
     * invoked when a tool is selected for definition
     * @param event
     */
    void onSelection(ToolSelectedEvent event);
}
