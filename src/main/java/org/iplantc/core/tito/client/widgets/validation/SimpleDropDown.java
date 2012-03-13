package org.iplantc.core.tito.client.widgets.validation;

import java.util.List;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.json.client.JSONString;

/**
 * A PropertyEditor that lets the user select from a list of predefined strings.
 * 
 * @author hariolf
 * 
 */
public class SimpleDropDown extends AbstractPropertyEditor<JSONString> {
    private SimpleComboBox<String> combo;

    public SimpleDropDown(List<String> values) {
        combo = new SimpleComboBox<String>();
        combo.add(values);
        combo.setTriggerAction(TriggerAction.ALL); // Always show all values
        combo.setEditable(false);
        if (!values.isEmpty()) {
            setValue(new JSONString(values.get(0)));
        }
    }

    /** Does nothing if the argument is null */
    @Override
    public void setValue(JSONString value) {
        if (value != null) {
            combo.setSimpleValue(value.stringValue());
        }
    }

    @Override
    public JSONString getValue() {
        return new JSONString(combo.getSimpleValue());
    }

    /**
     * 
     * @return true if the dropdown contains at least one item, false otherwise
     */
    public boolean isEmpty() {
        return combo.getStore().getCount() == 0;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Component getEditorComponent() {
        return combo;
    }
}
