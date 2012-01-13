package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface CommandLineArgumentChangeEventHandler extends EventHandler {

    void onChange(CommandLineArgumentChangeEvent event);
}
