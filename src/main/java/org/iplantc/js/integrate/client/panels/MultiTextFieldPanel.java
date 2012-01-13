package org.iplantc.js.integrate.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.Widget;

/**
 * A component that allows the user to enter a number of strings.<br/>
 * One text field is displayed initially; the user can add new fields or remove them via "plus" and
 * "minus" buttons next to the fields.
 * 
 * @author hariolf
 * 
 */
public class MultiTextFieldPanel extends LayoutContainer {
    private static final int BUTTON_WIDTH = 20;

    List<TextField<String>> textFields;
    Button addButton;

    /**
     * Creates a new MultiTextFieldPanel containing one empty text field.
     */
    public MultiTextFieldPanel() {
        textFields = new ArrayList<TextField<String>>();

        addListener(Events.AfterLayout, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                for (TextField<String> field : textFields) {
                    field.setWidth(getWidth() - BUTTON_WIDTH);
                }
            }
        });
    }

    /**
     * Creates a new MultiTextFieldPanel from a list of strings. For each string, a text field is added.
     * 
     * @param values
     */
    public MultiTextFieldPanel(List<String> values) {
        this();
        addFields(values);
    }

    private Button buildAddButton() {
        addButton = new Button("+"); //$NON-NLS-1$
        addButton.setWidth(BUTTON_WIDTH);
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                addField();
                focusLastField();
            }
        });
        return addButton;
    }

    private SelectionListener<ButtonEvent> createRemoveButtonListener(final Button button) {
        return new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Widget w = button.getParent();
                if (w != null) {
                    w.removeFromParent();

                    HorizontalPanel panel = (HorizontalPanel)w;
                    Component c = panel.getItem(0);
                    textFields.remove(c);
                }
            }
        };
    }

    private void focusLastField() {
        if (textFields != null && !textFields.isEmpty()) {
            textFields.get(textFields.size()-1).focus();
        }
    }
    
    /**
     * Adds an empty text field after the last one.
     */
    public void addField() {
        addField(""); //$NON-NLS-1$
    }

    /**
     * Adds a new text field after the last one and sets a string value.
     * 
     * @param text
     */
    public void addField(String text) {
        if (addButton != null) {
            addButton.setText("-"); //$NON-NLS-1$
            addButton.removeAllListeners();
            addButton.addSelectionListener(createRemoveButtonListener(addButton));
        }
        
        TextField<String> field = new TextField<String>();
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        field.setValue(text);
        textFields.add(field);

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(field);

        hPanel.add(buildAddButton());
        add(hPanel);
        layout();
    }

    /**
     * Adds a new field for every string in a list.
     * 
     * @param values
     */
    public void addFields(List<String> values) {
        for (String value : values) {
            addField(value);
        }
    }

    /**
     * Returns the values of the text fields.
     * 
     * @return
     */
    public List<String> getValues() {
        List<String> values = new ArrayList<String>();
        for (int i = 0; i < textFields.size(); i++) {
            String value = textFields.get(i).getValue();
            if (value != null && !value.isEmpty()) {
                values.add(value);
            }
        }
        return values;
    }
}