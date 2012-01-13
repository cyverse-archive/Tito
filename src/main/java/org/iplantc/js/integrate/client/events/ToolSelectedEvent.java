package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a tool is selected or de selected for integration
 * @author sriram
 * 
 */
public class ToolSelectedEvent extends GwtEvent<ToolSelectedEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<ToolSelectedEventHandler> TYPE = new GwtEvent.Type<ToolSelectedEventHandler>();

    private boolean isSelected;

    /**
     * 
     * @param selected boolean whether a tool was selected or de-selected
     */
    public ToolSelectedEvent(boolean selected) {
        setSelected(selected);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ToolSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ToolSelectedEventHandler handler) {
        handler.onSelection(this);

    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

}
