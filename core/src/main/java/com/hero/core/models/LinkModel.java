package com.hero.core.models;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables= Resource.class)
public class LinkModel {

    @Inject
    public Externalizer externalizer;

    @Self
    public Resource resource;

    @Inject
    @Optional
    public String linkURL;

    @Inject
    @Optional
    public String textLink;

    @Inject
    @Optional
    public String opening;

    private String absoluteURL;

    @PostConstruct
    protected void init() {
        if (externalizer != null) {
            absoluteURL = externalizer.externalLink(resource.getResourceResolver(), Externalizer.LOCAL, linkURL) + ".html";
        } else {
            absoluteURL = "/hero/url";
        }
    }

    public String getAbsoluteURL() {
        return absoluteURL;
    }
}
