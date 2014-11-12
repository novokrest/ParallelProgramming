package Commands;

import ThreadPool.ThreadPool;
import ThreadPool.Task;

public class CreateTaskCommand extends ThreadPoolCommand {
    private final int myTaskDuration;

    public CreateTaskCommand(ThreadPool threadPool, int taskDuration) {
        super(threadPool);
        myTaskDuration = taskDuration;
    }

    @Override
    public Object execute() {
        Task task = new Task(myTaskDuration);
        myThreadPool.execute(task);

        return task.getId();
    }
}
