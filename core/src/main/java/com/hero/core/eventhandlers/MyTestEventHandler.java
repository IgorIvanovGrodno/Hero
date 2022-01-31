package com.hero.core.eventhandlers;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
