package learnthreadpool;

import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            ThreadPoolQueue threadPoolQueue = new ThreadPoolQueue();
            threadPoolQueue.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
