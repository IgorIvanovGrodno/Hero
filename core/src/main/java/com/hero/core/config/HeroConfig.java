package com.hero.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Hero test context-aware configuration")
public @interface HeroConfig {

    @Property(label = "Hero site locale")
    String siteLocale() default "default";

}
