package org.iplantc.core.tito.client.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.metadata.WizardPropertyGroupContainer;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.WizardBuilder;
import org.iplantc.core.tito.client.events.WizardValidationEvent;
import org.iplantc.core.tito.client.events.WizardValidationEventHandler;
import org.iplantc.core.tito.client.strategies.WizardValidationBroadcastStrategy;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;

/**
 * A dialog that displays preview of the wizard that is being currently defined
 * 
 * @author sriram
 *
 */
public class PreviewDialog extends Dialog {
    private ComponentValueTable tblComponentVals;

    private List<HandlerRegistration> handlers;

    /**
     * Constructs an instance of the object given an identifier.
     * 
     * @param tag a unique identifier used as a "window handle."
     */
    public PreviewDialog(final JSONObject objJson) {
        tblComponentVals = new ComponentValueTable(new WizardValidationBroadcastStrategy());

        init();
        build(objJson);
    }

    private void init() {
        setBorders(false);
        setShadow(false);
        setResizable(false);
        setSize(628, 532);
        setModal(true);
        initButtons();
    }

    private Button getOkButton() {
        return getButtonById(Dialog.OK);
    }

    private Button getCancelButton() {
        return getButtonById(Dialog.CANCEL);
    }

    private void initButtons() {
        setButtons(Dialog.OKCANCEL);

        Button btnOk = getOkButton();
        btnOk.setText(I18N.DISPLAY.previewSubmit());

        btnOk.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                MessageBox.info(I18N.DISPLAY.previewDeployedDialogCaption(),
                        I18N.DISPLAY.previewDeployedDialogText(), new AnalysisLaunchListenerImpl());
            }
        });

        getCancelButton().addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                doClose();
            }
        });
    }

    private void enableValidation() {
        // now that the panels have been initialized, we can allow validation
        tblComponentVals.enableValidation();

        // we need to enforce contracts before any validation can occur
        tblComponentVals.enforceContracts();

        // perform our initial validation... we circumvent the normal method of validation
        // because this window is created from a fired event and if our validator fires an
        // event (that would
        // potentially disable our buttons) that event is never picked up by the system.
        List<String> errors = tblComponentVals.validate(false);

        if (errors.isEmpty()) {
            getOkButton().enable();
        } else {
            getOkButton().disable();
        }
    }

    private void build(final JSONObject objJson) {
        WizardPropertyGroupContainer container = new WizardPropertyGroupContainer(objJson);
        WizardBuilder builder = new WizardBuilder();
        setHeading(I18N.DISPLAY.preview() + " - " + container.getLabel()); //$NON-NLS-1$
        add(builder.build(container, tblComponentVals));
        layout();
        registerEventHandlers();
        enableValidation();
    }

    /**
     * Add handlers for events
     */
    private void registerEventHandlers() {
        handlers = new ArrayList<HandlerRegistration>();

        EventBus eventbus = EventBus.getInstance();

        handlers.add(eventbus.addHandler(WizardValidationEvent.TYPE, new WizardValidationEventHandler() {
            @Override
            public void onInvalid(WizardValidationEvent event) {
                getOkButton().disable();
            }

            @Override
            public void onValid(WizardValidationEvent event) {
                getOkButton().enable();
            }
        }));
    }

    private void removeEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    private void doClose() {
        removeEventHandlers();
        hide();
    }

    private class AnalysisLaunchListenerImpl implements Listener<MessageBoxEvent> {
        @Override
        public void handleEvent(MessageBoxEvent be) {
            doClose();
        }
    }
}
