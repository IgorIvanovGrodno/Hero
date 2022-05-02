package com.hero.core.models;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployeeModel {
    private final String SELECTOR = ".img";

    @Inject
    @Optional
    private String title;

    @Inject
    @Optional
    private String text;

    @Inject
    @Optional
    private String fileName;

    @Inject
    @Optional
    private String fileReference;

    @Self
    private Resource currentResource;

    private String imagePath;

    @PostConstruct
    protected void init() {
        if(fileReference!=null&&!fileReference.isEmpty()){
            imagePath=fileReference;
        }else{
            if(fileName!=null&&!fileName.isEmpty()){
                String extension = fileName.substring(fileName.indexOf('.'));
                imagePath=currentResource.getPath()+SELECTOR+extension;
            }
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
