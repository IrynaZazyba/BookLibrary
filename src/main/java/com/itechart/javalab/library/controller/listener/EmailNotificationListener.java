package com.itechart.javalab.library.controller.listener;

import com.itechart.javalab.library.service.util.notification.DelayRemainder;
import com.itechart.javalab.library.service.util.notification.ReturnRemainder;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Log4j2
public class EmailNotificationListener implements ServletContextListener {

    private Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("path", sce.getServletContext().getRealPath("resources/template"));

            JobDetail returnNotification = getJobDetail(jobDataMap, ReturnRemainder.class, "return-notification");
            SimpleTrigger returnTrigger = getSimpleTrigger("return-trigger");

            JobDetail delayNotification = getJobDetail(jobDataMap, DelayRemainder.class, "delay-notification");
            SimpleTrigger delayTrigger = getSimpleTrigger("delay-trigger");

            scheduler.scheduleJob(returnNotification, returnTrigger);
            scheduler.scheduleJob(delayNotification, delayTrigger);
        } catch (SchedulerException e) {
            log.error("SchedulerException in attempt to create Scheduler");
            throw new RuntimeException("SchedulerException in attempt to create Scheduler", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error("SchedulerException in attempt to shutdown Scheduler");
            throw new RuntimeException("SchedulerException in attempt to shutdown Scheduler", e);
        }
    }

    private SimpleTrigger getSimpleTrigger(String triggerName) {
        return newTrigger().withIdentity(triggerName)
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInHours(24).repeatForever())
                .build();
    }

    @SuppressWarnings("unchecked")
    private JobDetail getJobDetail(JobDataMap jobDataMap, Class<?> jobClass, String identity) {
        return newJob((Class<? extends Job>) jobClass)
                .usingJobData(jobDataMap)
                .withIdentity(identity)
                .build();

    }
}
