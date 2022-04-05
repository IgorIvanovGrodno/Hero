package com.hero.core.servlets;

import com.day.cq.tagging.JcrTagManagerFactory;
import com.hero.core.eventhandlers.sling.jobs.MyJobCreator;
import com.hero.core.service.GroupCreateService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Component( service = Servlet.class,
         property = {"sling.servlet.paths=" + "/content/we-retail/us/en/exxx" })
public class HeroSlingServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    GroupCreateService groupCreateService;

    @Reference
    JcrTagManagerFactory jcrTagManagerFactory;

    @Reference
    private MyJobCreator myJobCreator;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
//        String result = groupCreateService.createGroup("banana");
//        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
//        Resource resource = request.getResourceResolver().getResource("/content/Hero/en");
//        String res = "";
//        if (tagManager != null) {
//            Tag tag = tagManager.resolve("/content/cq:tags/mertag/ztag");
//            try {
//                Tag tag1 = tagManager.createTag("/content/cq:tags/mertag/qtag", "qtag", "qtag");
//                if (tag1 != null){
//                    res = tag1.getTagID() +" ";
//                }
//            } catch (InvalidTagFormatException e) {
//                e.printStackTrace();
//            }
//            if (tag != null){
//                res += tag.getTagID() + " ";
//            }
//            if (resource != null) {
//                res += resource.getName();
//            }
//            tagManager.setTags(resource, new Tag[]{tag});
//        }
//        try {
//            WorkflowSession workflowSession =  request.getResourceResolver().adaptTo(WorkflowSession.class);
//            if(workflowSession != null) {
//                WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/aem-training");
//                WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", "/content/Hero/en");
//                workflowSession.startWorkflow(workflowModel, workflowData);
//            }
//        } catch (WorkflowException e) {
//            e.printStackTrace();
//        }
        myJobCreator.createJob();
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<h1>Test Servlet "+"</h1>");
        response.getWriter().close();
    }
}
