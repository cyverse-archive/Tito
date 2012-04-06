package org.iplantc.core.tito.client.services;

import org.iplantc.de.shared.SharedAuthenticationValidatingServiceFacade;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EnumerationServices {
    public void getDataFormats(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.formats"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getWidgetTypes(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(
                "org.iplantc.services.zoidberg.propertytypes"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getRuleTypes(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.ruletypes"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void addIntegration(JSONObject tool, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PUT,
                "org.iplantc.services.zoidberg.inprogress", tool.toString()); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getIntegrationsAsSummary(String user, AsyncCallback<String> callback) {
        user = URL.encode(user);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET,
                "org.iplantc.services.zoidberg.inprogress?user=" + user + "&summary=true"); //$NON-NLS-1$ //$NON-NLS-2$
        callService(callback, wrapper);
    }

    public void getIntegrationById(String id, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET,
                "org.iplantc.services.zoidberg.inprogress?tito=" + id); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getUUID(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.uuid"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getDeployedComponents(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.components"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getInfoTypes(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.infotypes"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getPreview(JSONObject tool, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST,
                "org.iplantc.services.zoidberg.preview", tool.toString()); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void saveIntegration(JSONObject tool, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST,
                "org.iplantc.services.zoidberg.inprogress", tool.toString()); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void deleteIntegration(JSONObject body, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST,
                "org.iplantc.services.zoidberg.inprogress", body.toString()); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void saveAndPublish(String json, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PUT,
                "org.iplantc.services.zoidberg.publish?", json); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getAnalysisGroups(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET,
                "org.iplantc.services.zoidberg.analgroups"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    private void callService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {
        SharedAuthenticationValidatingServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void getIntegrationAsSummary(String templateID, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET,
                "org.iplantc.services.zoidberg.inprogress?tito=" + templateID + "&summary=true"); //$NON-NLS-1$ //$NON-NLS-2$
        callService(callback, wrapper);
    }

}
