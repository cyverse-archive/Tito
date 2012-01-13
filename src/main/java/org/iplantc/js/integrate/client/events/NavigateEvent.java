package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a user tries to navigate to home
 * @author sriram
 * 
 */
public class NavigateEvent extends GwtEvent<NavigateEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigateToHomeEventHandler
     */
    public static final GwtEvent.Type<NavigateEventHandler> TYPE = new GwtEvent.Type<NavigateEventHandler>();

    @Override
    protected void dispatch(NavigateEventHandler arg0) {
        arg0.onHome();

    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NavigateEventHandler> getAssociatedType() {
        return TYPE;
    }

}
