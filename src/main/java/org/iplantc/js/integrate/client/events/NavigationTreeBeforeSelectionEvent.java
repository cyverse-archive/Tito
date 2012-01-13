package org.iplantc.js.integrate.client.events;

import org.iplantc.js.integrate.client.models.MetaDataTreeModel;

import com.google.gwt.event.shared.GwtEvent;
/**
 * 
 * An event that is fired before when before a node is selected in navigation tree
 * @author sriram
 * 
 */
public class NavigationTreeBeforeSelectionEvent extends
        GwtEvent<NavigationTreeBeforeSelectionEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigationTreeSelectionChangeEventHandler
     */
    public static final GwtEvent.Type<NavigationTreeBeforeSelectionEventHandler> TYPE = new GwtEvent.Type<NavigationTreeBeforeSelectionEventHandler>();
    private MetaDataTreeModel model;
    private boolean cancelled;

    /**
     * create a new instance of NavigationTreeBeforeSelectionEvent
     * 
     * @param model a model thats needs to saved before navigation
     */
    public NavigationTreeBeforeSelectionEvent(MetaDataTreeModel model) {
        this.model = model;
    }

    /**
     * @return the tree model
     */
    public MetaDataTreeModel getModel() {
        return model;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NavigationTreeBeforeSelectionEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NavigationTreeBeforeSelectionEventHandler handler) {
        handler.beforeSelectionChange(this);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
