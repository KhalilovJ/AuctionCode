package az.code.auctionbackend.services;

import org.springframework.stereotype.Service;

@Service
public class TaskDefinitionBean implements Runnable {


    @Override
    public void run() {
        System.out.println("LOL");
    }



//    @Autowired
//    private TaskScheduler taskScheduler;
//
//    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
//
//    public void scheduleATask(String jobId, Runnable tasklet, String cronExpression) {
//        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
//        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet,
//                new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
//        jobsMap.put(jobId, scheduledTask);
//    }
//
//    public void removeScheduledTask(String jobId) {
//        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
//        if(scheduledTask != null) {
//            scheduledTask.cancel(true);
//            jobsMap.put(jobId, null);
//        }
//    }

}
