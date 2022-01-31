package com.hero.core.eventhandlers;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public class MyFrameworkListener implements FrameworkListener {

    @Override
    public void frameworkEvent(FrameworkEvent frameworkEvent) {
        if (frameworkEvent.getType() == FrameworkEvent.PACKAGES_REFRESHED) {
            Bundle bundle = frameworkEvent.getBundle();
        }
    }
}
