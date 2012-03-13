package org.iplantc.core.tito.client.events;

import org.iplantc.core.tito.client.models.MetaDataTreeModel;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An event that is fired when a selection changes in Navigation Tree 
 * 
 * @author sriram
 *
 */
public class NavigationTreeSelectionChangeEvent extends
        GwtEvent<NavigationTreeSelectionChangeEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.tito.client.events.NavigationTreeSelectionChangeEventHandler
     */
    public static final GwtEvent.Type<NavigationTreeSelectionChangeEventHandler> TYPE = new GwtEvent.Type<NavigationTreeSelectionChangeEventHandler>();
    private MetaDataTreeModel model;

    public NavigationTreeSelectionChangeEvent(MetaDataTreeModel model) {
        this.model = model;
    }

    /**
     * @return the tree model
     */
    public MetaDataTreeModel getModel() {
        return model;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NavigationTreeSelectionChangeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NavigationTreeSelectionChangeEventHandler handler) {
        handler.onSelectionChange(this);
    }

}
