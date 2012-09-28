package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
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
    protected BasicEditableList list;
    
    /**
     * Create a new instance
     * 
     * @param property property that is currently edited
     * @param category category for this property
     */
    public AbstractListPropertyEditorPanel(Property property) {
        this.property = property;

        setLayout(new FitLayout());
        allocateList();
    }
   
   /**
    * Init the BasicEditableList for this category
    */
    protected abstract void allocateList();
   
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
