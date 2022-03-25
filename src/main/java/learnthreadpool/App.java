package learnthreadpool;

import java.util.concurrent.*;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final BlockingQueue<String> tasksQueue = new LinkedBlockingQueue();
    private static final Logger logger = LoggerFactory.getLogger(TaskProducer.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            // log4j basic configuration
            BasicConfigurator.configure();

            TaskService taskProducer = new TaskProducer(tasksQueue);
            ServiceRegister.INSTANCE.registerService(taskProducer);
            taskProducer.run();

            TaskService taskConsumer = new TaskConsumer(tasksQueue);
            ServiceRegister.INSTANCE.registerService(taskConsumer);
            taskConsumer.run();

            //Disabling all registered services
            new GracefulShutdown().enable(ServiceRegister.INSTANCE);
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

            for (StackTraceElement stElem : stackTrace) {
                logger.error(stElem.toString());
            }
        }
    }
}
