package org.iplantc.core.tito.client;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.MenuHyperlink;
import org.iplantc.core.client.widgets.MenuLabel;
import org.iplantc.core.tito.client.dialogs.AboutApplicationDialog;
import org.iplantc.core.tito.client.events.AfterTemplateLoadEvent;
import org.iplantc.core.tito.client.events.AfterTemplateLoadEventHandler;
import org.iplantc.core.tito.client.events.NavigateToHomeEvent;
import org.iplantc.core.tito.client.events.NavigateToHomeEventHandler;
import org.iplantc.core.tito.client.events.NewProjectEvent;
import org.iplantc.core.tito.client.events.TemplateLoadEvent;
import org.iplantc.core.tito.client.events.TemplateLoadEvent.MODE;
import org.iplantc.core.tito.client.panels.TemplatesListingGridPanel;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.util.WindowUtil;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Image;

/**
 * Defines the overall layout for the root panel of the web application.
 * 
 * @author sriram
 */
public class ApplicationLayout extends Viewport {
    private ContentPanel north;
    private Component center;

    private final BorderLayout layout;

    /**
     * Default constructor.
     */
    public ApplicationLayout() {
        // build top level layout
        layout = new BorderLayout();

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            @Override
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        setLayout(layout);
        setStyleName("iplantc_background"); //$NON-NLS-1$

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
        CenterLayout layoutHorizontalCenter = new CenterLayout();
        layoutHorizontalCenter.setExtraStyle("iplantc_center_layout_horizontal"); //$NON-NLS-1$

        LayoutContainer container = new LayoutContainer(layoutHorizontalCenter);
        container.setStyleName("iplantc_portal_component"); //$NON-NLS-1$

        Command addButtonCommand = new NewTemplateSelectionListenerImpl();

        container.add(new TemplatesListingGridPanel(addButtonCommand));

        replaceCenterPanel(container);
    }

    private void drawNorth() {
        north = new ContentPanel();
        north.setHeaderVisible(false);
        north.setBodyBorder(false);
        north.setBorders(false);
        north.setBodyStyleName("iplantc_portal_component"); //$NON-NLS-1$

        north.add(new HeaderPanel());

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 90);
        data.setCollapsible(false);
        data.setFloatable(false);
        data.setHideCollapseTool(true);
        data.setSplit(false);
        data.setMargins(new Margins(0, 0, 0, 0));

