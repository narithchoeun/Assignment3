import java.util.*;

public class Critical
{
    public static String[] pool = new String[125];
    public static String[] array = new String[100];

    public static int randomLength() {
        Random r = new Random();
        int num = 5 + (int)(Math.random() * 5);
        return num;
    }

    public static String randomString() {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();
        String temp = "";
        int stringLength = randomLength();

        for (int i = 0; i < stringLength; i++) {
            int location = (int)(Math.random() * letters.length());
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
        for(int i = 0; i < 100; i++){ 
            int num = (int)(Math.random() * 125);
            array[i] = pool[num];
        }
    }

    public static void main(String[] args)
    {
        fillPool();
        fillArray();
        for(int i = 0; i < pool.length; i++){
            System.out.println(pool[i]);
        }
    }
}
