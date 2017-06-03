package IntegersGrouping;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Goncalo Fonseca
 */
public class IntegersGrouping {

    public static void main(String[] args) {
        //Generates random data for test
        Random randomGenerator = new Random();
        int[] integers = new int[5 + randomGenerator.nextInt(45)];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = randomGenerator.nextInt(70);
        }
        int nGroup = 1 + randomGenerator.nextInt((int)Arrays.stream(integers).distinct().count());
       
        
        Grouper grouper = new Grouper();
        grouper.group(integers, nGroup);
        System.out.println(grouper.getGroups()+" nGroup: " + nGroup + "\n" );
        grouper.visualize();
    }
}
