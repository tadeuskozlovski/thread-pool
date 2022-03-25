package learnthreadpool;

import java.util.function.Consumer;

public class GracefulShutdown {
    public void enable(ServiceRegister serviceRegister) throws NullPointerException {
        Consumer<ServiceRegister> stopServices = (servRegister) -> {
            //Shutting down every service in list
            for (TaskService ts : servRegister.getServiceList()) {
                ts.shutdown();
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopServices.accept(serviceRegister);
            }
        });
    }
}
