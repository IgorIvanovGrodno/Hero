package com.hero.core.servlets;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component( service = Servlet.class,
        property = {"sling.servlet.paths=" + "/bin/testworkflow" })
public class WorkflowServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String workflowStatus = "Error...";
        ResourceResolver resourceResolver = request.getResourceResolver();
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
        if (workflowSession != null) {
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH","/content/Hero/new-workflow-page-2");
            WorkflowModel workflowModel = null;
            try {
                workflowModel = workflowSession.getModel("/var/workflow/models/aem-new-author-model");
                workflowSession.startWorkflow(workflowModel, workflowData);
                workflowStatus = "Workflow is executing...";
            } catch (WorkflowException e) {
                e.printStackTrace();
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(workflowStatus);
    }
}
