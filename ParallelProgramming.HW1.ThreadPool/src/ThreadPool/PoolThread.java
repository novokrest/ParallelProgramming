package ThreadPool;

import java.util.Date;
import java.util.Queue;

public class PoolThread extends Thread implements Logged {
    private final ThreadPool myThreadPool;
    private final Queue<Task> myTaskQueue;
    private final long myTimeout;
    private boolean myIsHot;
    private boolean myIsStopped = false;

    public PoolThread(ThreadPool threadPool, Queue<Task> taskQueue, long timeout, boolean isHot) {
        myThreadPool = threadPool;
        myTaskQueue = taskQueue;
        myTimeout = timeout;
        myIsHot = isHot;
    }

    public PoolThread(ThreadPool threadPool, Queue<Task> taskQueue, long timeout) {
        this(threadPool, taskQueue, timeout, false);
    }

    public void setIsHot(boolean isHot) {
        myIsHot = isHot;
    }

    public boolean isHot() {
        return myIsHot;
    }

    public void setIsStopped(boolean isStopped) {
        myIsStopped = isStopped;
    }

    public boolean isStopped() {
        return myIsStopped;
    }

    private Task getTask() throws InterruptedException {
        synchronized (myTaskQueue) {
            Date start = new Date();
            Date end;
            long timeoutRest = myTimeout;
            while (myTaskQueue.isEmpty()) {
               myTaskQueue.wait(timeoutRest);
                if (!this.isHot()) {
                    end = new Date(); //weak place
                    long sleepingTime = (end.getTime() - start.getTime()) / 1000;
                    if (sleepingTime >= myTimeout) {
                        return null;
                    }
                    timeoutRest = myTimeout - sleepingTime;
                }
            }
            return myTaskQueue.poll();
        }
    }

    @Override
    public void run() {
        log("started");

        while (!isStopped()) {
            try {
                Task task = getTask();
                if (task == null) {
                    break;
                }
                myThreadPool.registerTaskExecutor(this, task);
                Object result = task.execute();
                myThreadPool.unregisterTaskExecutor(this, task);
                myThreadPool.setTaskResult(task, result);
            }
            catch (InterruptedException e) {
                if (!this.isHot()) {
                    setIsStopped(true);
                }
            }
        }

        myThreadPool.removeThread(this);
        log("finished");
    }

    @Override
    public void log(String message) {
        System.out.println("Thread-" + Thread.currentThread().getId() + ": " + message);
        System.out.flush();
    }
}
