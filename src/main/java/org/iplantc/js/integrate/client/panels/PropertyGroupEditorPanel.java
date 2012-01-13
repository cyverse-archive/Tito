package org.iplantc.js.integrate.client.panels;

import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.metadata.client.property.groups.PropertyGroup;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.js.integrate.client.I18N;
import org.iplantc.js.integrate.client.events.JSONMetaDataObjectChangedEvent;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.user.client.Element;

/**
 * Panel for editing group specific data.
 * 
 * @author amuir
 * 
 */
public class PropertyGroupEditorPanel extends ContentPanel {
    private final PropertyGroup group;

    /**
     * Instantiate from the group to be edited.
     * 
     * @param group property group to be edited.
     */
    public PropertyGroupEditorPanel(final PropertyGroup group) {
        this.group = group;
        init();
    }

    private void init() {
        setHeaderVisible(false);
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$
    }

    private void fireEditEvent(final String name) {
        JSONMetaDataObjectChangedEvent event = new JSONMetaDataObjectChangedEvent(group);

        EventBus eventbus = EventBus.getInstance();
        eventbus.fireEvent(event);
    }

    private BoundedTextField<String> buildTextField() {
        final BoundedTextField<String> ret = new BoundedTextField<String>();

        ret.setWidth(265);
        ret.setSelectOnFocus(true);
        ret.setValue(group.getLabel());
        ret.setMaxLength(32);

        ret.addKeyListener(new KeyListener() {
            public void componentKeyUp(ComponentEvent event) {
                if (ret.getValue() != null && !(ret.getValue().isEmpty())) {
                    group.setLabel(ret.getValue());
                    fireEditEvent(ret.getValue());
                }
            }
        });

        ret.focus();

        return ret;
    }

    private LayoutContainer buildMessagePanel() {
        VerticalPanel ret = new VerticalPanel();
        ret.setStyleAttribute("background-color", "#EDEDED"); //$NON-NLS-1$ //$NON-NLS-2$
        ret.setHeight("100%"); //$NON-NLS-1$
        ret.setSpacing(5);

        LayoutContainer pnlInner = new LayoutContainer();
        pnlInner.add(new Label(I18N.DISPLAY.groupName() + ":")); //$NON-NLS-1$
        pnlInner.add(buildTextField());

        ret.add(pnlInner);

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        add(buildMessagePanel());
    }
}
