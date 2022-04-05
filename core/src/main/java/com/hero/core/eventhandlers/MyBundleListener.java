package com.hero.core.eventhandlers;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class MyBundleListener implements BundleListener {
    @Override
    public void bundleChanged(BundleEvent bundleEvent) {
        int s = bundleEvent.getType();
    }
}
