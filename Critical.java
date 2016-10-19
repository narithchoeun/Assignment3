import java.util.*;

public class Critical
{
    public static int poolSize = 12;
    public static int arraySize = 10;
    public static int numberOfThreads = 4;
    public static String[] pool = new String[poolSize];
    public static String[] array = new String[arraySize];

    public static int randomLength(int min, int max) {
        return min + (int)(Math.random() * max);
    }

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

    public static void fillPool() {
        for(int i = 0; i < pool.length; i++) {
            pool[i] = randomString();
        }
    }

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
        
        System.out.print("pool:");
        for(int i = 0; i < pool.length; i++){
        	System.out.print(pool[i] + " ");
        }
        System.out.println();
        
        System.out.print("array:");
        for(int i = 0; i < array.length; i++){
        	System.out.print(array[i] + " ");
        }
        System.out.println("\n");
        
        DopeThread dope = new DopeThread();
        for (int i = 0; i < numberOfThreads; i++) {
            Thread t = new Thread(dope, "Thread " + i);
            t.start();
        }
    }
}
