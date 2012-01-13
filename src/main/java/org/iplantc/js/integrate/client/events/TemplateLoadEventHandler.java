package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An event handler for TemplateLoadEvent
 * @author sriram
 *
 */
public interface TemplateLoadEventHandler extends EventHandler {
    
    /**
     * invoked when template is loaded
     * @param event
     */
    void onLoad(TemplateLoadEvent event);
}
