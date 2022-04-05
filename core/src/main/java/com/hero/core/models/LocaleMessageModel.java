package com.hero.core.models;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;

@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LocaleMessageModel {

    @Inject
    Page currentPage;

    @Self
    private SlingHttpServletRequest slingRequest;

    private String val;

    @PostConstruct
    public void init() {
        val = "Fail";
        if (currentPage != null) {
            Locale pageLang = currentPage.getLanguage(false);
            ResourceBundle resourceBundle = slingRequest.getResourceBundle(pageLang);
            I18n i18n = new I18n(resourceBundle);
            val = i18n.get("Java");
        }
    }

    public String getVal() {
        return val;
    }
}
