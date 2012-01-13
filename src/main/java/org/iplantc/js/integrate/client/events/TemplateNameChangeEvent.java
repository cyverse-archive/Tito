package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a template name is changed
 * @author sriram
 * 
 */
public class TemplateNameChangeEvent extends GwtEvent<TemplateNameChangeEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.TemplateNameChangeEventHandler
     */
    public static final GwtEvent.Type<TemplateNameChangeEventHandler> TYPE = new GwtEvent.Type<TemplateNameChangeEventHandler>();
    private String newValue;

    public TemplateNameChangeEvent(String newValue) {
        this.newValue = newValue;
    }

    /**
     * @return the new template name
     */
    public String getNewValue() {
        return newValue;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<TemplateNameChangeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TemplateNameChangeEventHandler handler) {
        handler.onSelectionChange(this);
    }

}
