package com.hero.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

@Model(adaptables= Resource.class)
public class TimeModel {
    private int time;

    @PostConstruct
    public void init() {
        this.time = (int) (Math.random() * 3);
    }

    public int getTime() {
        return time;
    }
}
