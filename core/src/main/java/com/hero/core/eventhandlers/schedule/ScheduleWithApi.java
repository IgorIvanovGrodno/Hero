package com.hero.core.eventhandlers.schedule;

import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = ScheduleWithApi.class)
public class ScheduleWithApi {
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /** The scheduler for rescheduling jobs. */
    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate(ComponentContext componentContext) throws Exception {
        String schedulingExpression = "0 * * * * ?";
        Runnable job = new Runnable() {
            @Override
            public void run() {
                log.info("Scheduler API");
            }
        };
        scheduler.addJob("myJob", job, null, schedulingExpression, true);
    }
}
