package org.iplantc.core.tito.client.widgets.validation;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.models.RuleType;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Lets the user choose a rule type.
 * @author hariolf
 *
 */
public class RuleTypeDropDown extends GenericDropDown<RuleType, JSONString> {

    /**
     * Creates a new RuleTypeDropDown an populates it asynchronosly.
     * 
     * @param afterLoad a command to execute after the drop-down has been populated
     */
    public RuleTypeDropDown(Command afterLoad) {
        super(new ArrayList<RuleType>(), RuleType.DESCRIPTION);
        combo.setWidth(400);
        loadValues(afterLoad);
    }

    /**
     * Loads rule types via a service and executes a command if the service call finished successfully.
     * @param afterLoad
     */
    private void loadValues(final Command afterLoad) {
        EnumerationServices services = new EnumerationServices();
        services.getRuleTypes(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                List<RuleType> rules = new ArrayList<RuleType>();

                if (result != null) {
                    JSONObject obj = JSONParser.parseStrict(result).isObject();
                    if (obj != null) {
                        JSONArray arr = obj.get("rule_types").isArray(); //$NON-NLS-1$

                        for (int i = 0; i < arr.size(); i++) {
                            RuleType r = new RuleType(arr.get(i).isObject());
                            rules.add(r);
                        }
                    }
                }

                addToStore(rules);

                afterLoad.execute();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadRuleTypes(), caught);
            }
        });

    }

    /**
     * Filters the items so only rules in a given category are shown.
     * If showAboveBelowRules is false, the "int above" and "int below" rules are
     * not shown even if they are in the selected category.
     * @param category
     * @param showAboveBelowRules
     */
    public void setFilter(final PropertyTypeCategory category, final boolean showAboveBelowRules) {
        combo.getStore().clearFilters();
        StoreFilter<RuleType> filter = new StoreFilter<RuleType>() {
            @Override
            public boolean select(Store<RuleType> store, RuleType parent, RuleType item, String property) {
                boolean isInCategory = category.isOneOf(item.getCategoriesAsList());
                if (!showAboveBelowRules) {
                    isInCategory &= !item.getName().equals("IntAbove"); //$NON-NLS-1$
                    isInCategory &= !item.getName().equals("IntBelow"); //$NON-NLS-1$
                }
                return isInCategory;
            }
        };
        combo.getStore().addFilter(filter);
        ListStore<RuleType> store = combo.getStore();
        for (int i = 0; i < store.getCount(); i++) {
            RuleType item = store.getAt(i);
            if (filter.select(store, null, item, null)) {
                combo.setValue(item);
                break;
            }
        }
    }

    /**
     * Returns a RuleType for a given type string, or null if no rule matches.
     * @param typeName
     * @return
     */
    public RuleType getRuleTypeByName(String typeName) {
        ListStore<RuleType> store = combo.getStore();
        for (int i = 0; i < store.getCount(); i++) {
            RuleType item = store.getAt(i);
            if (typeName.equals(item.getName())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Selects a given item.
     * @see setValue(JSONString)
     * @param typeName type string that matches the rule to select
     */
    public void setValue(String typeName) {
        setValue(new JSONString(typeName));
    }

    @Override
    public JSONString getValue() {
        RuleType t = combo.getValue();
        if (t != null) {
            return new JSONString(t.get(RuleType.NAME).toString());
        } else {
            return new JSONString(""); //$NON-NLS-1$
        }
    }

    @Override
    protected boolean equals(RuleType element, JSONString value) {
        return element.getName().equals(value.stringValue());
    }
}
