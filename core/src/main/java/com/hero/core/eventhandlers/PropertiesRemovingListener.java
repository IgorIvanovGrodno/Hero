package com.hero.core.eventhandlers;

import com.hero.core.eventhandlers.sling.jobs.StorePropertiesJob;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = EventListener.class)
public class PropertiesRemovingListener implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesRemovingListener.class);
    private Session session;

    @Reference
    private StorePropertiesJob storePropertiesJob;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private SlingRepository slingRepository;

    @Activate
    public void activate(ComponentContext context) {
        try {
            this.session = slingRepository.loginService("subMyObservation", null);
            if (session != null) {
                session.getWorkspace().getObservationManager().addEventListener(this, Event.PROPERTY_REMOVED,
                        "/content/Hero/",
                        true,
                        null,
                        null,
                        false);
                session.save();
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        LOGGER.info("Event jcr");
        while (eventIterator.hasNext()) {
           Event event = eventIterator.nextEvent();
            try {
                String path = event.getPath();
                storePropertiesJob.createJob(path);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
    }

    @Deactivate
    public void deactivate() {
        if (session != null) {
            session.logout();
        }
    }

}
