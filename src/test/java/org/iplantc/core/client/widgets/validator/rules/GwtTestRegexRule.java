package org.iplantc.core.client.widgets.validator.rules;

import java.util.Arrays;
import java.util.List;

import org.iplantc.core.client.widgets.strategies.IWizardValidationBroadcastStrategy;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestRegexRule extends GWTTestCase {

    public void testValidate() {
        RegexRule rule = new RegexRule(Arrays.asList(".+")); //$NON-NLS-1$
        ComponentValueTable tbl = new ComponentValueTable(new IWizardValidationBroadcastStrategy() {
            @Override
            public void broadcast(List<String> errors) {
            }
        });
        assertNull(rule.validate(tbl, "testField1", "test123")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotNull(rule.validate(tbl, "testField2", "")); //$NON-NLS-1$ //$NON-NLS-2$

        rule = new RegexRule(Arrays.asList("\\w+")); //$NON-NLS-1$
        assertNull(rule.validate(tbl, "testField1", "test123")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotNull(rule.validate(tbl, "testField2", "test.123")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.core.tito"; //$NON-NLS-1$
    }
}
