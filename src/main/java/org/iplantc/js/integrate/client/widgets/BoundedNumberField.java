package org.iplantc.js.integrate.client.widgets;

import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.user.client.Element;

/**
 * A bounded number field that does not allow users to type beneath the defined maximum length
 * 
 * @author sriram
 *
 */
public class BoundedNumberField extends NumberField {
    @Override
    public void setMaxLength(int m) {
        super.setMaxLength(m);

        if (rendered) {
            getInputEl().setElementAttribute("maxLength", m); //$NON-NLS-1$
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength()); //$NON-NLS-1$
    }
}
