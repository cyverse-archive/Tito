package org.iplantc.core.tito.client.panels;

import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.events.CommandLineArgumentChangeEvent;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public abstract class PropertyTypeEditorPanel extends VerticalPanel {
    public static final String DEFAULT_STRING = ""; //$NON-NLS-1$

    private static final String ID_FLD_CMD_L_OPTN = "idFldCmdLOptn"; //$NON-NLS-1$

    protected final Property property;

    private TextFieldContainer pnlCommandLineOption;

    protected PropertyTypeEditorPanel(Property property) {
        this.property = property;

        init();
        buildFields();
        initFieldValues();
        addFields();
    }

    protected void buildFields() {
        buildCommandLineOptionPanel();
    }

    protected void addFields() {
        add(pnlCommandLineOption);
    }

    protected void initFieldValues() {
        TextField<String> field = pnlCommandLineOption.getField();

        initTextField(field, property.getName());
        field.focus();
    }

    private void init() {
        setSize(450, 450);
        setLayout(new FitLayout());
    }

    private void buildCommandLineOptionPanel() {
        String caption = I18N.DISPLAY.flag();

        TextField<String> field = buildTextField(ID_FLD_CMD_L_OPTN, 255, 128, new FlagEditKeyUpCommand());
        IPlantValidator.setRegexRestrictedCmdLineChars(field, caption);

        pnlCommandLineOption = buildTextFieldContainer(caption, field);
    }

    protected void updatePropertyName(String value) {
        property.setName(value);
    }

    protected void initTextField(TextField<String> field, String value) {
        if (field != null && value != null && !value.isEmpty()) {
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

    protected TextFieldContainer buildTextFieldContainer(final String caption, TextField<String> field) {
        Label label = new Label(caption + ":"); //$NON-NLS-1$
        return new TextFieldContainer(label, field);
    }

    protected CheckBox buildCheckBox(String id, String label, Listener<BaseEvent> changeListener) {
        CheckBox ret = new CheckBox();

        ret.setId(id);
        ret.setBoxLabel(label);

        if (changeListener != null) {
            ret.addListener(Events.Change, changeListener);
        }

        return ret;
    }

    private void fireCommandLineArgumentChangeEvent() {
        EventBus.getInstance().fireEvent(new CommandLineArgumentChangeEvent(property));
    }

    protected interface KeyUpCommand {
        void handleNullInput();

        void execute(String value);
    }

    private class FlagEditKeyUpCommand implements KeyUpCommand {
        @Override
        public void execute(String value) {
            if (value == null) {
                handleNullInput();
            } else {
                updatePropertyName(value);
            }

            fireCommandLineArgumentChangeEvent();
        }

        @Override
        public void handleNullInput() {
            property.setName(DEFAULT_STRING);
        }
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
