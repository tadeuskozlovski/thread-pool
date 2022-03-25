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

            //Every service is registered in ServiceRegister
            ServiceRegister serviceRegister = new ServiceRegister();

            TaskService taskProducer = new TaskProducer(tasksQueue);
            serviceRegister.registerService(taskProducer);
            taskProducer.run();

            TaskService taskConsumer = new TaskConsumer(tasksQueue);
            serviceRegister.registerService(taskConsumer);
            taskConsumer.run();

            //Disabling all registered services
            new GracefulShutdown().enable(serviceRegister);
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

            for (StackTraceElement stElem : stackTrace) {
                logger.error(stElem.toString());
            }
        }
    }
}
