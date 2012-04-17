package org.iplantc.core.tito.client.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.models.DeployedComponent;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.tito.client.utils.DeployedComponentSearchUtil;
import org.iplantc.core.tito.client.utils.DeployedComponentSorter;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * A dialog to look up deployed components
 * 
 * @author sriram
 * 
 */
public class DCLookUpDialog extends Dialog {

    private Grid<DeployedComponent> grid;
    private RowExpander expander;


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
        DeployedComponentSearchUtil util = new DeployedComponentSearchUtil();
        tool.add(util.buildSearchField());
        return tool;
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
        EnumerationServices services = new EnumerationServices();
        services.getDeployedComponents(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ArrayList<DeployedComponent> components = DeployedComponentSearchUtil.parseJson(result);
                grid.getStore().add(components);
                grid.getStore().sort(DeployedComponent.NAME, SortDir.ASC);
                setCurrentCompSelection(currentSelection);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadDeployedComponents(), caught);
            }
        });
    }

    public void setCurrentCompSelection(String currentSelction) {
        List<DeployedComponent> comps = grid.getStore().getModels();

        if (currentSelction != null && !currentSelction.equals("")) { //$NON-NLS-1$
            for (DeployedComponent dc : comps) {
                if (dc.getId().equals(currentSelction)) {
                    grid.getSelectionModel().select(false, dc);
                    break;
                }
            }
        }
    }

    public DeployedComponent getSelectedItem() {
        return grid.getSelectionModel().getSelectedItem();
    }
}
