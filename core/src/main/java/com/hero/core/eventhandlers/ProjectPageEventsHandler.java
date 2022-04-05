package com.hero.core.eventhandlers;

import com.day.cq.wcm.api.PageEvent;
import com.hero.core.service.CreatingVersionService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = EventHandler.class, property = {EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC
//        , EventConstants.EVENT_FILTER + "=(modifications=[{path=/content/Hero/man}]))"
})
public class ProjectPageEventsHandler implements EventHandler {
    @Reference
    private CreatingVersionService creatingVersionService;

    @Override
    public void handleEvent(Event event) {
        ArrayList<HashMap> prop = (ArrayList<HashMap>) event.getProperty("modifications");
        HashMap<String, String> valueMap = prop.get(0);
        String path = valueMap.get("path");
        if (path.matches("/content/myGroup/.*")) {
//            creatingVersionService.createVersion(path);
        }
//        creatingVersionService.createVersion(path);
    }
}
