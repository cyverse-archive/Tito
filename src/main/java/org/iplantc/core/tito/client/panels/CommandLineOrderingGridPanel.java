package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyData;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.events.CommandLineArgumentChangeEvent;
import org.iplantc.core.tito.client.utils.PropertyUtil;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;

/**
 * Grid for adjusting the command line ordering of Parameters and Input/Output files.
 * 
 * @author psarando
 */
public class CommandLineOrderingGridPanel extends ContentPanel {
    /**
     * Grid containing all parameters with a command line order.
     */
    private Grid<PropertyData> orderedGrid;

    /**
     * Grid containing all parameters without a command line order (-1).
     */
    private Grid<PropertyData> unorderedGrid;

    /**
     * Constructs the grid from a list of Parameters and Input/Output files.
     * 
     * @param propertyList
     * @param dataObjectList
     */
    public CommandLineOrderingGridPanel(final List<Property> propertyList) {
        setHeading(I18N.DISPLAY.cmdLineOrderingCaption());
        setBorders(true);

        compose();

        updateOrderingGrids(propertyList);

        // reset the command line order on all properties before they can be rearranged.
        updateParameterOrdering();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        setLayout(new RowLayout(Orientation.HORIZONTAL));

        RowData data = new RowData();
        data.setWidth(.5);
        data.setMargins(new Margins(4));
        add(unorderedGrid, data);

        data = new RowData();
        data.setWidth(.5);
        data.setMargins(new Margins(4));
        add(orderedGrid, data);
    }

    private void compose() {
        initOrderingGrids();
        initDragnDrop();
    }

    /**
     * Init the ordering grids with initial settings.
     * 
     * @param propertyList
     * @param dataObjectList
     */
    private void initOrderingGrids() {
        unorderedGrid = new Grid<PropertyData>(new ListStore<PropertyData>(),
                buildUnorderedColumnModel());
        unorderedGrid.setAutoExpandColumn(PropertyData.LABEL);
        unorderedGrid.setHeight(340);
        unorderedGrid.setBorders(true);
        unorderedGrid.getView().setEmptyText(I18N.DISPLAY.noParams());

        orderedGrid = new Grid<PropertyData>(new ListStore<PropertyData>(), buildOrderedColumnModel());
        orderedGrid.setAutoExpandColumn(PropertyData.LABEL);
        orderedGrid.setHeight(340);
        orderedGrid.setBorders(true);
        orderedGrid.getView().setEmptyText(I18N.DISPLAY.noParams());
    }

    /**
     * Updates the ordering grids with the given lists of properties and input/output files.
     * 
     * @param propertyList
     * @param dataObjectList
     */
    private void updateOrderingGrids(List<Property> propertyList) {
        // create a sorted PropertyData list and an unordered ListStore from the given Property list.
        ListStore<PropertyData> unorderedListStore = new ListStore<PropertyData>();
        List<PropertyData> listOrderedPropertyData = new ArrayList<PropertyData>();

        for (Property property : propertyList) {
            PropertyData propData = new PropertyData(property);

            if (propData.getOrder() < 0) {
                if (PropertyUtil.orderingRequired(propData.getProperty())) {
            		unorderedListStore.add(propData);
            	}
            } else {
                listOrderedPropertyData.add(propData);
            }
        }

        // sort by command line order
        Collections.sort(listOrderedPropertyData);

        // sort the unorderedListStore by display label
        unorderedListStore.sort(PropertyData.LABEL, SortDir.ASC);

        // The ordered grid needs to stay unsorted for drag n drop to work.
        ListStore<PropertyData> orderedListStore = new ListStore<PropertyData>();
        orderedListStore.add(listOrderedPropertyData);

        // populate the unordered and ordered grids.
        unorderedGrid.reconfigure(unorderedListStore, unorderedGrid.getColumnModel());
        orderedGrid.reconfigure(orderedListStore, orderedGrid.getColumnModel());
    }
    
