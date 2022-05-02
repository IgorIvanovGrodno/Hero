package com.hero.core.servlets.search;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
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

@Component(service = Servlet.class,
        property={
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/searchsuggestions"
//                "sling.servlet.resourceTypes="+ "Hero/components/content/helloworld",
//                "sling.servlet.extensions=" + "json",
//                "sling.servlet.selectors=" + SuggestionSearchServlet.DEFAULT_SELECTOR
        })
public class SuggestionSearchServlet extends SlingSafeMethodsServlet {

    /**
     * Selector to trigger the search servlet.
     */
    protected static final String DEFAULT_SELECTOR = "searchsuggestions";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        JSONArray searchResult = getSearchResult(session, getQuery(request));
        response.setContentType("application/json");
        response.getWriter().write(searchResult.toString());
    }

    private String getQuery(SlingHttpServletRequest request) {
        String searchKey = request.getRequestParameter("fulltext").getString();
        return "SELECT [rep:suggest()] FROM [nt:unstructured] WHERE SUGGEST('"
                + searchKey + "') OPTION(INDEX NAME [testIndex])";
    }

    private JSONArray getSearchResult(Session session, String stringQuery) {
        JSONArray searchResultArray = new JSONArray();

        if (session != null) {
            try {
                QueryManager queryManager = session.getWorkspace().getQueryManager();
                Query jcrQuery = queryManager.createQuery(stringQuery, Query.JCR_SQL2);
                QueryResult result = jcrQuery.execute();
                RowIterator rows = result.getRows();

                while (rows.hasNext()) {
                    searchResultArray.put(rows.nextRow().getValue("rep:suggest()").getString());
                }

            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }

        return searchResultArray;
    }
}
