package org.iplantc.core.client.widgets.validator.rules;

import java.util.Arrays;
import java.util.List;

import org.iplantc.core.client.widgets.strategies.IWizardValidationBroadcastStrategy;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestDoubleBelowRule extends GWTTestCase {

    @Test
    public void testValidate() {
        List<String> params = Arrays.asList("5.7"); //$NON-NLS-1$
        DoubleBelowRule rule = new DoubleBelowRule(params);
        ComponentValueTable tbl = new ComponentValueTable(new IWizardValidationBroadcastStrategy() {
            @Override
            public void broadcast(List<String> errors) {
            }
        });
        assertNull(rule.validate(tbl, "testField1", "5.4")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotNull(rule.validate(tbl, "testField2", "5.8")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.core.tito"; //$NON-NLS-1$
    }
}
