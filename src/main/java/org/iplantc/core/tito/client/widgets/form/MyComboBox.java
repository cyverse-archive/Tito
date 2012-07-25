package org.iplantc.core.tito.client.widgets.form;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.Element;

/**
 * This class is a simple override of the gxt <code>ComboBox</code> class. This class provides the
 * ability to use the trigger of the <code>ComboBox</code> class as needed. Additionally, this class also
 * allows the user to assign an ID to the trigger itself.
 * 
 * @author jstroot
 * 
 * @param <D>
 */
public class MyComboBox<D extends ModelData> extends ComboBox<D> {
    private String triggerID;
    @Override
    protected void onTriggerClick(ComponentEvent ce) {
        fireEvent(Events.TriggerClick, ce);
    }

    public void setTriggerId(String id) {
        triggerID = id;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        if (!triggerID.isEmpty() && (triggerID.length() != 0)) {
            trigger.setId(triggerID);
        }
    }
}