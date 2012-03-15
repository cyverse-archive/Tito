package org.iplantc.core.tito.client;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.tito.client.events.AfterTemplateLoadEvent;
import org.iplantc.core.tito.client.events.AfterTemplateLoadEventHandler;
import org.iplantc.core.tito.client.events.NavigateToHomeEvent;
import org.iplantc.core.tito.client.events.NavigateToHomeEventHandler;
import org.iplantc.core.tito.client.events.NewProjectEvent;
import org.iplantc.core.tito.client.events.TemplateLoadEvent;
import org.iplantc.core.tito.client.events.TemplateLoadEvent.MODE;
import org.iplantc.core.tito.client.panels.TemplatesListingGridPanel;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window.Location;

/**
 * Defines the overall layout for the root panel of the web application.
 * 
 * @author sriram
 */
public class ApplicationLayout extends Viewport {
    private Component center;

    private final FlowLayout layout;

    /**
     * Default constructor.
     */
    public ApplicationLayout() {
        // build top level layout
        layout = new FlowLayout(0);

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            @Override
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        setLayout(layout);
       // setStyleName("iplantc_background"); //$NON-NLS-1$

        addListeners();
    }

    private void addListeners() {
        EventBus instance = EventBus.getInstance();
        instance.addHandler(NavigateToHomeEvent.TYPE, new NavigateToHomeEventHandler() {
            @Override
            public void onHome() {
                addListOfTools();
            }

        });
    }

    private void addListOfTools() {
        Command addButtonCommand = new NewTemplateSelectionListenerImpl();
        replaceCenterPanel(new TemplatesListingGridPanel(addButtonCommand));
    }



    /**
     * Replace the contents of the center panel.
     * 
     * @param view a new component to set in the center of the BorderLayout.
     */
    public void replaceCenterPanel(Component view) {
        if (center != null) {
            remove(center);
        }

        center = view;

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
        data.setMargins(new Margins(0));

        if (center != null) {
            add(center, data);
        }

        if (isRendered()) {
            layout();
        }
    }

    public void reset() {
        // clear our center
        if (center != null) {
            remove(center);
        }

        center = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        showTools();
    }

    /**
     * Loads a tool if the request string contains a tool ID; otherwise, a the list of available tools is
     * displayed.
     */
    private void showTools() {
        String toolId = getToolId();
        if (toolId != null) {
            if (toolId.isEmpty()) {
                fireNewProjectEvent();
            } else {
                loadTool(toolId);
    
                // if AfterTemplateLoadEvent says "failed", addListOfTools().
                // regList is a list whose only element is reg; this avoids having to make reg an instance variable
                final List<HandlerRegistration> regList = new ArrayList<HandlerRegistration>();
                HandlerRegistration reg = EventBus.getInstance().addHandler(AfterTemplateLoadEvent.TYPE,
                        new AfterTemplateLoadEventHandler() {
                            @Override
                            public void onLoad(AfterTemplateLoadEvent event) {
                                if (!event.isSuccessful()) {
                                    MessageBox.alert(org.iplantc.core.uicommons.client.I18N.ERROR.error(), I18N.DISPLAY.cantLoadTemplate(), null);
                                    addListOfTools();
                                }
                                regList.get(0).removeHandler();
                            }
                        });
                regList.add(reg);
            }
        } else {
            addListOfTools();
        }
    }

    private void fireNewProjectEvent() {
        EventBus eventbus = EventBus.getInstance();
        NewProjectEvent event = new NewProjectEvent(NewProjectEvent.ProjectType.Tool);
        eventbus.fireEvent(event);
    }

    /**
     * Returns the tool ID from the request string
     * 
     * @return a string containing a tool ID, or null if the request does not contain one
     */
    private String getToolId() {
        String paramName = Constants.CLIENT.titoId();
        return Location.getParameter(paramName);
    }

    /**
     * Loads a tool with a given ID into the application.
     * 
     * @param id
     */
    private void loadTool(String id) {
        EventBus.getInstance().fireEvent(new TemplateLoadEvent(id, MODE.EDIT));
    }

   
    private class NewTemplateSelectionListenerImpl extends SelectionListener<MenuEvent> implements
            Command {
        @Override
        public void componentSelected(MenuEvent ce) {
            execute();
        }

        @Override
        public void execute() {
            fireNewProjectEvent();
        }
    }
}
