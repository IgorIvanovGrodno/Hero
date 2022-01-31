package com.hero.core.filters;

import com.day.image.Layer;
import com.hero.core.service.ImageRotateService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.*;
import java.io.*;

//@Component
//@SlingServletFilter(scope = {SlingServletFilterScope.REQUEST},
//        pattern = "/bin/.*",
//        methods = {"GET","HEAD"})

@Component(service = Filter.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=My Demo to filter incoming requests",
                EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
                EngineConstants.SLING_FILTER_PATTERN + "=" + "/content/we-retail/.*\\.jpeg",
                "sling.filter.extensions" + "=" + "{jpeg}",
                Constants.SERVICE_RANKING + ":Integer=-700"
        })

//@SlingFilter(scope = SlingFilterScope.REQUEST, order = -10000, metatype = false)
public class ImageRotateFilter implements Filter {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ResourceResolver resourceResolver;

    @Reference
    private ImageRotateService imageRotateService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SlingHttpServletRequest slingHttpServletRequest = (SlingHttpServletRequest) servletRequest;
        String pathToFile = slingHttpServletRequest.getRequestPathInfo().getResourcePath();
        SlingHttpServletResponse slingHttpServletResponse = (SlingHttpServletResponse)servletResponse;
        this.resourceResolver = ((SlingHttpServletRequest) servletRequest).getResourceResolver();
        try {
            String pathImageFile = this.getNodeByPath(pathToFile).getProperty("fileReference").getString() + "/jcr:content/renditions/original/jcr:content";
            OutputStream outputStream = slingHttpServletResponse.getOutputStream();
            try {
                Layer rotatedImage = imageRotateService.imageAction(this.getNodeByPath(pathImageFile));
                if (rotatedImage != null) {
                    imageRotateService.writeImage(rotatedImage, outputStream, "image/jpeg", 1);
                }
            } finally {
                outputStream.flush();
                outputStream.close();
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        filterChain.doFilter(servletRequest, slingHttpServletResponse);
    }

    @Override
    public void destroy() {

    }

    private Node getNodeByPath(String path) {
        Resource resource = this.resourceResolver.getResource(path);
        Node node = resource.adaptTo(Node.class);
        return node;
    }
}
