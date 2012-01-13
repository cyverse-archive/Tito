package org.iplantc.js.integrate.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.js.integrate.client.dialogs.validation.BasicEditableList;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
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
    protected final String category;
    protected BasicEditableList list;
    
    /**
     * Create a new instance
     * 
     * @param property property that is currently edited
     * @param category category for this property
     */
    public AbstractListPropertyEditorPanel(Property property, String category) {
        this.property = property;
        this.category = category;
        allocateList(category);
    }
   
   /**
    * Init the BasicEditableList for this category
    * 
    * @param category
    */
   protected abstract void allocateList(final String category);
   
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
