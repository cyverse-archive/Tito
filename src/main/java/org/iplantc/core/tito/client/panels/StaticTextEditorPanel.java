package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.events.JSONMetaDataObjectChangedEvent;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.widgets.BoundedTextArea;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * A panel to edit static text
 * 
 * @author sriram
 *
 */
public class StaticTextEditorPanel extends ContentPanel {
    private final Property property;
    private TextArea textArea;

    /**
     * Creates an instance of StaticTextEditorPanel
     * 
     * @param property a property
     */
    public StaticTextEditorPanel(final Property property) {
        this.property = property;
        init();
    }

    private void init() {
        setHeaderVisible(false);
        setLayout(new FitLayout());
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$
        textArea = buildTextArea();
        add(buildPanel());
    }

    private TextArea buildTextArea() {
        textArea = new BoundedTextArea();
        textArea.setMaxLength(256);
        textArea.setValue(property.getLabel());
        textArea.setWidth(500);
        textArea.setHeight(150);
        textArea.setEmptyText(I18N.DISPLAY.staticTextEmpty());
        textArea.addListener(Events.OnKeyUp, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                fireEditEvent(textArea.getValue());
            }
        });
        textArea.addListener(Events.Focus, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                textArea.selectAll();
            }
        });
        return textArea;
    }

    private LayoutContainer buildPanel() {
        VerticalPanel ret = new VerticalPanel();
        ret.setLayout(new FitLayout());
        ret.setStyleAttribute("background-color", "#EDEDED"); //$NON-NLS-1$ //$NON-NLS-2$
        ret.setSpacing(8);

        ret.add(new Label(I18N.DISPLAY.staticTextLabel() + ":")); //$NON-NLS-1$

        ret.add(textArea);

        return ret;
    }

    private void fireEditEvent(final String text) {
        property.setLabel(text);
        JSONMetaDataObjectChangedEvent event = new JSONMetaDataObjectChangedEvent(property);

        EventBus eventbus = EventBus.getInstance();
        eventbus.fireEvent(event);
    }

    public boolean validate() {
        return textArea.validate();
    }
}
