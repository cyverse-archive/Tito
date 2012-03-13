package org.iplantc.core.tito.client;

import com.google.gwt.core.client.GWT;

/**
 * Static access to client constants.
 * 
 * @author lenards
 * 
 */
public class Constants {
    public static final TitoConstants CLIENT = (TitoConstants)GWT
            .create(TitoConstants.class);
}
