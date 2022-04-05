package com.hero.core.eventhandlers.sling.jobs;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component(immediate = true, service = JobConsumer.class, property = JobConsumer.PROPERTY_TOPICS + "=com/hero/job/store/properties")
public class StorePropertiesJobConsumer implements JobConsumer {
    private Session session;

    @Reference
    private SlingRepository slingRepository;

    @Override
    public JobResult process(Job job) {
        String property =  job.getProperty("property", String.class);
        try {
            this.session = slingRepository.loginService("subMyObservation", null);
            if (session != null) {
                String uniqueID = UUID.randomUUID().toString();
                if (session.nodeExists("/var/log-property")) {
                    Node propertyStorageNode = session.getNode("/var/log");
                    propertyStorageNode.addNode("property-" + uniqueID).setProperty("path", property);

                } else {
                    Node varNode = session.getNode("/var");
                    varNode.addNode("log-property").addNode("property-" + uniqueID).setProperty("path", property);
                }
                session.save();
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        return JobResult.OK;
    }

    @Deactivate
    public void deactivate() {
        if (session != null) {
            session.logout();
        }
    }
}
