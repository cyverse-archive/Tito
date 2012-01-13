package org.iplantc.js.integrate.client.widgets.validation;

import org.iplantc.js.integrate.client.I18N;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.NumberPropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONNumber;

/**
 * A PropertyEditor for integer or double precision numbers.
 * 
 * @author hariolf
 * 
 */
public class NumberEditor extends AbstractPropertyEditor<JSONNumber> {
    private static final long serialVersionUID = 7273589843495381671L;
    private NumberField field;
    private boolean allowDecimals;

    /**
     * Creates a new IntegerEditor with a given number value.
     * 
     * @param value
     * @param allowDecimals whether fractional numbers can be entered
     */
    public NumberEditor(JSONNumber value, boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
        field = createField();
        setValue(value);
    }

    private NumberField createField() {
        NumberField field = new NumberField();
        field.setSelectOnFocus(true);

        if (!allowDecimals) {
            NumberPropertyEditor ne = new NumberPropertyEditor(Integer.class);
            ne.setFormat(NumberFormat.getFormat("#")); //$NON-NLS-1$
            field.setPropertyEditor(ne);
        }
        field.setAllowDecimals(allowDecimals);

        field.setHideTrigger(allowDecimals); // no spinner buttons for double values
        field.setIncrement(1);
        field.setAllowBlank(false);
        field.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                try {
                    if (allowDecimals) {
                        Double.valueOf(value);
                    } else {
                        Long.valueOf(value);
                    }
                    return null;
                } catch (NumberFormatException e) {
                    return allowDecimals ? I18N.DISPLAY.mustBeNumber() : I18N.DISPLAY.mustBeInt();
                }
            }
        });
        field.setAutoValidate(true);

        return field;
    }

    @Override
    public JSONNumber getValue() {
        Number number = field.getValue();
        if (number == null) {
            return null;
        }

        if (allowDecimals) {
            return new JSONNumber(number.doubleValue());
        } else {
            return new JSONNumber(number.intValue());
        }
    }

    @Override
    public void setValue(JSONNumber value) {
        if (value == null) {
            value = new JSONNumber(0);
        }

        field.setValue(value.doubleValue());
    }

    @Override
    public boolean validate() {
        return field.validate();
    }

    @Override
    public Component getEditorComponent() {
        return field;
    }
}
