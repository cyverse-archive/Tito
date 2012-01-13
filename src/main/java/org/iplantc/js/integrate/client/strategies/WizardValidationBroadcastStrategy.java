package org.iplantc.js.integrate.client.strategies;

import java.util.List;

import org.iplantc.core.client.widgets.strategies.IWizardValidationBroadcastStrategy;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.js.integrate.client.events.WizardValidationEvent;

public class WizardValidationBroadcastStrategy implements IWizardValidationBroadcastStrategy {
    @Override
    public void broadcast(List<String> errors) {
        boolean isValid = errors.isEmpty();

        EventBus eventbus = EventBus.getInstance();

        WizardValidationEvent event = new WizardValidationEvent(isValid);
        eventbus.fireEvent(event);
    }
}
