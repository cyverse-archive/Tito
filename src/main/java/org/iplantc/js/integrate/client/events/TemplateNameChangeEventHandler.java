package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * An event handler for TemplateNameChangeEvent
 * @author sriram
 * 
 */
public interface TemplateNameChangeEventHandler extends EventHandler {

    /**
     * invoked when a template name is changed
     * 
     * @param event TemplateNameChangeEvent
     */
    void onSelectionChange(TemplateNameChangeEvent event);

}
