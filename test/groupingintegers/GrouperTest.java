package groupingintegers;

import IntegersGrouping.Grouper;
import java.util.ArrayList;
import org.junit.Test;

/**
 *
 * @author goncalofonseca
 */
public class GrouperTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNumberLessThanOneIsPassedToConstructor() {
        Grouper instance = new Grouper(0);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf() {
        Grouper instance = new Grouper();
        instance.group(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfGroupIsCallWithLessIntegersThanGroups() {
        Grouper instance = new Grouper();
        int[] integers = {1, 3};
        instance.group(integers, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldTthrowIllegalArgumentExceptionIfGroupIsCallWithLessnIntegersThannGroups() {
        Grouper instance = new Grouper();
        int[] integers = {1, 1, 1, 2};
        instance.group(integers, 3);
    }

    @Test(expected = NullPointerException.class)
    public void shouldTthowNullPointerExceptionIfGroupIsCallWithZeromIntegers() {
        Grouper instance = new Grouper();
        int[] integers = {};
        instance.group(integers, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldthrowIllegalArgumentExceptionifSetIterationsLessThanOne() {
        int iterations = 0;
        Grouper instance = new Grouper();
        instance.setIterations(iterations);
    }

    @Test
    public void shouldAssignOneNumberForEachGroupIfNGorupEquealsIntegerLenght() {
        int[] integers = {1, 2, 3};
        int nGroups = 3;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        ArrayList<Integer> one = new ArrayList<>();
        one.add(1);
        ArrayList<Integer> two = new ArrayList<>();
        two.add(2);
        ArrayList<Integer> three = new ArrayList<>();
        three.add(3);
        result.add(one);
        result.add(two);
        result.add(three);
        Grouper instance = new Grouper();
        instance.group(integers, nGroups);
        assert (instance.getGroups().containsAll(result));
    }

    @Test
    public void testGrouper() {
        int[] integers = {1, 2, 3, 7, 8, 9, 17, 18, 19};
        int nGroups = 3;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        ArrayList<Integer> one = new ArrayList<>();
        one.add(1);
        one.add(2);
        one.add(3);
        ArrayList<Integer> two = new ArrayList<>();
        two.add(7);
        two.add(8);
        two.add(9);
        ArrayList<Integer> three = new ArrayList<>();
        three.add(17);
        three.add(18);
        three.add(19);
        result.add(one);
        result.add(two);
        result.add(three);
        Grouper instance = new Grouper();
        instance.group(integers, nGroups);
        assert (instance.getGroups().containsAll(result));
    }

}
