package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a tool request is submitted
 * @author sriram
 * 
 */
public class NewToolRequestSubmitEvent extends GwtEvent<NewToolRequestSubmitEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<NewToolRequestSubmitEventHandler> TYPE = new GwtEvent.Type<NewToolRequestSubmitEventHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NewToolRequestSubmitEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NewToolRequestSubmitEventHandler handler) {
        handler.onRequestComplete(this);

    }

}
