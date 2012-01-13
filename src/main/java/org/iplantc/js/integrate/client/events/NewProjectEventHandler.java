package org.iplantc.js.integrate.client.events;

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

    /**
     * Fired when a user wants a new interface to an existing tool.
     */
    void newInterface();

    /**
     * Fired when a user wants to compose a new workflow.
     */
    void newWorkflow();
}
