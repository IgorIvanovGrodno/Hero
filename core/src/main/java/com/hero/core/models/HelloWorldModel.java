package com.hero.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables= Resource.class)
public class HelloWorldModel {

    @Inject
    @Optional
    public String text;

}
