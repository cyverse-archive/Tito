package org.iplantc.js.integrate;

import junit.framework.Test;

import org.iplantc.core.client.widgets.validator.rules.GwtTestDoubleAboveRule;
import org.iplantc.core.client.widgets.validator.rules.GwtTestDoubleBelowRule;
import org.iplantc.core.client.widgets.validator.rules.GwtTestRegexRule;
import org.iplantc.js.integrate.client.GwtTestProperty;
import org.iplantc.js.integrate.client.GwtTestPropertyGroup;
import org.iplantc.js.integrate.client.GwtTestPropertyGroupContainer;
import org.iplantc.js.integrate.client.models.GwtTestDataObject;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * A test suite that contains all js-integrate tests.
 * 
 * @author hariolf
 * 
 */
public class JSIntegrateSuite extends GWTTestCase {

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite("Tests for js-integrate"); //$NON-NLS-1$
        suite.addTestSuite(GwtTestDoubleAboveRule.class);
        suite.addTestSuite(GwtTestDoubleBelowRule.class);
        suite.addTestSuite(GwtTestRegexRule.class);
        suite.addTestSuite(GwtTestProperty.class);
        suite.addTestSuite(GwtTestPropertyGroup.class);
        suite.addTestSuite(GwtTestPropertyGroupContainer.class);
        suite.addTestSuite(GwtTestDataObject.class);
        return suite;
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.js.integrate.JSIntegrate"; //$NON-NLS-1$
    }
}
