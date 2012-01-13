package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.EventHandler;
/**
 * 
 * An event handler for TemplateSaveEvent
 * @author sriram
 * 
 */
public interface TemplateSaveEventHandler extends EventHandler {
    /**
     * 
     * method to be invoked on save
     * 
     * @param event TemplateSaveEvent
     */
    void onSave(TemplateSaveEvent event);
}
