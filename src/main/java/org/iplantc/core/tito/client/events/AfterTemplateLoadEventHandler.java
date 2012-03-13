package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An event handler for AfterTemplateLoadEvent
 * @author sriram
 *
 */
public interface AfterTemplateLoadEventHandler extends EventHandler {
    
    /**
     * invoked after template has been loaded
     * @param event
     */
    void onLoad(AfterTemplateLoadEvent event);
}
