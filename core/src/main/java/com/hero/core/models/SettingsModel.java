package com.hero.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;

@Model(adaptables= SlingHttpServletRequest.class)
public class SettingsModel {

    @Inject
    @Optional
    @Default(values="empty")
    public String layout;
}
