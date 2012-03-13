package org.iplantc.core.tito.client.dialogs;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;

public class AccordionChildPanel extends ContentPanel {
    /**
     * Constructs an instance of a panel given a unique identifier.
     * 
     * @param id unique id for this panel.
     */
    public AccordionChildPanel(String id) {
        setId(id);
        setAutoHeight(true);
        setBodyStyleName("accordianbody"); //$NON-NLS-1$

        getHeader().setStyleName("accordianTitle"); //$NON-NLS-1$

        setBodyBorder(true);
    }
}
