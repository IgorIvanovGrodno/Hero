package com.hero.core.eventhandlers.sling.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = JobConsumer.class, property = JobConsumer.PROPERTY_TOPICS + "=com/hero/my/job")
public class MyJobConsumer implements JobConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJobConsumer.class);

    @Override
    public JobResult process(Job job) {
        if (job != null) {
            String prop = job.getProperty("prop", String.class);
            LOGGER.info(job.getTopic() + prop);
            return JobResult.OK;
        } else {
            return JobResult.FAILED;
        }
    }
}
