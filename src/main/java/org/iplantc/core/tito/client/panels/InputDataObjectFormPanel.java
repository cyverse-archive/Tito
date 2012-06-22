package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * A form panel to collect input DataObject
 * 
 * @author sriram
 *
 */
public class InputDataObjectFormPanel extends DataObjectFormPanel {
    private TextFieldContainer pnlPropertyLabel;
    private TextFieldContainer pnlToolTip;

    public InputDataObjectFormPanel(Property property) {
        super(property);
    }

    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.inputMultiplicityOption();
    }

    @Override
    protected void buildFields() {
        super.buildFields();

        pnlPropertyLabel = buildLabelFieldContainer();
        pnlToolTip = buildToolTipFieldContainer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        DataObject obj = getDataObject();

        if (obj != null) {
            initTextField(pnlPropertyLabel.getField(), obj.getLabel());
            initTextField(pnlToolTip.getField(), obj.getDescription());
        }

        super.initFieldValues();
    }

    @Override
    protected void addFields() {
        super.addFields();

        add(pnlPropertyLabel);
        add(pnlToolTip);

        add(new Label(multiplicityGroup.getFieldLabel() + ":")); //$NON-NLS-1$
        add(multiplicityGroup);
        add(new Label(infoTypeField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(infoTypeField);
    }
}
