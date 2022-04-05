package com.hero.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables= Resource.class,
        adapters = {ComponentExporter.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HelloWorldModel.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
@JsonSerialize(as = HelloWorldModel.class)
public class HelloWorldModel implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "Hero/components/content/helloworld";

    @Inject
    @Optional
    @Named("colorpicker")
    public String text;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
