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

    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.outPutMultiplicityOption();
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        DataObject obj = getDataObject();

        if (obj != null) {
            cbxImplicitOutput.setValue(obj.isImplicit());
            initTextField(outputFileNameField, obj.getOutputFilename());
        }

        super.initFieldValues();
    }

    @Override
    protected void buildFields() {
        buildOutputFileNameField();
        buildImplicitOutputCheckbox();

        super.buildFields();
    }

    public String getOutputFilename() {
        String filename = outputFileNameField.getValue();
        return filename == null ? "" : filename; //$NON-NLS-1$
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
