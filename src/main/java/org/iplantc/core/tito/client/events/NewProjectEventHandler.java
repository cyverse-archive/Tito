package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for new project events.
 * 
 * @author amuir
 * 
 */
public interface NewProjectEventHandler extends EventHandler {
    /**
     * Fired when a user wants to integrate a new tool.
     */
    void newTool();
}
