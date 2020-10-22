package com.hero.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables= Resource.class)
public class TextModel {
    @Inject
    @Optional
    public String title;

    @Inject
    @Optional
    public String text;

    @Inject
    @Optional
    public boolean textIsRich;

    private String context;

    @PostConstruct
    protected void init() {
        context = textIsRich ? "html":"text";
    }

    public String getContext() {
        return context;
    }
}
