public class Critical
{
    public static int poolSize = 125;
    public static int arraySize = 100;
    public static int numberOfThreads = 20;
    public static String[] pool = new String[poolSize];
    public static String[] array = new String[arraySize];

    //returns random int between the min and max parameters
    public static int randomLength(int min, int max) {
        return min + (int)(Math.random() * max);
    }

    //generates a random string with a random length
    public static String randomString() {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String temp = "";
        int stringLength = randomLength(5, 5);

        for (int i = 0; i < stringLength; i++) {
            int location = randomLength(0, letters.length());
            temp += Character.toString(letters.charAt(location));
        }

        return temp;
    }

    //fill up the shared pool 
    public static void fillPool() {
        for(int i = 0; i < pool.length; i++) {
            pool[i] = randomString();
        }
    }

    //fill up the shared array 
    public static void fillArray() {
        for(int i = 0; i < arraySize; i++){ 
            int num = randomLength(0, poolSize);
            array[i] = pool[num];
        }
    }

    public static void main(String[] args)
    {
        fillPool();
        fillArray();
        
        DopeThread dope = new DopeThread();
        for (int i = 0; i < numberOfThreads; i++) {
        	Thread t = new Thread(dope);
          t.start();
        }
    }
}