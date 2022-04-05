package com.hero.core.service;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import static org.osgi.service.metatype.annotations.AttributeType.BOOLEAN;

@ObjectClassDefinition
public @interface StringGeneratorConfig {
    @AttributeDefinition(name = "checkbox", description = "This is a description", type = BOOLEAN)
    boolean getCheckboxValue() default false;

    @AttributeDefinition(name = "extraText", description = "This is an extra text", type = AttributeType.STRING)
    String getExtraText();
}
