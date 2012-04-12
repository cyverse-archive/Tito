package org.iplantc.core.tito.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a template is saved
 * @author sriram
 * 
 */
public class TemplateSaveEvent extends GwtEvent<TemplateSaveEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.tito.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<TemplateSaveEventHandler> TYPE = new GwtEvent.Type<TemplateSaveEventHandler>();

    private String titoId;
    
    public TemplateSaveEvent(String titoId) {
    	this.titoId = titoId;
    }
    
    
    @Override
    protected void dispatch(TemplateSaveEventHandler handler) {
        handler.onSave(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<TemplateSaveEventHandler> getAssociatedType() {
        return TYPE;
    }

	public String getTitoId() {
		return titoId;
	}

}
