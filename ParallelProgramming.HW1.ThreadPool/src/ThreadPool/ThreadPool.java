package ThreadPool;

import java.util.*;

public class ThreadPool implements Logged {
    private final int myHotThreadCount;
    private final int myThreadTimeout;

    private final Queue<Task> myTaskQueue = new LinkedList<Task>();
    private final List<PoolThread> myThreads = new LinkedList<PoolThread>();
    private final Map<Integer, PoolThread> myTask2ExecutorMap = new HashMap<Integer, PoolThread>();

    public ThreadPool(int hotThreadCount, int threadTimeout) {
        myHotThreadCount = hotThreadCount;
        myThreadTimeout = threadTimeout;

        createHotThreads();
    }

    private void createHotThreads() {
        for (int i = 0; i < myHotThreadCount; i++) {
            PoolThread hotThread = new PoolThread(this, myTaskQueue, myThreadTimeout, true);
            myThreads.add(hotThread);
            hotThread.start();
        }
    }

    public void createThread() {
        PoolThread poolThread = new PoolThread(this, myTaskQueue, myThreadTimeout);
        synchronized (myThreads) {
            myThreads.add(poolThread);
        }
        poolThread.start();

        log("New thread was created");
    }

    public void removeThread(PoolThread thread) {
        synchronized (myThreads) {
            myThreads.remove(thread);
        }
    }

    public void execute(Task task) {
        synchronized (myTaskQueue) {
            myTaskQueue.offer(task);
            log("Task ID = " + task.getId() + " was added");
            myTaskQueue.notify();
        }
    }

    public void registerTaskExecutor(PoolThread executor, Task task) {
        synchronized (myTask2ExecutorMap) {
            myTask2ExecutorMap.put(task.getId(), executor);
        }
        log("Thread-" + executor.getId() + " was registered for executing Task ID = " + task.getId());
    }

    public void unregisterTaskExecutor(PoolThread executor, Task task) {
        synchronized (myTask2ExecutorMap) {
            myTask2ExecutorMap.remove(task.getId());
        }
        log("Thread-" + executor.getId() + " finished executing Task ID = " + task.getId());
    }

    public void setTaskResult(Task task, Object result) {
        System.out.println("Task-" + task.getId() + " has result: " + result);
    }

    public void interruptTask(int taskId) {
        synchronized (myTaskQueue) {
            for (Task task: myTaskQueue) {
                if (taskId == task.getId()) {
                    myTaskQueue.remove(task);
                    log("Task with ID = " + taskId + " was removed");
                    return;
                }
            }
        }

        synchronized (myTask2ExecutorMap) {
            if (myTask2ExecutorMap.containsKey(taskId)) {
                PoolThread executor = myTask2ExecutorMap.get(taskId);
                myTask2ExecutorMap.remove(taskId);
                executor.interrupt();
                log("Task with ID = " + taskId + " was removed");
                return;
            }
        }

        log("Task with ID = " + taskId + " doesn't exist");
    }

    public void stop() {
        for (PoolThread thread: myThreads) {
            thread.setIsHot(false);
            thread.interrupt();
        }
    }

    @Override
    public void log(String message) {
        System.out.println(message);
        System.out.flush();
    }
}
