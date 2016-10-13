import java.util.*;

public class Critical
{
    public static int poolSize = 125;
    public static int arraySize = 100;
    public static String[] pool = new String[poolSize];
    public static String[] array = new String[arraySize];

    public static int randomLength(int min, int max) {
        return min + (int)(Math.random() * max);
    }

    public static String randomString() {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();
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

    public static void runOperation() {
        int num = randomLength(0, 2500);

        if (num < 1900) {
            System.out.println(occurences());
        } else {
            replace();
        }
    }

    public static int occurences() {
        String randomString = pool[randomLength(0,poolSize)];
        int count = 0;

        for(int i = 0; i < array.length; i++) {
            if (randomString.equals(array[i]))
                count++;
        }

        return count;
    }

    public static void replace() {
        String randomOne = pool[randomLength(0,poolSize)];
        String randomTwo = pool[randomLength(0,poolSize)];

        for(int i = 0; i < array.length; i++) {
            if (randomOne.equals(array[i]))
                array[i] = randomTwo;
        }
    }

    public static void main(String[] args)
    {
        fillPool();
        fillArray();
        for (int i = 0; i < 500; i++) {
            runOperation();
        }
        
        DopeThread dope = new DopeThread();
        Thread t = new Thread(dope);
        t.start();
    }
}
