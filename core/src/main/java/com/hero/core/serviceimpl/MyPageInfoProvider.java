package com.hero.core.serviceimpl;

import com.day.cq.wcm.api.PageInfoProvider;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = PageInfoProvider.class)
public class MyPageInfoProvider implements PageInfoProvider {
    @Override
    public void updatePageInfo(SlingHttpServletRequest slingHttpServletRequest, JSONObject jsonObject, Resource resource) throws JSONException {
        JSONObject urlinfo = new JSONObject();
        urlinfo.put("publishURL", "http://hahha");
        jsonObject.put("URLs",urlinfo);
    }
}
