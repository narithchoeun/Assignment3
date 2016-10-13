import java.util.concurrent.locks.ReentrantLock;

class DopeThread implements Runnable 
{
    private static ReentrantLock lock = new ReentrantLock();

    public void run() {
        // try {
        //     Thread.sleep(1);
        // } catch (InterruptedException e) {
        //     System.out.println(e);
        // }

        // System.out.println("Narith sucks");
        runOperation();
    }

    public static void runOperation() {
        int num = Critical.randomLength(0, 2500);

        if (num < 1900) {
            System.out.println(occurences());
        } else {
            replace();
        }
    }

    public static int occurences() {
        String randomString = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        int count = 0;

        for(int i = 0; i < Critical.array.length; i++) {
        	if (randomString.equals(Critical.array[i])) {
        		lock.lock();

        		try {
	        		count++;
	        	}  catch (Exception e) {
		            e.printStackTrace();
	         	} finally {
	            	lock.unlock();
	            }
        	}
        }

        return count;
    }

    public static void replace() {
        String randomOne = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        String randomTwo = Critical.pool[Critical.randomLength(0,Critical.poolSize)];

        for(int i = 0; i < Critical.array.length; i++) {
            if (randomOne.equals(Critical.array[i])){
            	lock.lock();

            	try {
            		Critical.array[i] = randomTwo;
            	} catch (Exception e) {
            		e.printStackTrace();
            	} finally {
            		lock.unlock();
            	}
            }
        }
    }
}
