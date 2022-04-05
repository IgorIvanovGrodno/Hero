package com.hero.core.models;

import com.hero.core.config.HeroConfig;
import com.hero.core.serviceimpl.DatabaseService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;

@Model(adaptables= Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataBaseModel {
    private ArrayList<String> list;

    @Inject
    private DatabaseService databaseService;

    @PostConstruct
    public void init() {
        this.list = databaseService.getNames();
    }

    public ArrayList<String> getList() {
        return list;
    }
}
