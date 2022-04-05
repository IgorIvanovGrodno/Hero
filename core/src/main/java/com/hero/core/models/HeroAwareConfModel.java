package com.hero.core.models;

import com.day.cq.wcm.api.Page;
import com.hero.core.config.HeroConfig;
import com.hero.core.externalizer.ExternalLinkBuilderService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroAwareConfModel {

    @ScriptVariable
    Page currentPage;

    @OSGiService
    private ExternalLinkBuilderService externalLinkBuilderService;

    @SlingObject
    ResourceResolver resourceResolver;

    @OSGiService
    private ConfigurationResolver configurationResolver;
    private String siteLocale;
    private String externalLink;
    private HeroConfig heroConfig;

    @PostConstruct
    public void init() {
        HeroConfig her = getContextAwareConfig(currentPage.getPath(), resourceResolver);
        if (her != null) {
            siteLocale = her.siteLocale();
        } else {
            siteLocale = "siteLocale NULL";
        }
        externalLink = externalLinkBuilderService.buildExternalLink("/haha");
    }

    private HeroConfig getContextAwareConfig(String currentPagePath, ResourceResolver resourceResolver) {
        String currentPath = currentPagePath != null && !currentPagePath.isEmpty() ? currentPagePath : "";
        Resource currentResource = resourceResolver.getResource(currentPath);
        if (currentResource != null) {
            ConfigurationBuilder configurationBuilder = currentResource.adaptTo(ConfigurationBuilder.class);
            if (configurationBuilder != null) {
                return configurationBuilder.as(HeroConfig.class);
            }
        }
        //Use the Optional!!!
        return null;
    }

    public String getSiteLocale() {
        return siteLocale;
    }

    public String getExternalLink() {
        return externalLink;
    }
}
