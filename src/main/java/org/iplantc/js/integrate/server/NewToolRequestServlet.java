package org.iplantc.js.integrate.server;

import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.iplantc.de.server.UploadServlet;
import org.iplantc.js.integrate.client.I18N;

/**
 * A class to accept files from the client.
 * 
 * This class extends the UploadAction class provided by the GWT Upload library. The executeAction method
 * must be overridden for custom behavior.
 * 
 * @author sriram
 * 
 */
public class NewToolRequestServlet extends UploadServlet {
    private static final long serialVersionUID = 1L;

    /**
     * The logger for error and informational messages.
     */
    private static Logger LOG = Logger.getLogger(NewToolRequestServlet.class);

    /**
     * Performs the necessary operations for an upload action.
     * 
     * @param request the HTTP request associated with the action.
     * @param fileItems the file associated with the action.
     * @return a string representing data in JSON format.
     * @throws UploadActionException if there is an issue invoking the dispatch to the servlet
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> fileItems)   {
        super.executeAction(request, fileItems);

        SimpleMessageSender msgSender = new SimpleMessageSender();
        try {
            msgSender.send(user, email, jsonInfo.toString(2));
        } catch (Exception e) {
            LOG.error("FileUploadServlet::executeAction - Exception while sending email to support about tool request:" //$NON-NLS-1$
                    + e.getMessage());
            e.printStackTrace();
            jsonErrors.put("error", e.getMessage()); //$NON-NLS-1$
            return jsonErrors.toString();
        }

        LOG.debug("NewToolRequestServlet::executeAction - Attempted to send email."); //$NON-NLS-1$
        jsonInfo.put("success", I18N.DISPLAY.toolRequestSucess()); //$NON-NLS-1$
        // remove files from session. this avoids duplicate submissions
        removeSessionFileItems(request, false);

        LOG.debug("NewToolRequestServlet::executeAction - JSON returned: " + jsonInfo); //$NON-NLS-1$
        return jsonInfo.toString();
    }

}
