package org.iplantc.core.tito.client.events;

import org.iplantc.core.metadata.client.JSONMetaDataObject;
import org.iplantc.core.tito.client.models.MetaDataTreeModel;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a node is added to the Navigation tree
 * @author sriram
 * 
 */
public class NavigationTreeAddEvent extends GwtEvent<NavigationTreeAddEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.tito.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<NavigationTreeAddEventHandler> TYPE = new GwtEvent.Type<NavigationTreeAddEventHandler>();

    private MetaDataTreeModel selectedItem;
    private JSONMetaDataObject newObject;

    /**
     * 
     * @param selectedElement the selected tree element
     * @param newId ID of the new tree element to be created
     * @param label a label for the new element
     */
    public NavigationTreeAddEvent(MetaDataTreeModel selectedItem, JSONMetaDataObject newObject) {
        this.selectedItem = selectedItem;
        this.newObject = newObject;
    }

    /**
     * @return the selected tree element
     */
    public MetaDataTreeModel getSelectedElement() {
        return selectedItem;
    }

    /**
     * @return the new tree element to be added
     */
    public JSONMetaDataObject getNewObject() {
        return newObject;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NavigationTreeAddEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NavigationTreeAddEventHandler handler) {
        handler.onAdd(this);
    }

}
