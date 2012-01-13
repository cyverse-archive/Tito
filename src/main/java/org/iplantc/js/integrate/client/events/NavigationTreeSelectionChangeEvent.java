package org.iplantc.js.integrate.client.events;

import org.iplantc.js.integrate.client.models.MetaDataTreeModel;

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
     * @see org.iplantc.js.integrate.client.events.NavigationTreeSelectionChangeEventHandler
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
