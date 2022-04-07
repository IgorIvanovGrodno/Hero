package com.hero.core.eventhandlers;


import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;

@Component(immediate = true, service = EventHandler.class, property = {"event.topics=com/hero"})
public class MyTestEventHandler implements EventHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyTestEventHandler.class);


    @Override
    public void handleEvent(Event event) {
//        FrameworkUtil.getBundle(MyTestEventHandler.class).getBundleContext().addFrameworkListener(new MyFrameworkListener());
        String ev = event.getTopic();
        LOGGER.info(event.getTopic());
    }
}
