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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;


@Component(immediate = true, service = EventListener.class)
public class MyJcrEventListener implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJcrEventListener.class);
    private Session session;
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
                session.getWorkspace().getObservationManager().addEventListener(this, Event.PROPERTY_ADDED,
                        "/content/Hero/en",
                        true,
                        null,
                        null,
                        false);
//                session.save();
            }
        } catch (UnsupportedRepositoryOperationException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        LOGGER.info("My JCR observation");
    }

    @Deactivate
    public void deactivate() {
        if (session != null) {
            session.logout();
        }
    }
}
