package org.iplantc.core.tito.client.models;

import java.util.Date;

import org.iplantc.core.uicommons.client.util.DateParser;

import com.extjs.gxt.ui.client.data.BaseModelData;
/**
 * A model that represents template summary
 * 
 * @author sriram
 *
 */
public class TemplateSummary extends BaseModelData {

    public static final String ID = "id"; //$NON-NLS-1$
    public static final String NAME = "name"; //$NON-NLS-1$
    public static final String STATUS = "status"; //$NON-NLS-1$
    public static final String LAST_EDITED_DATE = "edited_date"; //$NON-NLS-1$
    public static final String PUBLISHED_DATE = "published_date"; //$NON-NLS-1$
    public static final String IS_PUBLIC = "is_public"; //$NON-NLS-1$
    public static final String IS_PUBLISHABLE = "is_publishable"; //$NON-NLS-1$
    public static final String IS_ORDERED = "is_ordered"; //$NON-NLS-1$

    /**
	 * 
	 */
    private static final long serialVersionUID = -9081949865361530349L;

    public TemplateSummary(String id, String name, String status, String last_edited_date,
            String published_date, boolean is_public, boolean is_publishable, boolean is_ordered) {
        set(ID, id);
        set(NAME, name);
        set(STATUS, status);
        set(LAST_EDITED_DATE, DateParser.parseDate(last_edited_date));
        set(PUBLISHED_DATE, DateParser.parseDate(published_date));
        set(IS_PUBLIC, is_public);
        set(IS_PUBLISHABLE, is_publishable);
        set(IS_ORDERED, is_ordered);
    }

    /**
     * @return the id
     */
    public String getId() {
        return get(ID);
    }

    /**
     * @return the name
     */
    public String getName() {
        return get(NAME);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return get(STATUS);
    }
    
    /**
     * @return last edited date
     */
    public Date getLastEditedDate() {
        return get(LAST_EDITED_DATE);
    }
    
    /**
     * @return published date
     */
    public Date getPublishedDate() {
        return get(PUBLISHED_DATE);
    }
    
    /**
     * 
     * @return is public flag
     */
    public Boolean isPublic() {
        return get(IS_PUBLIC);
    }

    /**
     * 
     * @return is publishable flag
     */
    public Boolean isPublishable() {
        return get(IS_PUBLISHABLE);
    }

    /**
     * @return is ordered flag
     * @return
     */
    public Boolean isOrdered() {
        return get(IS_ORDERED);
    }

}
