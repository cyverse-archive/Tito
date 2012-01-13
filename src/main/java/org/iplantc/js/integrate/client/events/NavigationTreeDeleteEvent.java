package org.iplantc.js.integrate.client.events;

import org.iplantc.js.integrate.client.models.MetaDataTreeModel;
import org.iplantc.js.integrate.client.panels.NavigationTreePanel;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An event that is fired when a node in NavigationTree is deleted
 * 
 * @author sriram
 *
 */
public class NavigationTreeDeleteEvent extends GwtEvent<NavigationTreeDeleteEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigationTreeDeleteEventHandler
     */
    public static final GwtEvent.Type<NavigationTreeDeleteEventHandler> TYPE = new GwtEvent.Type<NavigationTreeDeleteEventHandler>();

    private MetaDataTreeModel selectedItem;
    private MetaDataTreeModel parent;
    private NavigationTreePanel treePanel;

    /**
     * create a new instance of NavigationTreeDeleteEvent
     * 
     * @param selectedItem selected node for delete
     * @param parent selected node's parent
     * @param treePanel the NavigationTreePanel from which the node was deleted
     */
    public NavigationTreeDeleteEvent(MetaDataTreeModel selectedItem, MetaDataTreeModel parent,
            NavigationTreePanel treePanel) {
        this.selectedItem = selectedItem;
        this.parent = parent;
        this.treePanel = treePanel;
    }

    /**
     * @return the selectedItem
     */
    public MetaDataTreeModel getSelectedItem() {
        return selectedItem;
    }

    /**
     * @return the parent
     */
    public MetaDataTreeModel getParent() {
        return parent;
    }

    /**
     * 
     * @return the NavigationTreePanel that the element was deleted from
     */
    public NavigationTreePanel getTreePanel() {
        return treePanel;
    }

    @Override
    protected void dispatch(NavigationTreeDeleteEventHandler handler) {

        handler.onDelete(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<NavigationTreeDeleteEventHandler> getAssociatedType() {

        return TYPE;
    }

}
