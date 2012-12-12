package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.tito.client.widgets.validation.ListEditor;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * An abstract class for all special list property editing requirements.
 * 
 * @author sriram
 * 
 */
public abstract class ListEditorPanel extends LayoutContainer {
    protected final Property property;
    protected ListEditor list;

    public ListEditorPanel(final Property property) {
        this.property = property;

        setLayout(new FitLayout());
        allocateList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        if (list != null) {
            add((Widget)list);
        }
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
     * Init the BasicEditableList for this category
     */
    protected void allocateList() {
        list = buildList(parseMustContainValues());
    }

    /**
     * Build the BasicEditableList with the given JSON values.
     * 
     * @param values MustContain rule arguments.
     * @return A BasicEditableList populated with the given values.
     */
    protected abstract ListEditor buildList(JSONArray values);

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
            }

            ruleMustContain.setParams(values);
        }
    }
}
