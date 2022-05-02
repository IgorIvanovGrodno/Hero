//package com.hero.core.eventhandlers.schedule;
//
//import org.osgi.service.component.annotations.Component;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Component(immediate = true, service = Runnable.class, property = "scheduler.expression=0 * * * * ?")
//public class MyScheduleJob implements Runnable{
//    private static final Logger LOGGER = LoggerFactory.getLogger(MyScheduleJob.class);
//
//    @Override
//    public void run() {
//        LOGGER.info("My job");
//    }
//}
