package learnthreadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskConsumer implements TaskService{
    private final ScheduledExecutorService pullTasksScheduledExecutionService;
    private final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);
    private final BlockingQueue<String> tasksQueue;

    TaskConsumer(final BlockingQueue<String> tasksQueue, final int threadLimit) {
        this.tasksQueue = tasksQueue;
        pullTasksScheduledExecutionService = Executors.newScheduledThreadPool(threadLimit);
    }

    TaskConsumer(final BlockingQueue<String> tasksQueue) {
        this(tasksQueue, 5);
    }

    public void run(final long initialDelay, final long period) {
        // ScheduledExecutorService for pulling tasks from queue
        Runnable pullTask = () -> {
            String headTask = tasksQueue.poll();
            if (headTask != null)
                logger.info("Getting task: " + headTask);
        };

        pullTasksScheduledExecutionService.scheduleAtFixedRate(pullTask, 0, 150, TimeUnit.MILLISECONDS);
        logger.info("Task pulling service is started.");
    }

    public void run() {
        run(0, 150);
    }

    public void shutdown() {
        if (pullTasksScheduledExecutionService.isShutdown() || pullTasksScheduledExecutionService.isTerminated()) {
            logger.info("Pulling task service is already disabled.");
            return;
        }

        logger.info("Executing tasks in a queue...");
        while (!tasksQueue.isEmpty()) {
        }
        logger.info("All tasks are completed. Disabling task pulling service...");

        pullTasksScheduledExecutionService.shutdown();
        logger.info("Task pulling service is disabled.");
    }
}
