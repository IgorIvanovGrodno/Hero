package com.hero.core.eventhandlers.replication;

import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.ReplicationAction;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = EventHandler.class, property = {EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC, EventConstants.EVENT_FILTER + "=(paths=/content/Hero/en)"})
public class MyReplicationEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyReplicationEventHandler.class);

    @Override
    public void handleEvent(Event event) {
        LOGGER.info(event.toString());
        ReplicationAction replicationEvent = ReplicationAction.fromEvent(event);
        String path = replicationEvent.getPath();
        AgentConfig agentConfig = replicationEvent.getConfig();
    }
}
