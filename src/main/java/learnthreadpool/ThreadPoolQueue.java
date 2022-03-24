package learnthreadpool;

import java.util.concurrent.*;

public class ThreadPoolQueue {
    // BlockingQueue data type will be necessarily for the next releases
    private final BlockingQueue<String> tasksQueue = new LinkedBlockingQueue();
    private long taskCounter = 0;
    private final ScheduledExecutorService pushTasksScheduledExecutionService = Executors.newScheduledThreadPool(5);
    private final ScheduledExecutorService pullTasksScheduledExecutionService = Executors.newScheduledThreadPool(5);

    ThreadPoolQueue() {
        makeGracefulShutdown();
    }

    public boolean isTaskQueueEmpty() {
        return tasksQueue.isEmpty();
    }

    public synchronized void pushTasks() {
        // ScheduledExecutorService for pushing tasks into queue
        Runnable pushTask = () -> {
            System.out.println("Pushing task: " + taskCounter);
            try {
                this.tasksQueue.put(taskCounter + "");
                taskCounter++;

                //System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        pushTasksScheduledExecutionService.scheduleAtFixedRate(pushTask, 0, 100, TimeUnit.MILLISECONDS);
    }

    public synchronized void pullTasks() {
        // ScheduledExecutorService for pulling tasks from queue
        Runnable pullTask = () -> {
            String headTask = tasksQueue.poll();
            if (headTask != null) System.out.println("Getting task: " + headTask);
        };

        pullTasksScheduledExecutionService.scheduleAtFixedRate(pullTask, 0, 150, TimeUnit.MILLISECONDS);
    }

    public void makeGracefulShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting down pushing tasks service...");
                pushTasksScheduledExecutionService.shutdown();

                System.out.println("Executing tasks in a queue...");
                //I don't find other solution in this case
                while (!isTaskQueueEmpty()) {
                }

                System.out.println("All tasks are completed.");
                System.out.println("Shutting down pulling tasks service...");
                pullTasksScheduledExecutionService.shutdown();
            }
        });
    }

    public void start() {
        pushTasks();
        pullTasks();
    }
}