        add(north, data);
    }

    private void drawSouth() {
        HorizontalPanel south = new HorizontalPanel();

        Html copyright = new Html(I18N.DISPLAY.projectCopyrightStatement());
        copyright.setStyleName("copyright"); //$NON-NLS-1$

        Html nsftext = new Html(I18N.DISPLAY.nsfProjectText());
        nsftext.setStyleName("nsf_text"); //$NON-NLS-1$

        south.add(copyright);
        south.add(nsftext);

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.SOUTH, 20);
        data.setCollapsible(false);
        data.setFloatable(false);
        data.setHideCollapseTool(true);
        data.setSplit(false);
        data.setMargins(new Margins(0, 0, 0, 0));

        add(south, data);
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

        drawNorth();
        drawSouth();

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

    private class HeaderPanel extends HorizontalPanel {
        public HeaderPanel() {
            setBorders(false);

            add(buildLogoPanel());
            add(buildActionsPanel());
            setStyleName("iplantc_portal_component"); //$NON-NLS-1$
        }

        private VerticalPanel buildLogoPanel() {
            VerticalPanel panel = new VerticalPanel();

            Image logo = new Image(Constants.CLIENT.iplantLogo());
            logo.addStyleName("iplantc_logo"); //$NON-NLS-1$
            logo.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent arg0) {
                    com.google.gwt.user.client.Window.Location.assign(Constants.CLIENT.iplantHome());
                }
            });
            panel.add(logo);

            return panel;
        }

        private HorizontalPanel buildActionsPanel() {
            HorizontalPanel pnlActions = new HorizontalPanel();
            pnlActions.setStyleName("iplantc_header_actions"); //$NON-NLS-1$
            pnlActions.setSpacing(5);

            pnlActions.add(buildActionsMenu(UserInfo.getInstance().getUsername(), buildUserMenu()));
            pnlActions.add(buildActionsMenu(I18N.DISPLAY.help(), buildHelpMenu()));

            return pnlActions;
        }

        private Menu buildUserMenu() {
            Menu userMenu = buildMenu();

            userMenu.add(new MenuHyperlink(I18N.DISPLAY.logout(),
                    "iplantc_hyperlink", "iplantc_header_hyperlink_hover", new LogoutSelectionListener(), I18N.DISPLAY.logoutToolTipText())); //$NON-NLS-1$ //$NON-NLS-2$

            return userMenu;
        }

        private Menu buildHelpMenu() {
            Menu helpMenu = buildMenu();
            String styleName = "iplantc_hyperlink"; //$NON-NLS-1$
            String hoverStyle = "iplantc_header_hyperlink_hover"; //$NON-NLS-1$

            helpMenu.add(new MenuHyperlink(I18N.DISPLAY.documentation(), styleName, hoverStyle,
                    new DocumentationSelectionListener(), "")); //$NON-NLS-1$
            helpMenu.add(new MenuHyperlink(I18N.DISPLAY.forums(), styleName, hoverStyle,
                    new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    WindowUtil.open(Constants.CLIENT.forumsUrl());
                }
                    }, "")); //$NON-NLS-1$

            helpMenu.add(new MenuHyperlink(I18N.DISPLAY.contactSupport(), styleName, hoverStyle,
                    new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    WindowUtil.open(Constants.CLIENT.supportUrl());
                }
                    }, "")); //$NON-NLS-1$
            helpMenu.add(new MenuHyperlink(I18N.DISPLAY.about(), styleName, hoverStyle,
                    new AboutSelectionListener(), "")); //$NON-NLS-1$

            return helpMenu;
        }

        private HorizontalPanel buildActionsMenu(String menuHeaderText, final Menu menu) {
            final HorizontalPanel ret = new HorizontalPanel();
            ret.setStyleName("iplantc_header_menu_panel"); //$NON-NLS-1$

            // build menu header text and icon
            MenuLabel menuHeader = new MenuLabel(menuHeaderText,
                    "iplantc_header_menu_label", "iplantc_header_menu_label_hover"); //$NON-NLS-1$//$NON-NLS-2$
            menuHeader.addListener(Events.OnClick, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    showHeaderActionsMenu(ret, menu);
                }
            });

            IconButton icon = new IconButton("iplantc_header_menu_button", //$NON-NLS-1$
                    new SelectionListener<IconButtonEvent>() {
                        @Override
                        public void componentSelected(IconButtonEvent ce) {
                            showHeaderActionsMenu(ret, menu);
                        }
                    });

            ret.add(menuHeader);
            ret.add(icon);

            // update header style when menu is shown
            menu.addListener(Events.Show, new Listener<MenuEvent>() {
                @Override
                public void handleEvent(MenuEvent be) {
                    ret.addStyleName("iplantc_header_menu_selected"); //$NON-NLS-1$
                }
            });

            menu.addListener(Events.Hide, new Listener<MenuEvent>() {
                @Override
                public void handleEvent(MenuEvent be) {
                    ret.removeStyleName("iplantc_header_menu_selected"); //$NON-NLS-1$
                }
            });

            return ret;
        }

        private Menu buildMenu() {
            Menu menu = new Menu();

            menu.setSize(110, 90);
            menu.setBorders(true);
            menu.setStyleName("iplantc_header_menu_body"); //$NON-NLS-1$

            return menu;
        }

        private void showHeaderActionsMenu(HorizontalPanel anchor, Menu actionsMenu) {
            // show the menu so that its right edge is aligned with with the anchor's right edge,
            // and its top is aligned with the anchor's bottom.
            actionsMenu.showAt(anchor.getAbsoluteLeft() + anchor.getWidth() - 110,
                    anchor.getAbsoluteTop() + anchor.getHeight());
        }
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

    private class DocumentationSelectionListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            WindowUtil.open(Constants.CLIENT.titoHelpFile());
        }
    }

    private class AboutSelectionListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            AboutApplicationDialog dialog = new AboutApplicationDialog();
            dialog.setButtons(Dialog.OK);
            dialog.setHideOnButtonClick(true);
            dialog.show();
        }
    }

    private class LogoutSelectionListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            com.google.gwt.user.client.Window.Location.assign(
                    com.google.gwt.user.client.Window.Location.getPath()
                    + Constants.CLIENT.logoutUrl());

        }
    }

}
