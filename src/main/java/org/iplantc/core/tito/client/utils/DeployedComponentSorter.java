package org.iplantc.core.tito.client.utils;

import org.iplantc.core.uicommons.client.models.DeployedComponent;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

/**
 * A class used to compare 2 deployed components while sorting without considering upper/lower case
 * 
 * @author sriram
 * @param <M>
 * 
 */
public class DeployedComponentSorter extends StoreSorter<DeployedComponent> {

    @Override
    public int compare(Store<DeployedComponent> store, DeployedComponent o1, DeployedComponent o2,
            String property) {
        if (o1 == null || o2 == null) {
            if (o1 == null && o2 == null) {
                return 0;
            } else {
                return (o1 == null) ? -1 : 1;
            }
        }

        return compareStrings(o1.getName(), o2.getName());
    }

    protected int compareStrings(String s1, String s2) {
        return s1.toLowerCase().compareTo(s2.toLowerCase());
    }

}
