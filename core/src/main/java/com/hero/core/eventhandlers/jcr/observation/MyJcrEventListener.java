package com.hero.core.eventhandlers.jcr.observation;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


@Component(immediate = true, service = EventListener.class)
public class MyJcrEventListener implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJcrEventListener.class);
    private Session session;

    @Reference
    private EventAdmin eventAdmin;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private SlingRepository slingRepository;

    @Activate
    public void activate(ComponentContext context) {
//        Map<String, Object> properties = new HashMap<>();
//        properties.put(ResourceResolverFactory.SUBSERVICE, "subMyTestUser");
//        ResourceResolver resourceResolver = null;

        String[] types = {"cq:PageContent"};
        try {
//            resourceResolver = resourceResolverFactory.getServiceResourceResolver(properties);
//            Session session = resourceResolver.adaptTo(Session.class);
            this.session = slingRepository.loginService("subMyTestUser", null);

            if (session != null) {
                session.getWorkspace().getObservationManager().addEventListener(
                        this,                           //handler
                        Event.PROPERTY_ADDED,                      //event type
                        "/content/Hero/en/jcr:content",            //path
                        false,                                     //is Deep?
                        null,                                   //UUIDs filter
                        null,                                  //nodetypes filter
                        false);
//                session.save();
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        try {
            while (eventIterator.hasNext()) {
                Event event = eventIterator.nextEvent();
                if (event != null) {
                    LOGGER.info("My JCR observation ID: " + event.getIdentifier() + " path: " + event.getPath());
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        Dictionary properties = new Hashtable();
        properties.put("title", "title");
        properties.put("path", "/some/path");
        org.osgi.service.event.Event event = new org.osgi.service.event.Event("com/hero", properties);
        if (event != null) {
        eventAdmin.postEvent(event);
    }

    }

    @Deactivate
    public void deactivate() {
        try {
            // Get JCR ObservationManager from Workspace
            final ObservationManager observationManager = session.getWorkspace().getObservationManager();

            if (observationManager != null) {
                // Un-register event handler
                observationManager.removeEventListener(this);
            }
        } catch (UnsupportedRepositoryOperationException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {
            // Good housekeeping; Close your JCR Session when you are done w them!
            if (session != null) {
                session.logout();
            }
        }
    }
}
