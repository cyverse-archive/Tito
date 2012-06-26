/**
 * 
 */
package org.iplantc.core.tito.client.dialogs.validation;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.utils.PanelHelper;
import org.iplantc.core.tito.client.widgets.BoundedNumberField;
import org.iplantc.core.tito.client.widgets.validation.PropertyChangeListener;
import org.iplantc.core.tito.client.widgets.validation.PropertyEditor;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Command;

/**
 *
 * Basic editable grid to build a string List Items or number list items
 * 
 * @author sriram
 * 
 */
public abstract class BasicEditableList extends ContentPanel implements PropertyEditor<JSONArray> {
    private Button btnDelete;
    protected EditorGrid<GridRow> grid;
    protected Button btnAdd;
    private final Command updateCmd;

    protected static final String NEW_NAME = "New parameter";
    protected static final String NEW_VALUE = ""; //$NON-NLS-1$

    /**
     * Create a new instance of BasicEditableList
     * 
     * @param values to be loaded into the list
     * @param updateCmd call back command object thats needs to be invoked when the list is updated
     */
    public BasicEditableList(JSONArray values, Command updateCmd) {
        buildPanel();
        setToolBar();
        setAddSelectionListener();
        compose();
        this.updateCmd = updateCmd;

        setValue(values);
    }

    private void buildPanel() {
        setWidth(520);
        setLayout(new FitLayout());
        setHeaderVisible(true);
        setHeading(I18N.DISPLAY.listBoxFormatNotice());
      
    }

    protected void setToolBar() {
        setTopComponent(buildToolbar());;
    }
    
    private void compose() {
        add(buildGrid());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.js.integrate.client.widgets.validation.PropertyEditor#validate()
     */
    @Override
    public boolean validate() {
        // TODO implement me!!!
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.js.integrate.client.widgets.validation.PropertyEditor#getEditorComponent ()
     */
    @Override
    public Component getEditorComponent() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.js.integrate.client.widgets.validation.PropertyEditor#addChangeListener
     * (org.iplantc.js.integrate.client.widgets.validation.PropertyChangeListener)
     */
    @Override
    public void addChangeListener(PropertyChangeListener<JSONArray> listener) {
        // TODO Auto-generated method stub

    }

    /**
     * build a CellEditor to edit argument name strings.
     * 
     * @return a string cell editor
     */
    protected CellEditor buildNameCellEditor() {
        BoundedTextField<String> field = buildBoundedTextField(false);

        IPlantValidator.setRegexRestrictedCmdLineChars(field, ""); //$NON-NLS-1$

        return new CellEditor(field);
    }

    /**
     * build a CellEditor to edit argument value strings.
     * 
     * @return a string cell editor
     */
    protected CellEditor buildValueCellEditor() {
        BoundedTextField<String> field = buildBoundedTextField(true);

        IPlantValidator.setRegexRestrictedArgValueChars(field, ""); //$NON-NLS-1$

        return new CellEditor(field);
    }

    /**
     * build a CellEditor to edit string items.
     * 
     * @return a string cell editor
     */
    protected CellEditor buildStringCellEditor() {
        return new CellEditor(buildBoundedTextField(false));
    }

    protected BoundedTextField<String> buildBoundedTextField(boolean allowBlank) {
        BoundedTextField<String> field = new BoundedTextField<String>();

        field.setAllowBlank(allowBlank);
        field.setMaxLength(64);
        field.setSelectOnFocus(true);

        return field;
    }

    /**
     * build a CellEditor to edit number
     * 
     * @return a number cell editor
     */
    protected CellEditor buildNumberCellEditor() {
        final BoundedNumberField field = new BoundedNumberField();

        field.setAllowBlank(false);
        field.setSelectOnFocus(true);
        field.setMaxLength(10);

        return new CellEditor(field) {
            /**
             * {@inheritDoc}
             * 
             * Sets the property editor type in the underlying field to an Integer type if the changed
             * value contains a number formatted as an integer, otherwise it's set to a Double. This will
             * preserve integer values entered by the user, instead of always converting them to double
             * values.
             * 
             * @see com.extjs.gxt.ui.client.widget.Editor#completeEdit()
             */
            @Override
            public void completeEdit() {
                String value = field.getRawValue();

                try {
                    Integer.parseInt(value);
                    field.setPropertyEditorType(Integer.class);
                } catch (Exception e) {
                    // The string didn't parse as an Integer
                    // so use a Double editor type
                    field.setPropertyEditorType(Double.class);
                }

                super.completeEdit();
            }
        };
    }

    @SuppressWarnings("serial")
    protected class GridRow extends BaseModelData {
        public static final String STRING_NAME = "name"; //$NON-NLS-1$
        public static final String STRING_VALUE = "stringValue"; //$NON-NLS-1$
        public static final String STRING_ITEM = "stringItem"; //$NON-NLS-1$
        public static final String NUMBER_VALUE = "numberValue"; //$NON-NLS-1$
        public static final String NUMBER_ITEM = "numberItem"; //$NON-NLS-1$
        public static final String DEFAULT = "default"; //$NON-NLS-1$
        public static final String BOOLEAN = "boolean"; //$NON-NLS-1$

        GridRow(String item, String name, String value, Boolean isDefault) {
            set(STRING_ITEM, item);
            set(STRING_NAME, name);
            set(STRING_VALUE, value);
            set(DEFAULT, isDefault);
        }

        GridRow(Number item, String name, String value, Boolean isDefault) {
            set(NUMBER_ITEM, item);
            set(STRING_NAME, name);
            set(STRING_VALUE, value);
            set(DEFAULT, isDefault);
        }

        String getName() {
            return get(STRING_NAME);
        }
        
        GridRow(Boolean booleanValue, String item, String value) {
            set(BOOLEAN, booleanValue);
            set(STRING_ITEM, item);
            set(STRING_VALUE, value);
        }

        String getStringValue() {
            String ret = get(STRING_VALUE);

            if (ret == null) {
                return ""; //$NON-NLS-1$
            }

            return ret;
        }

        String getStringItem() {
            return get(STRING_ITEM);
        }

        Number getNumberItem() {
            return get(NUMBER_ITEM);
        }

        Boolean isDefault() {
            return get(DEFAULT);
        }
        
        String getBooleanValue() {
            return get(BOOLEAN).toString();
        }

        void setDefault(boolean b) {
            set(DEFAULT, b);
        }

    }

    /**
     * Build columns for the grid display
     * @return
     */
    protected abstract List<ColumnConfig> buildColumns();

    private ColumnModel buildColumnModel(CheckBoxSelectionModel<GridRow> checkboxModel) {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig colCheckBox = checkboxModel.getColumn();
        colCheckBox.setAlignment(HorizontalAlignment.CENTER);
        columns.add(colCheckBox);

        columns.addAll(buildColumns());

        return new ColumnModel(columns);
    }

    private Grid<GridRow> buildGrid() {
        ListStore<GridRow> store = new ListStore<GridRow>();

        CheckBoxSelectionModel<GridRow> checkboxModel = buildCheckboxModel();

        grid = new EditorGrid<GridRow>(store, buildColumnModel(checkboxModel));
        grid.addPlugin(checkboxModel);
        grid.setSelectionModel(checkboxModel);

        grid.setHeight(120);
        grid.getView().setEmptyText(I18N.DISPLAY.noItemsToDisplay());
        grid.getView().setShowDirtyCells(false);
        grid.getView().setForceFit(true);

        grid.addListener(Events.AfterEdit, new GridEditListener());

        return grid;
    }

    private void buildDeleteButton() {
        btnDelete = PanelHelper.buildButton("idRuleDeleteBtn", I18N.DISPLAY.delete(), //$NON-NLS-1$
                new DeleteSelectionListener());

        btnDelete.disable();
    }

    private void buildNewButton() {
        btnAdd = PanelHelper.buildButton("idRuleAddBtn", I18N.DISPLAY.add(), null); //$NON-NLS-1$
    }

    private ToolBar buildToolbar() {
        ToolBar toolBar = new ToolBar();

        buildNewButton();
        buildDeleteButton();

        toolBar.add(btnAdd);

        toolBar.add(new FillToolItem());
        toolBar.add(btnDelete);

        return toolBar;
    }

    private CheckBoxSelectionModel<GridRow> buildCheckboxModel() {
        CheckBoxSelectionModel<GridRow> ret = new CheckBoxSelectionModel<GridRow>();
        ret.setSelectionMode(SelectionMode.SIMPLE);

        ret.addSelectionChangedListener(new SelectionChangedListener<GridRow>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<GridRow> se) {
                btnDelete.setEnabled(!se.getSelection().isEmpty());
            }
        });

        return ret;
    }

