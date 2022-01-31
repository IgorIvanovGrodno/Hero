package apps.Hero.components.content.helloworld;

import com.adobe.cq.sightly.WCMUsePojo;

import java.util.HashMap;
import java.util.Map;

public class Settings extends WCMUsePojo {

    // used to pass is requestAttributes to data-sly-resource
    public Map<String, Object> settings = new HashMap<String, Object>();

    @Override
    public void activate() throws Exception {
        settings.put("layout", "flex");
    }
}