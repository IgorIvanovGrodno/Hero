package com.hero.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public interface InterfaceModel {
    String PROPERTY_TITLE = "title";
    String PROPERTY_TEXT = "text";

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL, name = PROPERTY_TITLE)
    String getTitle();

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL, name = PROPERTY_TEXT)
    String getText();

}
