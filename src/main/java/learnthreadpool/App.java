package learnthreadpool;

import java.util.concurrent.*;
import java.util.function.Consumer;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final BlockingQueue<String> tasksQueue = new LinkedBlockingQueue();
    private static final Logger logger = LoggerFactory.getLogger(TaskProducer.class);

    private static Consumer<ServiceRegister> stopServices = (serviceRegister) -> {
        //Shutting down every service in list
        for (TaskService ts : serviceRegister.getServiceList()) {
            ts.shutdown();
        }
    };

    public static void makeGracefulShutdown(final ServiceRegister serviceRegister) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopServices.accept(serviceRegister);
            }
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            // log4j basic configuration
            BasicConfigurator.configure();

            ServiceRegister serviceRegister = new ServiceRegister();

            TaskService taskProducer = new TaskProducer(tasksQueue);
            serviceRegister.registerService(taskProducer);
            taskProducer.run();

            TaskService taskConsumer = new TaskConsumer(tasksQueue);
            serviceRegister.registerService(taskConsumer);
            taskConsumer.run();

            makeGracefulShutdown(serviceRegister);

        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

            for (StackTraceElement stElem : stackTrace) {
                logger.error(stElem.toString());
            }
        }
    }
}
