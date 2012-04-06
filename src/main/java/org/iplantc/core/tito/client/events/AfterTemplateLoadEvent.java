package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired after a templated has been loaded
 * @author sriram
 * 
 */
public class AfterTemplateLoadEvent extends GwtEvent<AfterTemplateLoadEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.tito.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<AfterTemplateLoadEventHandler> TYPE = new GwtEvent.Type<AfterTemplateLoadEventHandler>();

    private String idTemplate;
    private boolean success;

    public AfterTemplateLoadEvent(String id, boolean success) {
        this.idTemplate = id;
        this.success = success;
    }

    /**
     * @return the idTemplate
     */
    public String getIdTemplate() {
        return idTemplate;
    }

    @Override
    protected void dispatch(AfterTemplateLoadEventHandler handler) {
        handler.onLoad(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AfterTemplateLoadEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return whether the template was successfully loaded, false on failure
     */
    public boolean isSuccessful() {
        return success;
    }

}
