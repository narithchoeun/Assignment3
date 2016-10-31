import java.util.concurrent.locks.ReentrantLock;

class DopeThread implements Runnable 
{
    private ReentrantLock lock = new ReentrantLock();
    private int operationCount = 1;
    private long totalSearchTime = 0, totalReplaceTime = 0;
    
    //runs thread for a set amount of operations
    public void run() {
    	for(int i = 0; i < operationCount; i++){
        runOperation();
    	}
    }

    //when thread starts, it randomly performs an operation based on the random generated number.
    //if the random number is < 1900 it will find all occurrences of a random string from the pool 
    //by cross checking it with what's in the array.
    
    //if the random number is > 1900, it will replaced a random selected string from the pool and 
    //replace the 1st occurrence of that string from a second random string chosen from the pool.
    public void runOperation() {
        int num = Critical.randomLength(0, 2500);

        if (num < 1900) {
          System.out.println(Thread.currentThread().getName() + " finding occurences.");
        	if (occurences() == 0)
        		System.out.println(Thread.currentThread().getName() + " did not find any occurences" );
        	System.out.println(Thread.currentThread().getName() + " totalSearchTime: " + totalSearchTime);
        	
        } else {
        	System.out.println(Thread.currentThread().getName() + " replacing.");
          replace();
        	System.out.println(Thread.currentThread().getName() + " totalReplaceTime: " + totalReplaceTime);
        }
    }

    // selects a random string from the pool and find all occurrences of that string in the array
    // uses optimistic approach without 2 locks or validation
    public int occurences() {
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
        			totalSearchTime += totalTime;
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

    //select a random string from the pool and replace the 1st occurrence of that string with a second randomly chosen string from the pool.
    public void replace() {
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
            		totalReplaceTime += totalTime;
            		System.out.println(Thread.currentThread().getName() + " waited " + totalTime + "ns to replace " + Critical.array[i] + " to " + randomTwo);
            		Critical.array[i] = randomTwo;
            		break;
            	} catch (Exception e){
            		e.printStackTrace();
            	} finally {
            		lock.unlock();
            	}
            }
        }
    }
}
