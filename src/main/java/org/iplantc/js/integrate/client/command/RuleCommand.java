package org.iplantc.js.integrate.client.command;

import org.iplantc.core.metadata.client.validation.MetaDataRule;

/**
 * A command class to work the MetaDataRule
 * 
 * @author sriram
 *
 */
public interface RuleCommand {
    void execute(MetaDataRule rule);
}
