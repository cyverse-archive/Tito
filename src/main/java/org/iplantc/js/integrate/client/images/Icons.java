package org.iplantc.js.integrate.client.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Provides access to bundled image resources.
 */
public interface Icons extends ClientBundle {
    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("list-items.gif")
    ImageResource listItems();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("new.gif")
    ImageResource add();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("delete.gif")
    ImageResource cancel();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("save.gif")
    ImageResource save();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("go_back.png")
    ImageResource back();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("logout.png")
    ImageResource logout();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("preview.png")
    ImageResource preview();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("publish.png")
    ImageResource publish();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("copy.png")
    ImageResource copy();

}
