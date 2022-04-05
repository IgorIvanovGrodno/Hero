package com.hero.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;

//import javax.annotation.PostConstruct;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables= Resource.class)
public class ImageModel {
    private final String SELECTOR = ".img";

    @Inject
    @Optional
    public String fileName;

    @Inject
    @Optional
    public String fileReference;

    @Self
    public Resource currentResource;

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
}
