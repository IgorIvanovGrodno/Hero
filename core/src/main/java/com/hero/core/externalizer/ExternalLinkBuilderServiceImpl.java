package com.hero.core.externalizer;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = ExternalLinkBuilderService.class)
public class ExternalLinkBuilderServiceImpl implements ExternalLinkBuilderService{
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Externalizer externalizer;

    @Reference
    private SlingSettingsService slingSettingsService;

    @Override
    public String buildExternalLink(String internalPath) {
        String externalLink = null;
        if (slingSettingsService != null) {
            if (slingSettingsService.getRunModes().contains("author")) {
                externalLink = externalizer.externalLink(resourceResolverFactory.getThreadResourceResolver(), "test",
                        internalPath + ".html");
            } else {
                externalLink = externalizer.publishLink(resourceResolverFactory.getThreadResourceResolver(),
                        internalPath + ".html");
            }
        }
        return externalLink;
    }
}
