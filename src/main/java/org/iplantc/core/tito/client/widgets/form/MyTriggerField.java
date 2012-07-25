package org.iplantc.core.tito.client.widgets.form;

import com.extjs.gxt.ui.client.widget.form.TriggerField;

public class MyTriggerField<D> extends TriggerField<D> {

    /**
     * Reset the trigger style by updating the DOM element directly
     * 
     * @param triggerStyle
     */
    public void resetTriggerStyle(final String triggerStyle) {
        if (trigger != null) {
            trigger.dom.setClassName("x-form-trigger " + triggerStyle);
        }

    }
}
