package org.iplantc.js.integrate.client.events;

import org.iplantc.core.metadata.client.property.Property;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a node is added to the Navigation tree
 * 
 * @author sriram
 * 
 */
public class CommandLineArgumentChangeEvent extends GwtEvent<CommandLineArgumentChangeEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.js.integrate.client.events.CommandLineArgumentChangeEventHandler
     */
    public static final GwtEvent.Type<CommandLineArgumentChangeEventHandler> TYPE = new GwtEvent.Type<CommandLineArgumentChangeEventHandler>();

    private Property property;

    /**
     * @param property the property whose cmd line arg changed
     */
    public CommandLineArgumentChangeEvent(Property property) {
        this.property = property;
    }

    /**
     * @return the property whose cmd line arg changed
     */
    public Property getProperty() {
        return property;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<CommandLineArgumentChangeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CommandLineArgumentChangeEventHandler handler) {
        handler.onChange(this);
    }

}
