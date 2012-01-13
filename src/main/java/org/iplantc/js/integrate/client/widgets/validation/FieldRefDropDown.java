package org.iplantc.js.integrate.client.widgets.validation;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;

import com.google.gwt.json.client.JSONString;

/**
 * A drop-down for selecting a property sheet. TODO instead of strings, use PropertySheet objects in the
 * combo box (or maybe not?)
 * 
 * @author hariolf
 */
public class FieldRefDropDown extends SimpleDropDown {

    /**
     * Creates a FieldRefDropDown containing the names of all property sheets from a
     * PropertyGroupContainer.
     * 
     * @param pgContainer
     * @param property
     * @param value
     */
    public FieldRefDropDown(PropertyGroupContainer pgContainer, Property property, JSONString value) {
        super(buildStringList(pgContainer, property));

        setValue(value);
    }

    /** builds a list of strings for the combo box */
    private static List<String> buildStringList(PropertyGroupContainer pgContainer, Property property) {
        List<Property> editors = filterGroups(pgContainer.getProperties(), property);
        List<String> strings = new ArrayList<String>();

        for (Property editor : editors) {
            strings.add(editor.getLabel());
        }

        return strings;
    }

    /**
     * Returns a new List that contains only property sheets of type SINGLE from the original List of
     * property sheets.
     * 
     * @param properties a list of properties
     * @param property the property whose validator is being edited
     * @return a new list containing only SINGLE type sheets
     */
    private static List<Property> filterGroups(List<Property> properties, Property property) {
        List<Property> filtered = new ArrayList<Property>();

        for (Property referencedProperty : properties) {
            if (property.getType().equals(referencedProperty.getType())
                    && referencedProperty != property) {
                filtered.add(referencedProperty);
            }
        }

        return filtered;
    }
}
