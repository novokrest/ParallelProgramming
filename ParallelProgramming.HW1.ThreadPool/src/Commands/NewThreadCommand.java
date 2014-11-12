package Commands;

import ThreadPool.ThreadPool;


public class NewThreadCommand extends ThreadPoolCommand {
    public NewThreadCommand(ThreadPool threadPool) {
        super(threadPool);
    }

    @Override
    public Object execute() {
        myThreadPool.createThread();

        return null;
    }
}
