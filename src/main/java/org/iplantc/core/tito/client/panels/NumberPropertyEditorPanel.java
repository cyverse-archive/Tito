package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class NumberPropertyEditorPanel extends PropertyTypeEditorPanel {
    private static final String ID_FIELD_NUM = "idFieldNum"; //$NON-NLS-1$

    private TextFieldContainer pnlPropertyLabel;
    private NumberFieldContainer pnlDefaultValue;
    private TextFieldContainer pnlToolTip;

    public NumberPropertyEditorPanel(Property property) {
        super(property);
    }

    @Override
    protected void buildFields() {
        super.buildFields();

        pnlPropertyLabel = buildLabelFieldContainer();
        pnlDefaultValue = buildDefaultValueContainer();
        pnlToolTip = buildToolTipFieldContainer();

        buildOptionalFlagCheckbox();
        buildRequiredCheckBox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        super.initFieldValues();

        pnlDefaultValue.getField().setValue(parseNumberFromString(property.getValue()));

        initTextField(pnlPropertyLabel.getField(), property.getLabel());
        initTextField(pnlToolTip.getField(), property.getDescription());

        cbxOptionFlag.setValue(property.isOmit_if_blank());
        initRequiredCheckBox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields() {
        super.addFields();

        add(pnlPropertyLabel);
        add(pnlDefaultValue);

        add(cbxOptionFlag);
        add(cbxRequired);

        add(pnlToolTip);
    }

    /**
     * Constructs a NumberField for usage by a container.
     * 
     * @return a configured instance of the NumberField GXT widget
     */
    private NumberFieldContainer buildDefaultValueContainer() {
        return new NumberFieldContainer(I18N.DISPLAY.defaultValue(), buildNumberField(64,
                new ValueEditKeyUpCommand(), false));
    }

    /**
     * Provide an instance of the NumberField GXT widget with the arguments set.
     * 
     * @param width the desired width of the field
     * @param cmdKeyUp the command to execute when the on key up event fires
     * @param onlyPositiveValues configures this field to only allow positive values.
     * 
     * @return a configured instance of the NumberField GXT widget
     */
    private NumberField buildNumberField(int width, final KeyUpCommand cmdKeyUp,
            boolean onlyPositiveValues) {
        final NumberField ret = new NumberField();

        ret.setId(ID_FIELD_NUM);
        ret.setWidth(width);

        if (cmdKeyUp != null) {
            ret.addKeyListener(new KeyListener() {
                @Override
                public void componentKeyUp(ComponentEvent event) {
                    // always send the raw value so validation can tell no input from invalid input
                    cmdKeyUp.execute(ret.getRawValue());
                }
            });
        }

        if (onlyPositiveValues) {
            ret.setMinValue(0);
            ret.setAllowNegative(false);
            ret.setAllowDecimals(false);
        }

        return ret;
    }

    /**
     * Parses value into an int or a double, depending on the format of the given string, or the default
     * double value if the string cannot be parsed as a number.
     * 
     * @param value Number as a string to parse
     * @return Either the int or double value of the given string
     */
    private Number parseNumberFromString(String value) {
        Number numVal;

        if (isInt(value)) {
            numVal = Integer.parseInt(value);
        } else if (isDouble(value)) {
            numVal = Double.parseDouble(value);
        } else {
            // it's not an int or a double
            numVal = null;
            property.setValue(DEFAULT_STRING);
        }

        return numVal;
    }

    /**
     * Determines if the string contains a double precision numeric value.
     * 
     * @param test string that may or may not contain a number
     * @return true if the string contains a double; otherwise false.
     */
    private boolean isDouble(String test) {
        boolean ret = false; // assume failure

        try {
            if (test != null) {
                Double.parseDouble(test);

                // if we get here, we know parseDouble succeeded
                ret = true;
            }
        } catch (NumberFormatException nfe) {
            // we are assuming false - setting the return value here would be redundant
        }

        return ret;
    }

    /**
     * Determines if the string contains a number formatted as an integer value.
     * 
     * @param test string that may or may not contain an integer
     * @return true if the string contains a number formatted as an integer; otherwise false.
     */
    private boolean isInt(String test) {
        if (test == null) {
            return false;
        }

        try {
            Integer.parseInt(test);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * User interface for representing a default value that is a number.
     */
    private class NumberFieldContainer extends LayoutContainer {
        private final NumberField field;

        protected NumberFieldContainer(String caption, NumberField field) {
            this.field = field;

            setLayout(new FitLayout());

            add(new Label(caption + ":")); //$NON-NLS-1$
            add(this.field);
        }

        protected NumberField getField() {
            return field;
        }
    }
}
