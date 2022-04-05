package com.hero.core.eventhandlers.sling.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.apache.sling.event.jobs.consumer.JobExecutor;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = JobExecutor.class, property = JobConsumer.PROPERTY_TOPICS + "=com/hero/my/job")
public class MySlingJobExecutor implements JobExecutor {

    @Override
    public JobExecutionResult process(Job job, JobExecutionContext jobExecutionContext) {
        //process the job and return the result
        //initialize job progress with n number of steps
        int n = -1;
        String resultMessage = "All right";
        jobExecutionContext.initProgress(n, -1);
        jobExecutionContext.log("Job initialized");

        //increament progress by 2 steps
        jobExecutionContext.incrementProgressCount(2);
        jobExecutionContext.log("2 steps completed.");

        //stop processing if job was cancelled
        if(jobExecutionContext.isStopped()) {
            jobExecutionContext.log("Job Stopped after 4 steps.");
            return jobExecutionContext.result().message(resultMessage).cancelled();
        }

        //add job log
        jobExecutionContext.log("Job finished.");

        return jobExecutionContext.result().message(resultMessage).succeeded();
    }
}
