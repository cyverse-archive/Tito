package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.tito.client.dialogs.validation.EditableNumberList;
import org.iplantc.core.tito.client.dialogs.validation.EditableStringList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;

public class ListboxEditorPanel extends AbstractListPropertyEditorPanel {
    public ListboxEditorPanel(final PropertyTypeCategory category, final Property property) {
        super(property, category);
    }

    private JSONArray parseMustContainValues() {
        MetaDataRule ruleMustContain = getMustContainRule();

        if (ruleMustContain != null) {
            return JsonUtil.buildArray(ruleMustContain.getParams());
        }

        return new JSONArray();
    }

    /**
     * @return The first MustContain rule found in the property validator.
     */
    protected MetaDataRule getMustContainRule() {
        MetaDataValidator validator = property.getValidator();

        if (validator != null && validator.getRules() != null) {
            for (MetaDataRule rule : validator.getRules()) {
                if (rule.getType().equalsIgnoreCase("MustContain")) { //$NON-NLS-1$
                    return rule;
                }
            }
        }

        return null;
    }

    @Override
    protected void allocateList(final PropertyTypeCategory category) {
        JSONArray values = parseMustContainValues();
        UpdatePropertyWithList updateCmd = new UpdatePropertyWithList();

        list = (category == PropertyTypeCategory.NUMBER) ? new EditableNumberList(values, updateCmd)
                : new EditableStringList(values, updateCmd);
    }

    /**
     * A command class to update property when list (must contain) is edited
     * 
     * @author sriram
     * 
     */
    private class UpdatePropertyWithList implements Command {
        @Override
        public void execute() {
            MetaDataRule ruleMustContain = null;
            MetaDataValidator validator = property.getValidator();

            if (validator == null) {
                validator = new MetaDataValidator();
                property.setValidator(validator);
            } else {
                ruleMustContain = getMustContainRule();
            }

            if (ruleMustContain == null) {
                ruleMustContain = new MetaDataRule("MustContain"); //$NON-NLS-1$
                validator.addRule(ruleMustContain);
            }

            List<JSONValue> values = new ArrayList<JSONValue>();
            JSONArray arr = list.getValue();

            for (int i = 0,len = arr.size(); i < len; i++) {
                JSONValue value = arr.get(i);
                values.add(value);

                JSONObject jsonItem = value.isObject();
                if (jsonItem != null) {
                    if (JsonUtil.getBoolean(jsonItem, MetaDataRule.IS_DEFAULT, false)) {
                        property.setValue(String.valueOf(i));
                    }
                }
            }

            ruleMustContain.setParams(values);
        }
    }
}
