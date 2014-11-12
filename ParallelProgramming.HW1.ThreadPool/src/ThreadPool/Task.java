package ThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Logged {
    private static AtomicInteger taskId = new AtomicInteger(0);

    private final int myId;
    private final int myDuration;

    public Task(int duration) {
        myId = taskId.incrementAndGet();
        myDuration = duration;
    }

    public int getId() {
        return myId;
    }

    public Object execute() throws InterruptedException {
        log("started");
        Thread.sleep(myDuration);
        log("finished");
        return "DONE";
    }

    @Override
    public void log(String message) {
        System.out.println("Task with ID = " + taskId + ": " + message);
    }
}
