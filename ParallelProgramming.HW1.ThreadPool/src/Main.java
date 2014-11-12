import Commands.CommandParser;
import ThreadPool.ThreadPool;
import Commands.Command;
import Commands.IncorrectCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Few arguments!");
            return;
        }

        int hotThreadCount = Integer.parseInt(args[0]);
        int threadTimeout = Integer.parseInt(args[1]) * 1000;

        final ThreadPool threadPool = new ThreadPool(hotThreadCount, threadTimeout);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                threadPool.stop();
            }
        });

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String input = null;
            try {
                input = consoleReader.readLine();
                if (input.equals("exit")) {
                    threadPool.stop();
                    break;
                }
                Command command = CommandParser.parseCommand(threadPool, input.split(" "));
                command.execute();
            } catch (IOException e) {
                System.out.println("Strange IO exception occurred during reading from console..." + "\n");
            } catch (IncorrectCommandException e) {
                System.out.println("Couldn't parse command: " + input + "\n");
            }
        }
    }
}
