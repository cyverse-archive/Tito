package org.iplantc.js.integrate.client.events;

import org.iplantc.core.metadata.client.JSONMetaDataObject;

import com.google.gwt.event.shared.GwtEvent;
/**
 * 
 * An event that is fired when a Property is changed
 * @author sriram
 * 
 */
public class JSONMetaDataObjectChangedEvent extends GwtEvent<JSONMetaDataObjectChangedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.PropertyGroupChangedEventHandler
     */
    public static final GwtEvent.Type<JSONMetaDataObjectChangedEventHandler> TYPE = new GwtEvent.Type<JSONMetaDataObjectChangedEventHandler>();

    private final JSONMetaDataObject metaDataObject;

    public JSONMetaDataObjectChangedEvent(final JSONMetaDataObject metaDataObject) {
        this.metaDataObject = metaDataObject;
    }

    @Override
    protected void dispatch(JSONMetaDataObjectChangedEventHandler handler) {
        handler.onFire(this);
    }

    @Override
    public Type<JSONMetaDataObjectChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public JSONMetaDataObject getJSONMetaDataObject() {
        return metaDataObject;
    }
}
