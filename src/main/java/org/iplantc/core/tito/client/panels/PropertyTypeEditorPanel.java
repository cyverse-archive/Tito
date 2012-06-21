package org.iplantc.core.tito.client.panels;

import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public abstract class PropertyTypeEditorPanel extends VerticalPanel {
    protected final Property property;

    protected PropertyTypeEditorPanel(Property property) {
        this.property = property;

        init();
        buildFields();
        initFieldValues();
        addFields();
    }

    protected abstract void buildFields();

    protected abstract void addFields();

    protected abstract void initFieldValues();

    private void init() {
        setSize(450, 450);
        setLayout(new FitLayout());
    }

    protected void initTextField(TextField<String> field, String value) {
        if (value != null && !value.isEmpty()) {
            field.setValue(value);
        }
    }

    protected TextField<String> buildTextField(String id, int width, int maxLength,
            final KeyUpCommand cmdKeyUp) {
        final TextField<String> ret = new BoundedTextField<String>();

        ret.setId(id);
        ret.setMaxLength(maxLength);
        ret.setWidth(width);
        ret.setSelectOnFocus(true);
        ret.setAutoValidate(true);

        if (cmdKeyUp != null) {
            ret.addKeyListener(new KeyListener() {
                @Override
                public void componentKeyUp(ComponentEvent event) {
                    cmdKeyUp.execute(ret.getValue());
                }
            });
        }

        return ret;
    }

    protected interface KeyUpCommand {
        void handleNullInput();

        void execute(String value);
    }

    /**
     * A simple subclass of LayoutContainer that holds a label and a text field, and provides access to
     * the field.
     * 
     * @author hariolf
     * 
     */
    protected static class TextFieldContainer extends LayoutContainer {
        private final TextField<String> field;

        protected TextFieldContainer(Label label, TextField<String> field) {
            this.field = field;
            this.field.setId(label.getText());

            setLayout(new FitLayout());
            add(label);
            add(this.field);
        }

        protected TextField<String> getField() {
            return field;
        }
    }

}
