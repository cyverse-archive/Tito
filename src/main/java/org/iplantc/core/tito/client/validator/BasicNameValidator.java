package org.iplantc.core.tito.client.validator;

import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * A validator that doesn't allow punctuation characters, brackets, backslashes, etc.
 * 
 * @author sriram, hariolf
 * 
 */
public class BasicNameValidator implements Validator {
    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(Field<?> field, String value) {
        char[] punct = {'!', '\"', '#', '$', '\'', '%', '&', '(', ')', '*', '+', ',', '/', '\\', ':',
                ';', '<', '>', '=', '?', '@', '[', ']', '^', '`', '{', '|', '}', '~'};
        char[] arr = value.toCharArray();

        // check for spaces at the beginning and at the end of the name
        if (arr[0] == ' ' || arr[arr.length - 1] == ' ') {
            return I18N.DISPLAY.nameValidationMsg();
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < punct.length; j++) {
                if (arr[i] == punct[j]) {
                    return I18N.DISPLAY.nameValidationMsg();
                }
            }
        }
        return null;
    }
}
