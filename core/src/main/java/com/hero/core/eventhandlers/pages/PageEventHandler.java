//package com.hero.core.eventhandlers.pages;
//
//import com.day.cq.wcm.api.PageEvent;
//import com.hero.core.eventhandlers.workflow.WorkFlowEventHandler;
//import org.apache.sling.api.resource.LoginException;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.api.resource.ResourceResolverFactory;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.osgi.service.event.Event;
//import org.osgi.service.event.EventConstants;
//import org.osgi.service.event.EventHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.jcr.Node;
//import javax.jcr.RepositoryException;
//import javax.jcr.Session;
//import javax.jcr.UnsupportedRepositoryOperationException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component(immediate = true, service = EventHandler.class, property = {EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC})
//public class PageEventHandler implements EventHandler {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PageEventHandler.class);
//    private int i = 0;
//    @Reference
//    ResourceResolverFactory resourceResolverFactory;
//
//    @Override
//    public void handleEvent(Event event) {
//        LOGGER.info(event.toString());
//        Map<String, Object> properties = new HashMap<>();
//        properties.put(ResourceResolverFactory.SUBSERVICE, "subMyObservation");
//        ResourceResolver resourceResolver = null;
//        Session session = null;
//        try {
//            resourceResolver = resourceResolverFactory.getServiceResourceResolver(properties);
//            session = resourceResolver.adaptTo(Session.class);
//            if (session != null) {
//                Node node = session.getNode("/content/Hero/jcr:content");
//                node.setProperty("myQ" + i, "MyT");
//                i++;
//                session.save();
//            }
//        } catch (LoginException | RepositoryException e) {
//            e.printStackTrace();
//        }
//    }
//}
