package learnthreadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TaskProducer implements TaskService {
    private AtomicLong taskCounter = new AtomicLong();
    private final ScheduledExecutorService pushTasksScheduledExecutionService;
    private final Logger logger = LoggerFactory.getLogger(TaskProducer.class);
    private final BlockingQueue<String> tasksQueue;

    TaskProducer(final BlockingQueue<String> tasksQueue, final int threadLimit) {
        this.tasksQueue = tasksQueue;
        pushTasksScheduledExecutionService = Executors.newScheduledThreadPool(threadLimit);
    }

    TaskProducer(final BlockingQueue<String> tasksQueue) {
        this(tasksQueue, 5);
    }

    public void run(final long initialDelay, final long period) {
        // ScheduledExecutorService for pushing tasks into queue
        Runnable pushTask = () -> {
            logger.info("Pushing task: " + taskCounter);
            try {
                tasksQueue.put(taskCounter + "");
                taskCounter.incrementAndGet();
            } catch (InterruptedException e) {
                StackTraceElement[] stackTrace = e.getStackTrace();

                for (StackTraceElement stElem : stackTrace) {
                    logger.error(stElem.toString());
                }
            }
        };
        pushTasksScheduledExecutionService.scheduleAtFixedRate(pushTask, initialDelay, period, TimeUnit.MILLISECONDS);
        logger.info("Task pushing service is started.");
    }

    public void run() {
        run(0, 100);
    }

    public void shutdown() {
        if (pushTasksScheduledExecutionService.isShutdown() || pushTasksScheduledExecutionService.isTerminated()) {
            logger.info("Pushing task service is already disabled.");
            return;
        }

        logger.info("Disabling task pushing service...");
        pushTasksScheduledExecutionService.shutdown();
        logger.info("Task pushing service is disabled.");
    }
}
