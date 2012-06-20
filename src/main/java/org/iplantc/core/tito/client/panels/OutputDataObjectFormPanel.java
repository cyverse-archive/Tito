package org.iplantc.core.tito.client.panels;

import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.user.client.Command;

/**
 * A form panel to collect output DataObject
 * 
 * @author sriram
 *
 */
public class OutputDataObjectFormPanel extends DataObjectFormPanel {
    private static final String ID_FLD_OP_NAME = "idFldOpName"; //$NON-NLS-1$

    protected TextField<String> outputFileNameField;
    private Command outputFilenameChangeCommand;

    public OutputDataObjectFormPanel(Property property) {
        super(property);
    }

    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.outPutMultiplicityOption();
    }

    @Override
    protected void addFields() {
        add(new Label(outputFileNameField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(outputFileNameField);

        super.addFields();
    }

    /**
     * Sets a command that will be called when the "output file" field changes.
     * 
     * @param cmd
     */
    public void setOutputFilenameChangeCommand(Command cmd) {
        outputFilenameChangeCommand = cmd;
    }

    /**
     * set the form field values from the DataObject
     * 
     * @param obj an instance of DataObject to be loaded into this form
     */
    @Override
    protected void initFieldValues(DataObject obj) {
        if (obj != null) {
            initTextField(outputFileNameField, obj.getOutputFilename());
        }

        super.initFieldValues(obj);
    }

    @Override
    protected void buildFields() {
        outputFileNameField = buildTextField(ID_FLD_OP_NAME, I18N.DISPLAY.outputFileName(), false, null,
                100, null, new OutputFilenameKeyUpCommand());

        super.buildFields();
    }

    public String getOutputFilename() {
        String filename = outputFileNameField.getValue();
        return filename == null ? "" : filename; //$NON-NLS-1$
    }

    private void initTextField(TextField<String> field, String value) {
        if (value != null && !value.isEmpty()) {
            field.setValue(value);
        }
    }

    private TextField<String> buildTextField(String id, String label, boolean allowBlank,
            String defaultVal, int maxLength, Validator validator, final KeyUpCommand cmdKeyUp) {
        final TextField<String> field = new BoundedTextField<String>();

        field.setFieldLabel(label);
        field.setId(id);
        field.setAllowBlank(allowBlank);
        field.setMaxLength(maxLength);
        field.setValidateOnBlur(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        if (defaultVal != null) {
            field.setValue(defaultVal);
        }

        if (validator != null) {
            field.setValidator(validator);
        }

        if (cmdKeyUp != null) {
            field.addKeyListener(new KeyListener() {
                @Override
                public void componentKeyUp(ComponentEvent event) {
                    cmdKeyUp.execute(field.getValue());
                }
            });
        }

        return field;
    }

    private interface KeyUpCommand {
        void handleNullInput();

        void execute(String value);
    }

    private class OutputFilenameKeyUpCommand implements KeyUpCommand {
        @Override
        public void execute(String value) {
            getDataObject().setOutputFilename(value);
            outputFilenameChangeCommand.execute();
        }

        @Override
        public void handleNullInput() {
            getDataObject().setOutputFilename(""); //$NON-NLS-1$
            outputFilenameChangeCommand.execute();
        }
    }
}
