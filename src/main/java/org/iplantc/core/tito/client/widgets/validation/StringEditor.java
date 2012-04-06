package org.iplantc.core.tito.client.widgets.validation;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.json.client.JSONString;

/**
 * A PropertyEditor for strings.
 * 
 * @author hariolf
 * 
 */
public class StringEditor extends AbstractPropertyEditor<JSONString> {
    private static final long serialVersionUID = 7273589843495381671L;

    private TextField<String> field;

    /**
     * Creates a PropertyEditor with a given string value.
     * 
     * @param value
     */
    public StringEditor(JSONString value) {
        buildField();

        if (value != null && !JsonUtil.trim(value.toString()).isEmpty()) {
            setValue(value);
        }
    }

    @Override
    public void setValue(JSONString value) {
        field.setValue(value.stringValue());
    }

    @Override
    public JSONString getValue() {
        String value = field.getValue();

        return (value == null) ? null : new JSONString(value);
    }

    public void setAllowBlank(boolean allowBlank) {
        field.setAllowBlank(allowBlank);
    }

    @Override
    public boolean validate() {
        return field.validate();
    }

    @Override
    public Component getEditorComponent() {
        return field;
    }

    private void buildField() {
        field = new TextField<String>();
        field.setSelectOnFocus(true);
        field.setValidateOnBlur(false);
    }
}
