package Commands;

import ThreadPool.ThreadPool;


public abstract class ThreadPoolCommand implements Command {
    protected final ThreadPool myThreadPool;

    public ThreadPoolCommand(ThreadPool threadPool) {
        myThreadPool = threadPool;
    }

    @Override
    public abstract Object execute();
}
