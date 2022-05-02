package com.hero.core.servlets.search.util;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageListItemImpl {
    transient protected SlingHttpServletRequest request;
    transient protected Page page;

    public PageListItemImpl(@Nonnull SlingHttpServletRequest request, @Nonnull Page page) {
        this.request = request;
        this.page = page;
        Page redirectTarget = this.getRedirectTarget(page);
        if (redirectTarget != null && !redirectTarget.equals(page)) {
            this.page = redirectTarget;
        }

    }

    public String getURL() {
        return Utils.getURL(this.request, this.page);
    }

    public String getTitle() {
        String title = this.page.getNavigationTitle();
        if (title == null) {
            title = this.page.getPageTitle();
        }

        if (title == null) {
            title = this.page.getTitle();
        }

        if (title == null) {
            title = this.page.getName();
        }

        return title;
    }

//    public String getDescription() {
//        return this.page.getDescription();
//    }

//    public Calendar getLastModified() {
//        return this.page.getLastModified();
//    }

//    public String getPath() {
//        return this.page.getPath();
//    }

    private Page getRedirectTarget(@Nonnull Page page) {
        Page result = page;
        PageManager pageManager = page.getPageManager();
        Set<String> redirectCandidates = new LinkedHashSet();
        redirectCandidates.add(page.getPath());

        String redirectTarget;
        while(result != null && StringUtils.isNotEmpty(redirectTarget = (String)result.getProperties().get("cq:redirectTarget", String.class))) {
            result = pageManager.getPage(redirectTarget);
            if (result != null && !redirectCandidates.add(result.getPath())) {
//                LOGGER.warn("Detected redirect loop for the following pages: {}.", redirectCandidates.toString());
                break;
            }
        }

        return result;
    }
}

