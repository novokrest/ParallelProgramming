/**
 * Created by novokrest on 11.11.14.
 */
public class Test {

    public static class Mutex {
        private boolean flag = false;

        public synchronized void get() {
            if (flag) {
                notify();
                return;
            }
            try {
                flag = true;
                wait();
                System.out.println("after wait");
            } catch (InterruptedException e) {
                System.out.println("Inside interruption");
            }
        }
    }

    public static void test() {
        final Mutex mutex = new Mutex();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }
        };

        thread1.start();
        while(true) {
            System.out.println(thread1.isAlive());
        }
    }

    public static void main(String[] args) {
        test();
    }
}
