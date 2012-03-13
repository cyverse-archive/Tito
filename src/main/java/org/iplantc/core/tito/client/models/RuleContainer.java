package org.iplantc.core.tito.client.models;

import java.util.List;

import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONValue;

/**
 * 
 * A model class for MetaDataRule
 * 
 * @author sriram
 *
 */

@SuppressWarnings("serial")
public class RuleContainer extends BaseModelData {
    public static String CAPTION = "caption"; //$NON-NLS-1$
    public static String RULE = "property"; //$NON-NLS-1$

    /**
     * Create a new instance of MetaDataRule
     * 
     * @param rule
     */
    public RuleContainer(MetaDataRule rule) {
        set(RULE, rule);
        setCaption();
    }

    /**
     * set the caption
     * 
     */
    public void setCaption() {
        set(CAPTION, toString(getRule()));
    }

    /**
     * This method is not in MetaDataRule because I18N isn't available there, and because it is
     * presentation logic.
     * 
     * @param rule
     * @return a string representation of the rule that can be displayed to the user
     */
    private String toString(MetaDataRule rule) {
        String type = rule.getType();
        List<JSONValue> params = rule.getParams();

        if ("IntBelow".equals(type)) { //$NON-NLS-1$
            return I18N.DISPLAY.intBelow(params.get(0).toString());
        } else if ("IntAbove".equals(type)) { //$NON-NLS-1$
            return I18N.DISPLAY.intAbove(params.get(0).toString());
        } else if ("IntRange".equals(type)) { //$NON-NLS-1$
            return I18N.DISPLAY.intRange(params.get(0).toString(), params.get(1).toString());
        } else {
            return unknownRuleToString(rule);
        }
    }

    private String unknownRuleToString(MetaDataRule rule) {
        StringBuilder builder = new StringBuilder();

        builder.append(rule.getType());

        List<JSONValue> params = rule.getParams();

        if (!params.isEmpty()) {
            builder.append(" - " + I18N.DISPLAY.valueParenS() + ": "); //$NON-NLS-1$ //$NON-NLS-2$

            for (JSONValue param : params) {
                builder.append(param);
                builder.append(", "); //$NON-NLS-1$
            }

            builder.delete(builder.length() - 2, builder.length()); // remove trailing chars
        }

        return builder.toString();
    }

    /**
     * get the caption
     * 
     * @return caption
     */
    public String getCaption() {
        return get(CAPTION);
    }

    /**
     * get the rule
     * 
     * @return MetaDataRule
     */
    public MetaDataRule getRule() {
        return get(RULE);
    }
}
