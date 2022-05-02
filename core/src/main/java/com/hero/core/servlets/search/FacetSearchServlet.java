package com.hero.core.servlets.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jackrabbit.oak.query.facet.FacetResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.osgi.service.component.annotations.Component;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = Servlet.class,
        property={
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/searchfacets"
        })
public class FacetSearchServlet extends SlingSafeMethodsServlet {

    protected static final String QUERY_SEARCH_FACET = "select [rep:facet(value)] from [nt:unstructured] WHERE ISDESCENDANTNODE('/content/Hero') and [value] IS NOT NULL";
    protected static final String QUERY_SEARCH_SPELLCHECKING = "select [rep:spellcheck()] from [nt:unstructured] WHERE ISDESCENDANTNODE('/content/Hero') and SPELLCHECK('Helo node1')";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        List<FacetResult.Facet> searchFacetResult = getSearchFacetResult(session);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), searchFacetResult);
    }

    private List<FacetResult.Facet> getSearchFacetResult(Session session) {
        List<FacetResult.Facet> searchFacetResultArray = new ArrayList<>();
        if (session != null) {
            try {
                QueryManager queryManager = session.getWorkspace().getQueryManager();
                Query query = queryManager.createQuery(QUERY_SEARCH_FACET, Query.JCR_SQL2);
                QueryResult result = query.execute();

                FacetResult facetResult = new FacetResult(result);
                List<FacetResult.Facet> facets = facetResult.getFacets("value");
                if (facets != null) {
                    searchFacetResultArray.addAll(facets);
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }

        return searchFacetResultArray;
    }


}
