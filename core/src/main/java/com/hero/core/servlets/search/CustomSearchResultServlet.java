package com.hero.core.servlets.search;

import com.adobe.cq.wcm.core.components.internal.servlets.SearchResultServlet;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.search.PredicateConverter;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.*;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hero.core.servlets.search.util.PageListItemImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONArray;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.jcr.RangeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.*;

@Component(
        service = {Servlet.class},
        property = {"sling.servlet.selectors=customsearchresults", "sling.servlet.resourceTypes=cq/Page", "sling.servlet.extensions=json", "sling.servlet.methods=GET"}
)
public class CustomSearchResultServlet extends SlingSafeMethodsServlet {
    protected static final String DEFAULT_SELECTOR = "customsearchresults";
    protected static final String PARAM_FULLTEXT = "fulltext";
    private static final String PARAM_RESULTS_OFFSET = "resultsOffset";
    private static final String PREDICATE_FULLTEXT = "fulltext";
    private static final String PREDICATE_TYPE = "type";
    private static final String PREDICATE_PATH = "path";
    private static final String NN_STRUCTURE = "structure";

    @Reference
    private QueryBuilder queryBuilder;
    @Reference
    private LanguageManager languageManager;
    @Reference
    private LiveRelationshipManager relationshipManager;

    public CustomSearchResultServlet() {
    }

    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response) throws IOException {
        Page currentPage = this.getCurrentPage(request);
        if (currentPage != null) {
            Resource searchResource = this.getSearchContentResource(request, currentPage);
            List<PageListItemImpl> results = this.getResults(request, searchResource, currentPage);
            this.writeJson(results, response);
        }

    }

    private Page getCurrentPage(SlingHttpServletRequest request) {
        Page currentPage = null;
        Resource currentResource = request.getResource();
        ResourceResolver resourceResolver = currentResource.getResourceResolver();
        PageManager pageManager = (PageManager)resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            currentPage = pageManager.getContainingPage(currentResource.getPath());
        }

        return currentPage;
    }

    private void writeJson(List<PageListItemImpl> results, SlingHttpServletResponse response) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Resource getSearchContentResource(SlingHttpServletRequest request, Page currentPage) {
        Resource searchContentResource = null;
        RequestPathInfo requestPathInfo = request.getRequestPathInfo();
        Resource resource = request.getResource();
        String relativeContentResource = requestPathInfo.getSuffix();
        if (StringUtils.startsWith(relativeContentResource, "/")) {
            relativeContentResource = StringUtils.substring(relativeContentResource, 1);
        }

        if (StringUtils.isNotEmpty(relativeContentResource)) {
            searchContentResource = resource.getChild(relativeContentResource);
            if (searchContentResource == null) {
                PageManager pageManager = (PageManager)resource.getResourceResolver().adaptTo(PageManager.class);
                if (pageManager != null) {
                    Template template = currentPage.getTemplate();
                    if (template != null) {
                        Resource templateResource = request.getResourceResolver().getResource(template.getPath());
                        if (templateResource != null) {
                            searchContentResource = templateResource.getChild("structure/" + relativeContentResource);
                        }
                    }
                }
            }
        }

        return searchContentResource;
    }

    private List<PageListItemImpl> getResults(SlingHttpServletRequest request, Resource searchResource, Page currentPage) {
        int searchTermMinimumLength = 3;
        int resultsSize = 10;
        String searchRootPagePath;
        if (searchResource != null) {
            ValueMap valueMap = searchResource.getValueMap();
            ValueMap contentPolicyMap = this.getContentPolicyProperties(searchResource, request.getResource());
            searchTermMinimumLength = (Integer)valueMap.get("searchTermMinimumLength", contentPolicyMap.get("searchTermMinimumLength", 3));
            resultsSize = (Integer)valueMap.get("resultsSize", contentPolicyMap.get("resultsSize", 10));
            String searchRoot = (String)searchResource.getValueMap().get("searchRoot", contentPolicyMap.get("searchRoot", String.class));
            searchRootPagePath = this.getSearchRootPagePath(searchRoot, currentPage);
        } else {
            String languageRoot = this.languageManager.getLanguageRoot(currentPage.getContentResource()).getPath();
            searchRootPagePath = this.getSearchRootPagePath(languageRoot, currentPage);
        }

        if (StringUtils.isEmpty(searchRootPagePath)) {
            searchRootPagePath = currentPage.getPath();
        }

        List<PageListItemImpl> results = new ArrayList<>();
        String fulltext = request.getParameter("fulltext");
        String topics = request.getParameter("topics");
        if (fulltext != null && fulltext.length() >= searchTermMinimumLength) {
            long resultsOffset = 0L;
            if (request.getParameter("resultsOffset") != null) {
                resultsOffset = Long.parseLong(request.getParameter("resultsOffset"));
            }

            Map<String, String> predicatesMap = new HashMap();
            predicatesMap.put("path", searchRootPagePath);
            predicatesMap.put("type", "cq:Page");
            predicatesMap.put("group.p.and", "true");
            predicatesMap.put("group.1_group.fulltext", fulltext);
            if (topics != null && !topics.isEmpty()) {
                predicatesMap.put("group.2_property", "jcr:content/@value");
                predicatesMap.put("group.2_property.value", topics);
            }

            PredicateGroup predicates = PredicateConverter.createPredicates(predicatesMap);
            ResourceResolver resourceResolver = request.getResource().getResourceResolver();
            Query query = this.queryBuilder.createQuery(predicates, (Session)resourceResolver.adaptTo(Session.class));
            if (resultsSize != 0) {
                query.setHitsPerPage((long)resultsSize);
            }

            if (resultsOffset != 0L) {
                query.setStart(resultsOffset);
            }

            SearchResult searchResult = query.getResult();
//            Iterator<Resource> iteratorResource = searchResult.getResources();
//            while (iteratorResource.hasNext()) {
//                results.put(iteratorResource.next());
//            }
            List<Hit> hits = searchResult.getHits();
            if (hits != null) {
                Iterator var17 = hits.iterator();

                while(var17.hasNext()) {
                    Hit hit = (Hit)var17.next();

                    try {
                        Resource hitRes = hit.getResource();
                        Page page = this.getPage(hitRes);
                        if (page != null) {
                            PageListItemImpl v = new PageListItemImpl(request, page);
                            results.add(v);
                        }
                    } catch (RepositoryException var21) {
                        int var = 1;
                    }
                }
            }

            return results;
        } else {
            return results;
        }
    }

    private String getSearchRootPagePath(String searchRoot, Page currentPage) {
        String searchRootPagePath = null;
        PageManager pageManager = currentPage.getPageManager();
        if (StringUtils.isNotEmpty(searchRoot) && pageManager != null) {
            Page rootPage = pageManager.getPage(searchRoot);
            if (rootPage != null) {
                Page searchRootLanguageRoot = this.languageManager.getLanguageRoot(rootPage.getContentResource());
                Page currentPageLanguageRoot = this.languageManager.getLanguageRoot(currentPage.getContentResource());
                RangeIterator liveCopiesIterator = null;

                try {
                    liveCopiesIterator = this.relationshipManager.getLiveRelationships((Resource)currentPage.adaptTo(Resource.class), (String)null, (RolloutManager.Trigger)null);
                } catch (WCMException var11) {
                }

                if (searchRootLanguageRoot != null && currentPageLanguageRoot != null && !searchRootLanguageRoot.equals(currentPageLanguageRoot)) {
                    Page languageCopySearchRoot = pageManager.getPage(ResourceUtil.normalize(currentPageLanguageRoot.getPath() + "/" + this.getRelativePath(searchRootLanguageRoot, rootPage)));
                    if (languageCopySearchRoot != null) {
                        rootPage = languageCopySearchRoot;
                    }
                } else if (liveCopiesIterator != null) {
                    while(liveCopiesIterator.hasNext()) {
                        LiveRelationship relationship = (LiveRelationship)liveCopiesIterator.next();
                        if (currentPage.getPath().startsWith(relationship.getTargetPath() + "/")) {
                            Page liveCopySearchRoot = pageManager.getPage(relationship.getTargetPath());
                            if (liveCopySearchRoot != null) {
                                rootPage = liveCopySearchRoot;
                                break;
                            }
                        }
                    }
                }

                searchRootPagePath = rootPage.getPath();
            }
        }

        return searchRootPagePath;
    }

    private ValueMap getContentPolicyProperties(Resource searchResource, Resource requestedResource) {
        ValueMap contentPolicyProperties = new ValueMapDecorator(new HashMap());
        ResourceResolver resourceResolver = searchResource.getResourceResolver();
        ContentPolicyManager contentPolicyManager = (ContentPolicyManager)resourceResolver.adaptTo(ContentPolicyManager.class);
        if (contentPolicyManager != null) {
            ContentPolicy policy = contentPolicyManager.getPolicy(searchResource);
            if (policy != null) {
                contentPolicyProperties = policy.getProperties();
            }
        }

        return (ValueMap)contentPolicyProperties;
    }

    @CheckForNull
    private String getRelativePath(@Nonnull Page root, @Nonnull Page child) {
        if (child.equals(root)) {
            return ".";
        } else {
            return (child.getPath() + "/").startsWith(root.getPath()) ? child.getPath().substring(root.getPath().length() + 1) : null;
        }
    }

    private Page getPage(Resource resource) {
        if (resource != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = (PageManager)resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                return pageManager.getContainingPage(resource);
            }
        }

        return null;
    }
}

