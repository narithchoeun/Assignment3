import java.util.concurrent.locks.ReentrantLock;

class DopeThread implements Runnable 
{
    private static ReentrantLock lock = new ReentrantLock();
    private int operationCount = 1;
    
    public void run() {
    	for(int i = 0; i < operationCount; i++){
        runOperation();
    	}
    }

    public static void runOperation() {
        int num = Critical.randomLength(0, 2500);

        if (num < 1900) {
          System.out.println(Thread.currentThread().getName() + " finding occurences.");
        	if (occurences() == 0)
        		System.out.println(Thread.currentThread().getName() + " did not find any occurences" );
        } else {
        	System.out.println(Thread.currentThread().getName() + " replacing.");
          replace();
        }
    }

    public static int occurences() {
        String randomString = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        int count = 0;
        long startTime = 0, endTime = 0, totalTime = 0;
        
        System.out.println(Thread.currentThread().getName() + " trying to find " + randomString);
        for(int i = 0; i < Critical.array.length; i++) {
        	if (randomString.equals(Critical.array[i])) {
        		System.out.println(Thread.currentThread().getName() + " contains " + randomString + " attempting to lock");
        		startTime = System.nanoTime();
        		lock.lock();
        		System.out.println(Thread.currentThread().getName() + " locked critical section");
        		try {
        			endTime = System.nanoTime();
        			totalTime = endTime - startTime;
        			System.out.println(Thread.currentThread().getName() + " waited " + totalTime + "ns to find " + randomString);
	        		count++;
	        	} catch (Exception e) {
		            e.printStackTrace();
	         	} finally {
	            System.out.println(randomString + " Total - " + count);
	            lock.unlock();
	          }
        	}
        }
                
        return count;
    }

    public static void replace() {
        String randomOne = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        String randomTwo = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        long startTime = 0, endTime = 0, totalTime = 0;

        System.out.println(Thread.currentThread().getName() + " trying to replace " + randomOne + " with " + randomTwo);
        for(int i = 0; i < Critical.array.length; i++) {
            if (randomOne.equals(Critical.array[i])){
            	lock.lock();
            	startTime = System.nanoTime();
            	try {
            		endTime = System.nanoTime();
            		totalTime = endTime - startTime;
            		System.out.println(Thread.currentThread().getName() + " waited " + totalTime + "ns to replace " + Critical.array[i] + " to " + randomTwo);
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
