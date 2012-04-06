package org.iplantc.core.tito.client.validator;

import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * A validator to validate url
 * 
 * @author sriram
 *
 */
public class BasicUrlValidator implements Validator {

    @Override
    public String validate(Field<?> field, String value) {

        String urlPattern = "^(?:http(s{0,1})://|www)[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%\\+\\!\\*\\(\\)\\'\\#\\_]*"; //$NON-NLS-1$
        if (value == null) {
            return I18N.DISPLAY.inValidUrl();
        }
        if (!value.matches(urlPattern)) {
            return I18N.DISPLAY.inValidUrl();
        } else {
            return null;
        }

    }

}
