package com.hero.core.service;

import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import java.util.HashMap;

@Component
public class GroupCreateServiceImpl implements GroupCreateService{

    @Reference
    ResourceResolverFactory resourceResolverFactory;
    @Override
    public String createGroup(String name) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(ResourceResolverFactory.SUBSERVICE, "iharivanou");
        ResourceResolver resourceResolver = null;
        String s;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(parameters);
            UserManager userManager = resourceResolver.adaptTo(UserManager.class);
            if(userManager != null){
                try {
                    userManager.createGroup(name);
                    return "success";
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    return "fail";
                }
            }
            return "manager problems";

        } catch (LoginException e) {
            e.printStackTrace();
            s = e.getMessage();
        }
        return "resolver problems" + s;
    }
}
