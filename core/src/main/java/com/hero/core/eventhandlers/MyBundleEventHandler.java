package com.hero.core.eventhandlers;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component(immediate = true, service = EventHandler.class, property = {"event.topics=com/adobe/acs/commons/automatic_page_replicator/REPLICATED"})
public class MyBundleEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBundleEventHandler.class);

        @Override
    public void handleEvent(Event event) {
        LOGGER.info(event.getTopic());
    }
}
