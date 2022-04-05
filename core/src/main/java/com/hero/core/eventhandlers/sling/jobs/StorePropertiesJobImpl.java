package com.hero.core.eventhandlers.sling.jobs;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Property;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = StorePropertiesJob.class)
public class StorePropertiesJobImpl implements StorePropertiesJob {

    @Reference
    private JobManager jobManager;

    @Override
    public void createJob(String path) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("property", path);
        jobManager.addJob("com/hero/job/store/properties", properties);
    }
}