    private boolean dataObjectImplicit(DataObject dataObj) {
    	boolean isImplicit = false;
    	if(dataObj != null) {
    		isImplicit = dataObj.isImplicit();
    	}
    	
    	return isImplicit;
    }

    /**
     * Builds a ColumnModel for the ordered grid with LABEL and ORDER columns.
     * 
     * @return ColumnModel for the ordered grid.
     */
    private ColumnModel buildOrderedColumnModel() {
        ColumnConfig colOrder = new ColumnConfig(PropertyData.ORDER, I18N.DISPLAY.order(), 40);
        colOrder.setSortable(false);
        colOrder.setMenuDisabled(true);

        ColumnConfig colParameters = new ColumnConfig(PropertyData.LABEL, I18N.DISPLAY.parameter(), 130);
        colParameters.setRenderer(new LabelGridCellRenderer());
        colParameters.setSortable(false);
        colParameters.setMenuDisabled(true);

        return new ColumnModel(Arrays.asList(colOrder, colParameters));
    }

    /**
     * Builds a ColumnModel for the unordered grid with a LABEL column.
     * 
     * @return ColumnModel for the unordered grid.
     */
    private ColumnModel buildUnorderedColumnModel() {
        ColumnConfig colParameters = new ColumnConfig(PropertyData.LABEL,
                I18N.DISPLAY.unorderedParameter(), 170);
        colParameters.setRenderer(new LabelGridCellRenderer());
        colParameters.setMenuDisabled(true);

        return new ColumnModel(Arrays.asList(colParameters));
    }

    /**
     * Resets the command line order on all properties based on their order in the ordered grid's
     * ListStore.
     */
    private void updateParameterOrdering() {
        ListStore<PropertyData> properties = orderedGrid.getStore();
        for (int row = 0,listSize = properties.getCount(); row < listSize; row++) {
            PropertyData property = properties.getAt(row);
            property.setOrder(row + 1);
            properties.update(property);
        }

        // fire a cmd line argument change event so the preview can be updated
        EventBus.getInstance().fireEvent(new CommandLineArgumentChangeEvent(null));
    }

    /**
     * Setup Drag'n'Drop for the ordered and unordered grids.
     */
    private void initDragnDrop() {
        new GridDragSource(unorderedGrid);
        new GridDragSource(orderedGrid);

        GridDropTarget target = new OrderedGridDropTarget(orderedGrid);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.INSERT);

        target = new UnorderedGridDropTarget(unorderedGrid);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.APPEND);
    }

    /**
     * A GridDropTarget implementation for the unordered grid that sets the command line order of any
     * parameter dropped into the grid to -1.
     * 
     * @author psarando
     */
    private class UnorderedGridDropTarget extends GridDropTarget {
        public UnorderedGridDropTarget(Grid<PropertyData> grid) {
            super(grid);
        }

        @Override
        public void onDragDrop(DNDEvent event) {
            List<PropertyData> dragList = event.getData();
            for (PropertyData source : dragList) {
                if (source != null) {
                    source.setOrder(-1);
                }
            }

            super.onDragDrop(event);

            updateParameterOrdering();
        }

    }

    /**
     * A GridDropTarget implementation for the ordered grid that sets the command line order on all
     * parameters based on their new order in the grid's ListStore.
     * 
     * @author psarando
     */
    private class OrderedGridDropTarget extends GridDropTarget {
        public OrderedGridDropTarget(Grid<PropertyData> grid) {
            super(grid);
        }

        @Override
        public void onDragDrop(DNDEvent event) {
            super.onDragDrop(event);

            updateParameterOrdering();
        }

    }

    /**
     * A GridCellRenderer implementation that displays additional info for the LABEL column based on the
     * parameters type (Property, Input, or Output file).
     * 
     * @author psarando
     */
    private class LabelGridCellRenderer implements GridCellRenderer<PropertyData> {
        @Override
        public Object render(PropertyData model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<PropertyData> store, Grid<PropertyData> grid) {
            return model.getLabel();
        }
    }
}
