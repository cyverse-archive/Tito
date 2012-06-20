package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.tito.client.dialogs.validation.BasicEditableList;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
/**
 * 
 * An abstract class for all special list property editing requirements
 * 
 * @author sriram
 *
 */

public abstract class AbstractListPropertyEditorPanel extends LayoutContainer {

    protected final Property property;
    protected final PropertyTypeCategory category;
    protected BasicEditableList list;
    
    /**
     * Create a new instance
     * 
     * @param property property that is currently edited
     * @param category category for this property
     */
    public AbstractListPropertyEditorPanel(Property property, PropertyTypeCategory category) {
        this.property = property;
        this.category = category;

        setLayout(new FitLayout());
        allocateList(category);
    }
   
   /**
    * Init the BasicEditableList for this category
    * 
    * @param category
    */
    protected abstract void allocateList(final PropertyTypeCategory category);
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected void onRender(Element parent, int pos) {
       super.onRender(parent, pos);

       if (list != null) {
           add(list);
       }
       
      
   }
    
}
