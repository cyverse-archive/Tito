package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * An event handler for NavigateToHomeEvent
 * @author sriram
 * 
 */
public interface NavigateToHomeEventHandler extends EventHandler {
    /**
     * invoked when home button is clicked
     */
    void onHome();
}
