package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.Element;

/**
 * 
 * An editor panel for PropertyGroupContainer
 * 
 * @author sriram
 *
 */
public class PropertyGroupContainerEditorPanel extends ContentPanel {
  
    /**
     * create an instance of PropertyGroupContainerEditorPanel
     * 
     * @param container
     */
    public PropertyGroupContainerEditorPanel(PropertyGroupContainer container) {
        init();
    }

    private void init() {
        setHeaderVisible(false);
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$
        setLayout(new FitLayout());
    }

    private LayoutContainer buildMessagePanel() {
        LayoutContainer ret = new LayoutContainer();
        ret.setStyleAttribute("background-color", "#EDEDED"); //$NON-NLS-1$ //$NON-NLS-2$
        ret.setHeight("100%"); //$NON-NLS-1$

        HBoxLayout layout = new HBoxLayout();
        layout.setPadding(new Padding(5));
        layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        layout.setPack(BoxLayoutPack.CENTER);
        ret.setLayout(layout);

        HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 0, 0, 0));

        ret.add(new Label(I18N.DISPLAY.selectParameter()), layoutData);

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
