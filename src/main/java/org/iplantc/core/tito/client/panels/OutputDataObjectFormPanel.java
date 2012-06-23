package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * A form panel to collect output DataObject
 * 
 * @author sriram
 *
 */
public class OutputDataObjectFormPanel extends DataObjectFormPanel {
    private static final String ID_IMPLICIT_OPT_CBX = "idImplicitOptCbx"; //$NON-NLS-1$
    private static final String ID_FLD_OP_NAME = "idFldOpName"; //$NON-NLS-1$

    private CheckBox cbxImplicitOutput;
    protected TextField<String> outputFileNameField;

    public OutputDataObjectFormPanel(Property property) {
        super(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.outPutMultiplicityOption();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        super.init();

        property.setType(DataObject.OUTPUT_TYPE);
        property.setVisible(false);
        property.setOmit_if_blank(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        buildOutputFileNameField();
        buildImplicitOutputCheckbox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        super.initFieldValues();

        DataObject obj = getDataObject();

        if (obj != null) {
            cbxImplicitOutput.setValue(obj.isImplicit());
            initTextField(outputFileNameField, obj.getOutputFilename());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields() {
        super.addFields();

        add(cbxImplicitOutput);
        add(new Label(outputFileNameField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(outputFileNameField);

        add(new Label(multiplicityGroup.getFieldLabel() + ":")); //$NON-NLS-1$
        add(multiplicityGroup);
        add(new Label(infoTypeField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(infoTypeField);
    }

    private void buildImplicitOutputCheckbox() {
        cbxImplicitOutput = buildCheckBox(ID_IMPLICIT_OPT_CBX, I18N.DISPLAY.implicitOutput(),
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        if (getDataObject() != null) {
                            getDataObject().setImplicit(cbxImplicitOutput.getValue());
                        }

                    }
                });
    }

    private void buildOutputFileNameField() {
        outputFileNameField = buildTextField(ID_FLD_OP_NAME, 100, 100,
                new OutputFilenameKeyUpCommand());

        outputFileNameField.setFieldLabel(I18N.DISPLAY.outputFileName());
        outputFileNameField.setAllowBlank(false);
        outputFileNameField.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private class OutputFilenameKeyUpCommand extends LabelEditKeyUpCommand {
        @Override
        public void execute(String value) {
            if (value != null) {
                getDataObject().setOutputFilename(value);
            }

            super.execute(value);
        }

        @Override
        public void handleNullInput() {
            getDataObject().setOutputFilename(DEFAULT_STRING);

            super.handleNullInput();
        }
    }
}
