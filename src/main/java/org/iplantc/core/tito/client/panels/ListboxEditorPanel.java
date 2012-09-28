package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.tito.client.dialogs.validation.BasicEditableList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;

public abstract class ListboxEditorPanel extends AbstractListPropertyEditorPanel {
    public ListboxEditorPanel(final Property property) {
        super(property);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void allocateList() {
        list = buildList(parseMustContainValues());
    }

    /**
     * Build the BasicEditableList with the given JSON values.
     * 
     * @param values MustContain rule arguments.
     * @return A BasicEditableList populated with the given values.
     */
    protected abstract BasicEditableList buildList(JSONArray values);

    /**
     * A command class to update property when list (must contain) is edited
     * 
     * @author sriram
     * 
     */
    protected class UpdatePropertyWithList implements Command {
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
