package Commands;

import ThreadPool.ThreadPool;


public class CommandParser {
    public static Command parseCommand(ThreadPool threadPool, String[] args) throws IncorrectCommandException {
        Command command = parseCreateTaskCommand(threadPool, args);
        if (command != null) {
            return command;
        }

        command = parseDeleteTaskCommand(threadPool, args);
        if (command != null) {
            return command;
        }

        command = parseNewThreadCommand(threadPool, args);
        if (command != null) {
            return command;
        }

        throw new IncorrectCommandException();
    }

    private static Command parseCreateTaskCommand(ThreadPool threadPool, String[] args) {
        if (args.length == 2 && args[0].equals("task")) {
            int taskDuration = Integer.parseInt(args[1]) * 1000;
            return new CreateTaskCommand(threadPool, taskDuration);
        }
        return null;
    }

    private static Command parseDeleteTaskCommand(ThreadPool threadPool, String[] args) {
        if (args.length == 2 && args[0].equals("delete")) {
            int taskId = Integer.parseInt(args[1]);
            return new DeleteTaskCommand(threadPool, taskId);
        }
        return null;
    }

    private static Command parseNewThreadCommand(ThreadPool threadPool, String[] args) {
        if (args.length == 2 && args[0].equals("new") && args[1].equals("thread")) {
            return new NewThreadCommand(threadPool);
        }
        return null;
    }
}
