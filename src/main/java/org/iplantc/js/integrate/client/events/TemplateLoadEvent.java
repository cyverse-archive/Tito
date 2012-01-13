package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a templated is loaded
 * @author sriram
 * 
 */
public class TemplateLoadEvent extends GwtEvent<TemplateLoadEventHandler> {

    public static enum MODE {
        EDIT,
        
        COPY
      };
    
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<TemplateLoadEventHandler> TYPE = new GwtEvent.Type<TemplateLoadEventHandler>();

    private String idTemplate;
    private MODE mode;

    public TemplateLoadEvent(String id, MODE mode) {
        this.idTemplate = id;
        this.mode = mode;
    }

    /**
     * @return the idTemplate
     */
    public String getIdTemplate() {
        return idTemplate;
    }

    @Override
    protected void dispatch(TemplateLoadEventHandler handler) {
        handler.onLoad(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<TemplateLoadEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the mode
     */
    public MODE getMode() {
        return mode;
    }

}
