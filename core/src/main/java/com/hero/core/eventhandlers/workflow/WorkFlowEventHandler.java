package com.hero.core.eventhandlers.workflow;

import com.day.cq.workflow.event.WorkflowEvent;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = EventHandler.class, property = {EventConstants.EVENT_TOPIC + "=" + WorkflowEvent.EVENT_TOPIC})
public class WorkFlowEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowEventHandler.class);
    @Override
    public void handleEvent(Event event) {
        LOGGER.info(event.getTopic() + " " + event.getPropertyNames());
    }
}
