import java.util.concurrent.locks.ReentrantLock;

class DopeThread implements Runnable 
{
    private ReentrantLock lock = new ReentrantLock();

    public void run() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("Narith sucks");
    }
}
