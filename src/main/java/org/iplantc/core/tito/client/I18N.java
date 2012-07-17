package org.iplantc.core.tito.client;

import org.iplantc.core.uicommons.client.CommonUIErrorStrings;

import com.google.gwt.core.client.GWT;

/**
 * Provides static access to localized strings.
 * 
 * @author lenards
 * 
 */
public class I18N {
    public static final DisplayStrings DISPLAY = (DisplayStrings)GWT.create(DisplayStrings.class);
    public static final CommonUIErrorStrings ERROR = (CommonUIErrorStrings)GWT
            .create(CommonUIErrorStrings.class);
}