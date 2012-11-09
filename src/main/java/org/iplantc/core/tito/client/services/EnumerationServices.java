package org.iplantc.core.tito.client.services;

import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.SharedAuthenticationValidatingServiceFacade;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.iplantc.de.shared.SharedServiceFacade;

public class EnumerationServices {
    public void getWidgetTypes(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(
                "org.iplantc.services.zoidberg.propertytypes"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getRuleTypes(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.ruletypes"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    public void getIntegrationById(String id, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET,
                "org.iplantc.services.zoidberg.inprogress/" + id); //$NON-NLS-1$
        callSecuredService(callback, wrapper);
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

    public void saveAndPublish(String json, AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PUT,
                "org.iplantc.services.zoidberg.publish", json); //$NON-NLS-1$
        callSecuredService(callback, wrapper);
    }

    public void getDataSources(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl()
                + "get-workflow-elements/data-sources"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    private void callService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {
        SharedAuthenticationValidatingServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    private void callSecuredService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {
        SharedServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
