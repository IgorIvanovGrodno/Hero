package com.hero.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, adapters = {RatingModel.class}, resourceType = {"Hero/components/content/helloworld"})
public class SecondRatingModelImpl implements RatingModel{
    @Override
    public String getRating() {
        return "Second rating";
    }
}
