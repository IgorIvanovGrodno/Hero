package com.hero.core.serviceimpl;

import com.hero.core.service.CreatingVersionService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
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
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionManager;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = CreatingVersionService.class)
public class CreatingVersionServiceImpl implements CreatingVersionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatingVersionServiceImpl.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void activate(ComponentContext context) {

    }
    @Override
    public void createVersion(String path) {
        LOGGER.info("Service work");
        Map<String, Object> properties = new HashMap<>();
        properties.put(ResourceResolverFactory.SUBSERVICE, "subMyObservation");
        ResourceResolver resourceResolver = null;
        Session session;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(properties);
            session = resourceResolver.adaptTo(Session.class);
            if (session != null) {
                try {
                    Node nodeOfPage = session.getNode(path + "/jcr:content");

                    if (nodeOfPage.canAddMixin("mix:versionable")) {
                        nodeOfPage.addMixin("mix:versionable");
                        session.save();
                        VersionManager versionManager = session.getWorkspace().getVersionManager();
                        versionManager.checkpoint(path + "/jcr:content");
//                        VersionHistory versionHistory = versionManager.getVersionHistory("/content/myGroup/en" + "/jcr:content");
//                        Version version = versionHistory.getVersion("1.3");
//                        versionManager.restore(version, true);
//                        versionManager.checkout("/content/myGroup/en" + "/jcr:content");
                    }
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
