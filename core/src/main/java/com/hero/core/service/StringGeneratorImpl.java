package com.hero.core.service;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import java.util.Dictionary;

@Component(immediate = true, service = StringGenerator.class)
@Designate(ocd = StringGeneratorConfig.class)
public class StringGeneratorImpl implements StringGenerator{

    private boolean checkbox;
    private String extraText;

    @Activate
    @Modified
    public void activate(StringGeneratorConfig stringGeneratorConfig, final ComponentContext context) {
        final Dictionary<String, Object> properties = context.getProperties();

        this.checkbox = stringGeneratorConfig.getCheckboxValue();
        this.extraText = String.valueOf(properties.get("extraText"));
    }

    @Override
    public String generateString() {
        return this.checkbox ? "String generator " + this.extraText : "Another value " + this.extraText;
    }
}
