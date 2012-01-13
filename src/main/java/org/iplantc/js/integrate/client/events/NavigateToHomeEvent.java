package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.GwtEvent;
/**
 * 
 * An event that is fired when an user tries to navigate to home
 * @author sriram
 * 
 */
public class NavigateToHomeEvent extends GwtEvent<NavigateToHomeEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigateToHomeEventHandler
     */
    public static final GwtEvent.Type<NavigateToHomeEventHandler> TYPE = new GwtEvent.Type<NavigateToHomeEventHandler>();

    @Override
    protected void dispatch(NavigateToHomeEventHandler arg0) {
        arg0.onHome();

    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NavigateToHomeEventHandler> getAssociatedType() {
        return TYPE;
    }

}
