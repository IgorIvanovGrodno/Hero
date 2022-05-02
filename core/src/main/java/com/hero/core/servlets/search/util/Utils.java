package com.hero.core.servlets.search.util;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

public class Utils {
    private Utils() {
    }

    @Nonnull
    public static String getURL(@Nonnull SlingHttpServletRequest request, @Nonnull PageManager pageManager, @Nonnull String path) {
        Page page = pageManager.getPage(path);
        return page != null ? getURL(request, page) : path;
    }

    @Nonnull
    public static String getURL(@Nonnull SlingHttpServletRequest request, @Nonnull Page page) {
        String vanityURL = page.getVanityUrl();
        return StringUtils.isEmpty(vanityURL) ? request.getContextPath() + page.getPath() + ".html" : request.getContextPath() + vanityURL;
    }
}

