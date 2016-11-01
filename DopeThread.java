import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

class DopeThread implements Runnable 
{
    private ReentrantLock lock = new ReentrantLock();
    private int operationCount = 3, searchCount = 0, replaceCount = 0;
    private long totalSearchWaitTime = 0, totalReplaceWaitTime = 0;
    static long searchTimes[] = new long[20];
    static long replaceTimes[] = new long[20];
    
    //runs thread for a set amount of operations
    public void run() {
    	for(int i = 0; i < operationCount; i++){
        runOperation();
    	}
    
    	printList();
    }
    

    //when thread starts, it randomly performs an operation based on the random generated number.
    //if the random number is < 1900 it will find all occurrences of a random string from the pool 
    //by cross checking it with what's in the array.
    
    //if the random number is > 1900, it will replaced a random selected string from the pool and 
    //replace the 1st occurrence of that string from a second random string chosen from the pool.
    private void runOperation() {
        int num = Critical.randomLength(0, 2500);

//        if (num < 1900) {
//          System.out.println(Thread.currentThread().getId()-(long)8 + " finding occurences.");
//        	if (occurences() == 0)
//        		System.out.println(Thread.currentThread().getId()-(long)8 + " did not find any occurences" );
//        	System.out.println(Thread.currentThread().getId()-(long)8 + " totalSearchWaitTime: " + totalSearchWaitTime);
//        } else {
//        	System.out.println(Thread.currentThread().getId()-(long)8 + " replacing.");
//          replace();
//        	System.out.println(Thread.currentThread().getId()-(long)8 + " totalReplaceWaitTime: " + totalReplaceWaitTime);
//        }
    }

    // selects a random string from the pool and find all occurrences of that string in the array
    // uses optimistic approach without 2 locks or validation
    private int occurences() {
        String randomString = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        int count = 0;
        long startTime = 0, endTime = 0, totalTime = 0;
        
//        System.out.println(Thread.currentThread().getId()-(long)8 + " trying to find " + randomString);
        searchCount++;
        for(int i = 0; i < Critical.array.length; i++) {
        	if (randomString.equals(Critical.array[i])) {
//        		System.out.println(Thread.currentThread().getId()-(long)8 + " contains " + randomString + " attempting to lock");
        		startTime = System.nanoTime();
        		lock.lock();
        		System.out.println(Thread.currentThread().getId()-(long)8 + " locked critical section");
        		try {
        			endTime = System.nanoTime();
        			totalTime = endTime - startTime;
        			System.out.println(Thread.currentThread().getId()-(long)8 + " waited " + totalTime + "ns to find " + randomString);
        			System.out.println(Thread.currentThread().getId()-(long)8 + " total being added: " + totalSearchWaitTime + " " + totalTime);
//        			addTime(Thread.currentThread().getId(), 1, totalSearchWaitTime);
        			searchTimes[(int)Thread.currentThread().getId()-8] += totalTime;
	        		count++;
	        	} catch (Exception e) {
		            e.printStackTrace();
	         	} finally {
	            lock.unlock();
	          }
        	}
        }
                
        return count;
    }

    //select a random string from the pool and replace the 1st occurrence of that string with a second randomly chosen string from the pool.
    private void replace() {
        String randomOne = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        String randomTwo = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        long startTime = 0, endTime = 0, totalTime = 0;

//        System.out.println(Thread.currentThread().getId() + " trying to replace " + randomOne + " with " + randomTwo);
        replaceCount++;
        for(int i = 0; i < Critical.array.length; i++) {
            if (randomOne.equals(Critical.array[i])){
//          		System.out.println(Thread.currentThread().getId() + " contains " + randomOne + " attempting to lock");
            	startTime = System.nanoTime();
            	lock.lock();
//          		System.out.println(Thread.currentThread().getId()-(long)8 + " locked critical section");
            	try {
            		endTime = System.nanoTime();
            		totalTime = endTime - startTime;
          			System.out.println(Thread.currentThread().getId()-(long)8 + " total being added: " + totalReplaceWaitTime + " " + totalTime);
            		System.out.println(Thread.currentThread().getId()-(long)8 + " waited " + totalTime + "ns to replace " + Critical.array[i] + " to " + randomTwo);
            		replaceTimes[(int)Thread.currentThread().getId()-8] += totalTime;
//            		addTime(Thread.currentThread().getId(), 2, totalSearchWaitTime);
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
    
    private void addTime(long thread, int op, long time){
    	int index = (int)thread - 8;
    	switch (op) {
	    	case 1:
	    		if (searchTimes[index] == 0){
	    			searchTimes[index] = time;
	    		} else {
	    			searchTimes[index] += time;
	    		}
	    		break;
	    	case 2:
	    		if (replaceTimes[index] == 0){
	    			replaceTimes[index] = time;
	    		} else {
	    			replaceTimes[index] += time;
	    		}
	    		break;
    	}
    }
    
    private synchronized void printList(){
    	System.out.print("searchTimes: ");
    	for (int i = 0; i < 20; i++){
    		System.out.print(searchTimes[i] + " ");
    	}
    	System.out.println();
//    	System.out.print("replaceTimes: ");
//    	for (int i = 0; i < replaceTimes.length; i++){
//    		System.out.print(replaceTimes[i] + " ");
//    	}
//    	System.out.println();
    }
}
