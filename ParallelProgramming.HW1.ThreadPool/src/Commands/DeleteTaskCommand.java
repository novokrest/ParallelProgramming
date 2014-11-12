package Commands;

import ThreadPool.ThreadPool;


public class DeleteTaskCommand extends ThreadPoolCommand {
    private final int myTaskID;

    public DeleteTaskCommand(ThreadPool threadPool, int taskID) {
        super(threadPool);
        myTaskID = taskID;
    }

    @Override
    public Object execute() {
        myThreadPool.interruptTask(myTaskID);
        return null;
    }
}
