package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

/**
 * A form panel to collect input DataObject
 * 
 * @author sriram
 *
 */
public class InputDataObjectFormPanel extends DataObjectFormPanel {

    public InputDataObjectFormPanel(Property property) {
        super(property);
    }

    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.inputMultiplicityOption();
    }
}
