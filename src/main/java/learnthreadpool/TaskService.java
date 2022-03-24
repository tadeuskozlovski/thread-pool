package learnthreadpool;

public interface TaskService{
    public void run(final long initialDelay, final long period);

    public void run();

    public void shutdown();
}
