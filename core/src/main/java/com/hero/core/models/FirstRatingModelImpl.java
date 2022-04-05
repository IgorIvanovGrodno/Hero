package com.hero.core.models;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.api.resource.Resource;

@Model(adaptables = Resource.class, adapters = {RatingModel.class}, resourceType = {"Hero/components/content/hero"})
public class FirstRatingModelImpl implements RatingModel {

    @Override
    public String getRating() {
        return "First rating";
    }
}
