package org.iplantc.core.tito.client.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.services.DeployedComponentSearchServiceFacade;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.tito.client.utils.DeployedComponentSearchUtil;
import org.iplantc.core.tito.client.utils.DeployedComponentSorter;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.images.Resources;
import org.iplantc.core.uicommons.client.models.DeployedComponent;
import org.iplantc.core.uicommons.client.models.JsDeployedComponent;


import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A dialog to look up deployed components
 * 
 * @author sriram
 * 
 */
public class DCLookUpDialog extends Dialog {

    private static final String ID_SEARCH_FLD = "idSearchFld";
    private static final String ID_SEARCH_BTN = "idSearchBtn";
    private Grid<DeployedComponent> grid;
    private RowExpander expander;
    private TextField<String> filter;
    private String currentSelection;


    public DCLookUpDialog(SelectionListener<ButtonEvent> DialogOkBtnSelectionListenerImpl,
            String currentSelection) {

        setTopComponent(buildToolBar());
        setButtons(Dialog.OKCANCEL);

        Button bntOk = setOkButtonListener(DialogOkBtnSelectionListenerImpl);

        initDialog(bntOk);

        getDeployedComponents(currentSelection);
        add(grid);
        setScrollMode(Scroll.AUTOY);
    }

    private void initDialog(Button bntOk) {
        setHideOnButtonClick(true);
        setHeading(I18N.DISPLAY.component());
        setLayout(new FitLayout());
        setSize(600, 500);
        setResizable(false);
        setModal(true);
        buildcomponentLookupGrid();
        initGridSelectionModel(bntOk);
    }

    private Button setOkButtonListener(SelectionListener<ButtonEvent> DialogOkBtnSelectionListenerImpl) {
        Button bntOk = getButtonById(Dialog.OK);
        bntOk.addSelectionListener(DialogOkBtnSelectionListenerImpl);
        bntOk.disable();
        return bntOk;
    }

    private ToolBar buildToolBar() {
        ToolBar tool = new ToolBar();
        tool.add(buildFilterField());
        tool.add(buildSearchButton());
        return tool;
    }

    private Button buildSearchButton() {
        Button searchBtn = new Button();
        searchBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                search(filter.getValue());
            }
        });

        searchBtn.setId(ID_SEARCH_BTN);
        searchBtn.setIcon(AbstractImagePrototype.create(Resources.ICONS.search()));
        return searchBtn;
    }

    private TextField<String> buildFilterField() {
        filter = new TextField<String>() {
            @Override
            public void onKeyUp(FieldEvent fe) {
                String val = getValue();
                if (val == null || val.isEmpty()) {
                    search("");
                    return;
                }

                if (fe.getKeyCode() == 13) {
                    if (val.length() >= 3) {
                        search(val);
                    }
                }
            }
        };

        filter.setEmptyText(I18N.DISPLAY.searchToolTip());
        filter.setToolTip(I18N.DISPLAY.searchToolTip());
        filter.setId(ID_SEARCH_FLD);
        filter.setWidth(350);
        return filter;
    }

    private void initGridSelectionModel(final Button btnOk) {
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnOk.enable();
            }
        });
    }

    private ColumnConfig buildColumnConfig(final String key, final String caption, int width) {
        ColumnConfig ret = new ColumnConfig(key, caption, width);

        return ret;
    }

    private ColumnModel buildColumnModel() {
        initExpander();
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();


        ColumnConfig name = buildColumnConfig(DeployedComponent.NAME, I18N.DISPLAY.name(), 225);
        name.setMenuDisabled(true);
        name.setSortable(true);

        ColumnConfig version = buildColumnConfig(DeployedComponent.VERSION,
                I18N.DISPLAY.versionColumnHeader(), 100);
        version.setSortable(false);
        version.setMenuDisabled(true);

        ColumnConfig loc = buildColumnConfig(DeployedComponent.LOCATION, I18N.DISPLAY.path(), 250);
        loc.setMenuDisabled(true);
        loc.setSortable(false);

        configs.add(expander);
        configs.add(name);
        configs.add(version);
        configs.add(loc);

        return new ColumnModel(configs);
    }

    private String buildDCDeatailsTemplate() {
        StringBuilder tmpl = new StringBuilder();
        // Description line
        tmpl.append("<tpl if=\"description\"><p>{description}</p></tpl>"); //$NON-NLS-1$
        // Contact line showing the integrator's email
        tmpl.append("<p><b>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.attribution());
        tmpl.append(":</b> "); //$NON-NLS-1$
        tmpl.append("<tpl if=\"attribution\"><p>{attribution}</p></tpl>"); //$NON-NLS-1$
        tmpl.append("<br/>"); //$NON-NLS-1$

        return tmpl.toString();
    }

    private void initExpander() {
        XTemplate tpl = XTemplate.create(buildDCDeatailsTemplate());

        expander = new RowExpander();
        expander.setTemplate(tpl);
    }

    private void buildcomponentLookupGrid() {
        ListStore<DeployedComponent> store = new ListStore<DeployedComponent>();
        grid = new Grid<DeployedComponent>(store, buildColumnModel());
        grid.setAutoExpandColumn("name"); //$NON-NLS-1$
        grid.addPlugin(expander);
        grid.getView().setEmptyText(I18N.DISPLAY.noComponents());
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.getStore().setStoreSorter(new DeployedComponentSorter());
    }

    private void getDeployedComponents(final String currentSelection) {
        grid.mask(I18N.DISPLAY.loadingMask());
        EnumerationServices services = new EnumerationServices();
        services.getDeployedComponents(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                grid.getStore().removeAll();
                ArrayList<DeployedComponent> components = DeployedComponentSearchUtil.parseJson(result);
                grid.getStore().add(components);
                grid.getStore().sort(DeployedComponent.NAME, SortDir.ASC);
                setCurrentCompSelection(currentSelection);
                grid.unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadDeployedComponents(), caught);
                grid.unmask();
            }
        });
    }

    public void setCurrentCompSelection(String curr) {
        this.currentSelection = curr;
        if (currentSelection != null && !currentSelection.equals("")) { //$NON-NLS-1$
            List<DeployedComponent> comps = grid.getStore().getModels();
            for (DeployedComponent dc : comps) {
                if (dc.getId().equals(currentSelection)) {
                    grid.getSelectionModel().select(false, dc);
                    break;
                }
            }
        }
    }

    public DeployedComponent getSelectedItem() {
        return grid.getSelectionModel().getSelectedItem();
    }

    private void search(String filter) {
        if (filter != null && !filter.isEmpty()) {
            if (filter.length() >= 3) {
                grid.mask(I18N.DISPLAY.loadingMask());
                DeployedComponentSearchServiceFacade facade = new DeployedComponentSearchServiceFacade();
                facade.searchDeployedComponents(filter, new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                        grid.unmask();
                    }

                    @Override
                    public void onSuccess(String result) {
                        List<DeployedComponent> dc_list = DeployedComponentSearchUtil.parseJson(result);
                        grid.getStore().removeAll();
                        grid.getStore().add(dc_list);
                        grid.unmask();
                    }
                });
            }
        } else {
            getDeployedComponents(currentSelection);
        }
    }
}
