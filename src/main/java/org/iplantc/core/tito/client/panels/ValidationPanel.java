package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.command.RuleCommand;
import org.iplantc.core.tito.client.dialogs.validation.ValidationEditDialog;
import org.iplantc.core.tito.client.models.RuleContainer;
import org.iplantc.core.tito.client.utils.PanelHelper;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;

public class ValidationPanel extends VerticalPanel {
    final private Property property;
    private PropertyTypeCategory category;

    private Grid<RuleContainer> gridRules;

    private Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;

    public ValidationPanel(final Property property) {
        this.property = property;

        setLayout(new FitLayout());
        buildAddButton();
        buildEditButton();
        buildDeleteButton();
        buildGrid();
    }

    private void buildEditButton() {
        btnEdit = PanelHelper.buildButton("idRuleEditBtn", I18N.DISPLAY.edit(), //$NON-NLS-1$
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        promptForEditRule();
                    }
                });

        btnEdit.disable();
    }

    private void buildDeleteButton() {
        btnDelete = PanelHelper.buildButton("idRuleDeleteBtn", I18N.DISPLAY.delete(), //$NON-NLS-1$
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        doDelete();
                    }
                });

        btnDelete.disable();
    }

    private ColumnConfig buildColumn(String id, String header, int width) {
        ColumnConfig ret = new ColumnConfig(id, header, width);
        ret.setMenuDisabled(true);

        return ret;
    }

    private ColumnModel buildColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(buildColumn(RuleContainer.CAPTION, I18N.DISPLAY.validationRules(), 420));

        return new ColumnModel(columns);
    }

    private void buildGrid() {
        gridRules = new Grid<RuleContainer>(new ListStore<RuleContainer>(), buildColumnModel());
        gridRules.setAutoExpandColumn(RuleContainer.CAPTION);
        gridRules.setHeight(120);
        gridRules.setStripeRows(true);
        gridRules.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gridRules.getView().setEmptyText(I18N.DISPLAY.noRulesToDisplay());

        gridRules.getSelectionModel().addSelectionChangedListener(
                new SelectionChangedListener<RuleContainer>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<RuleContainer> se) {
                        updateButtonVisibility();
                    }
                });

        // populate grid
        MetaDataValidator validator = property.getValidator();
        if (validator != null) {
            for (MetaDataRule rule : validator.getRules()) {
                RuleContainer ruleContainer = new RuleContainer(rule);
                gridRules.getStore().add(ruleContainer);
            }
        }
    }

    private void updateButtonVisibility() {
        boolean empty = gridRules.getSelectionModel().getSelection().isEmpty();
        btnDelete.setEnabled(!empty);
        btnEdit.setEnabled(!empty);
    }

   
    private void buildAddButton() {
        btnAdd = PanelHelper.buildButton("idRuleAddBtn", I18N.DISPLAY.add(), //$NON-NLS-1$
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        promptForNewRule();
                    }
                });
    }

    private ToolBar buildToolbar() {
        ToolBar ret = new ToolBar();

        ret.setWidth(getWidth());

        ret.add(btnAdd);
        ret.add(btnEdit);

        ret.add(new FillToolItem());

        ret.add(btnDelete);

        return ret;
    }

    private void promptForEditRule() {
        // TODO cache dialog?
        MetaDataRule selectedRule = gridRules.getSelectionModel().getSelectedItem().getRule();

        ValidationEditDialog dlg = new ValidationEditDialog(selectedRule, category,
                !containsIntBetweenRule(), new RuleCommand() {
                    @Override
                    public void execute(MetaDataRule rule) {
                        ListStore<RuleContainer> store = gridRules.getStore();
                        for (RuleContainer container : store.getModels()) {
                            if (container.getRule() == rule) {
                                container.setCaption();
                                store.update(container);
                            }
                        }
                    }
                });

        dlg.show();
    }

    private boolean isValidValidator(final MetaDataValidator validator) {
        boolean ret = true; // assume success

        // null validators are still valid
        if (validator != null) {
            if (!validator.isRequired()) {
                if (validator.getNumRules() == 0) {
                    // we are only invalid if we exist, aren't required and have no rules
                    ret = false;
                }
            }
        }

        return ret;
    }

    private void validateValidator(final MetaDataValidator validator) {
        // do we need to reset our validator - if we do not preview will fail
        if (!isValidValidator(validator)) {
            property.setValidator(null);
        }
    }

    private void doDelete() {
        MetaDataValidator validator = property.getValidator();

        GridSelectionModel<RuleContainer> selModel = gridRules.getSelectionModel();
        List<RuleContainer> items = selModel.getSelectedItems();

        // delete all selected rows in ascending order, keep track of the last deleted row
        int last = -1;
        for (RuleContainer container : items) {
            MetaDataRule rule = container.getRule();
            last = gridRules.getStore().indexOf(container);
            validator.removeRule(rule);

            gridRules.getStore().remove(container);
        }

        // update selection
        int count = gridRules.getStore().getCount();
        if (last >= count) {
            last = count - 1;
        }

        selModel.select(last, false);

        validateValidator(validator);
    }

    private void promptForNewRule() {
        // TODO cache dialog?
        new ValidationEditDialog(null, category, !containsIntBetweenRule(),
                new RuleCommand() {
                    @Override
                    public void execute(MetaDataRule rule) {
                        addRule(rule);
                    }
                }).show();
    }

    private boolean containsIntBetweenRule() {
        for (int i = 0; i < gridRules.getStore().getCount(); i++) {
            RuleContainer c = gridRules.getStore().getAt(i);
            String type = c.getRule().getType();
            if ("IntRange".equals(type)) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    private void addRule(MetaDataRule rule) {
        RuleContainer ruleContainer = new RuleContainer(rule);
        gridRules.getStore().add(ruleContainer);

        // add rule to validator
        MetaDataValidator validator = property.getValidator();
        if (validator != null) {
            validator.addRule(rule);
        } else {
            validator = new MetaDataValidator();
            validator.addRule(rule);
            property.setValidator(validator);
        }
    }

    private ContentPanel buildGridPanel() {
        ContentPanel ret = new ContentPanel();
        ret.setWidth(480);
        ret.setHeaderVisible(false);

        ret.setTopComponent(buildToolbar());

        ret.add(gridRules);

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        add(buildGridPanel());
    }

    public void reset(final PropertyTypeCategory category) {
        // if our category has not been set... this is the first pass and we do not want
        // to reset the validator
        if (this.category != null) {

            gridRules.getStore().removeAll();

            property.setValidator(null);
        }

        this.category = category;
    }
    
    /** Overridden to fix a problem where disabling this panel would disable the whole PropertyEditorPanel. */
    @Override
    public void disable() {
        btnAdd.disable();
        btnEdit.disable();
        btnDelete.disable();
        gridRules.disable();
    }
    
    /** see disable() */
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            super.setEnabled(true);
        }
        else {
            disable();
        }
    }
}
