package org.iplantc.js.integrate.client;

import com.google.gwt.core.client.GWT;

/**
 * Provides static access to localized strings.
 * 
 * @author lenards
 * 
 */
public class I18N {
    public static final DisplayStrings DISPLAY = (DisplayStrings)GWT.create(DisplayStrings.class);
}