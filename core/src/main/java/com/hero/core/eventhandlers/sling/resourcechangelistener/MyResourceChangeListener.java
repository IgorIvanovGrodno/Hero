package com.hero.core.eventhandlers.sling.resourcechangelistener;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(immediate = true,
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=/content/Hero",
                ResourceChangeListener.CHANGES + "=ADDED"
        })
public class MyResourceChangeListener implements ResourceChangeListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyResourceChangeListener.class);

    @Reference
    private JobManager jobManager;

    @Override
    public void onChange(List<ResourceChange> list) {
        for (ResourceChange resourceChange : list) {
            LOGGER.info("My Resource Change Listener " + resourceChange.getPath());
        }
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop", "hahah");
        properties.put("prop2", 1);
        jobManager.addJob("com/hero/my/job", properties);
    }
}
