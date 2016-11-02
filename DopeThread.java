import java.util.concurrent.locks.ReentrantLock;

class DopeThread implements Runnable 
{
    private ReentrantLock lock = new ReentrantLock();
    private int operationCount = 10, threadNum = 20;
    private double searchAvg = 0, replaceAvg = 0, searchSigma = 0, replaceSigma = 0;
    private long[] searchTimes = new long[threadNum];
    private long[] replaceTimes = new long[threadNum];
    
    //runs thread for a set amount of operations
    public void run() {
    	for(int i = 0; i < operationCount; i++){
        runOperation();
    	}
    	if (Thread.currentThread().getId()-(long)8 == 19){
      	average();
      	standardDeviation();
    		printStat();
    	}
    }
    

    //when thread starts, it randomly performs an operation based on the random generated number.
    //if the random number is < 1900 it will find all occurrences of a random string from the pool 
    //by cross checking it with what's in the array.
    
    //if the random number is > 1900, it will replaced a random selected string from the pool and 
    //replace the 1st occurrence of that string from a second random string chosen from the pool.
    private void runOperation() {
        int num = Critical.randomLength(0, 2500);

        if (num < 1900) {
        	occurrences();
        } else {
          replace();
        }
    }

    // selects a random string from the pool and find all occurrences of that string in the array
    // uses optimistic approach without 2 locks or validation
    private int occurrences() {
        String randomString = Critical.pool[Critical.randomLength(0,Critical.poolSize)];
        int count = 0;
        long startTime = 0, endTime = 0, totalTime = 0;
        for(int i = 0; i < Critical.array.length; i++) {
        	if (randomString.equals(Critical.array[i])) {
        		startTime = System.nanoTime();
        		lock.lock();
        		try {
        			endTime = System.nanoTime();
        			totalTime = endTime - startTime;
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

        for(int i = 0; i < Critical.array.length; i++) {
            if (randomOne.equals(Critical.array[i])){
            	startTime = System.nanoTime();
            	lock.lock();
            	try {
            		endTime = System.nanoTime();
            		totalTime = endTime - startTime;
            		replaceTimes[(int)Thread.currentThread().getId()-8] += totalTime;
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
    
    // calculates the average search and replace wait time 
    private synchronized void average(){
    	for(int i = 0; i < threadNum; i++){
    		searchAvg = (double)searchTimes[i]/(double)threadNum;
    		replaceAvg = (double)replaceTimes[i]/(double)threadNum;
    	}
    }
    
    // calculates the standard deviation 
    private synchronized void standardDeviation(){
    	int searchSum = 0, replaceSum = 0;
    	for(int i = 0; i < threadNum; i++){
    		searchSum += Math.pow((searchTimes[i] - searchAvg), 2);
    		replaceSum += Math.pow((replaceTimes[i] - replaceAvg), 2);
    	}
    	
    	searchSigma = Math.sqrt(searchSum/threadNum);
    	replaceSigma = Math.sqrt(replaceSum/threadNum);
    }
    
    // prints the search / replace times along with the deviation and average of all the threads.
    private synchronized void printStat(){
    	System.out.print("searchTimes: ");
    	for (int i = 0; i < 20; i++){
    		System.out.print(searchTimes[i] + " ");
    	}
    	System.out.println();
    	System.out.print("replaceTimes: ");
    	for (int i = 0; i < replaceTimes.length; i++){
    		System.out.print(replaceTimes[i] + " ");
    	}
    	System.out.println();
    	System.out.println("search deviation: " + searchSigma + "\n"
    			+ "search average: " + searchAvg + "\n"
    			+ "replace deviation: " + replaceSigma + "\n"
    			+ "replace average: " + replaceAvg);
    }
}