    /**
     * Selection listener for delete button
     * 
     * @author sriram
     *
     */
    protected class DeleteSelectionListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            List<GridRow> rows = grid.getSelectionModel().getSelectedItems();
            ListStore<GridRow> ls = grid.getStore();
            if (rows != null) {
                for (GridRow r : rows) {
                    ls.remove(r);

                    if (updateCmd != null) {
                        updateCmd.execute();
                    }
                }
            }
        }

    }

    /**
     * Selection listener for add button
     * 
     * @author sriram
     */
    protected abstract void setAddSelectionListener();

    /**
     * Grid's edit listener
     * 
     * @author sriram
     *
     */
    protected class GridEditListener implements Listener<GridEvent<GridRow>> {

        @Override
        public void handleEvent(GridEvent<GridRow> be) {
            if (updateCmd != null) {
                updateCmd.execute();
            }

        }

    }

    /**
     * 
     * A class to render check boxes in a grid column for selecting default values
     * 
     * @author sriram
     *
     */
    protected class DefaultValueCheckBoxCellRenderer implements GridCellRenderer<GridRow> {
        RadioGroup grp = new RadioGroup();

        @Override
        public Object render(final GridRow model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<GridRow> store, final Grid<GridRow> grid) {
            final Radio r = new Radio();
            grp.add(r);
            r.setValue(model.isDefault());
            r.addListener(Events.Change, new Listener<FieldEvent>() {

                @Override
                public void handleEvent(FieldEvent be) {
                    model.setDefault(r.getValue());
                    if (updateCmd != null) {
                        updateCmd.execute();
                    }
                }
            });
            return r;
        }
    }

}
