package org.iplantc.js.integrate.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ExecutableChangeEventHandler extends EventHandler {

    void onChange(ExecutableChangeEvent event);
}
