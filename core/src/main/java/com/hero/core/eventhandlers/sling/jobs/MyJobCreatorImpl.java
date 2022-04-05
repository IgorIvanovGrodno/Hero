package com.hero.core.eventhandlers.sling.jobs;

import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = MyJobCreator.class)
public class MyJobCreatorImpl implements MyJobCreator{

    @Reference
    private JobManager jobManager;

    @Override
    public void createJob() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop", "hahah");
        properties.put("prop2", 1);

        jobManager.addJob("com/hero/my/job", properties);

    }
}
