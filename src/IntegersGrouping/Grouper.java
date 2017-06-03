package IntegersGrouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

/**
 * This Class implements an adaptation of the k-means algorithm to one dimension
 * and groups numbers in n groups depending on how close they are to each other.
 *
 * It starts by assigning n random numbers from the initial array as the centers
 * of each group. After that calculates the distance between each point and the
 * centers and assigns the points to the group whose center is the closest.
 * After all points are assigned the centers of the groups are recalculated as
 * the mean of all the points of each center and the algorithm runs again until
 * the centers stops changing.
 *
 * This algorithm tends to be unconsistent because of the initial random assign
 * of the centers. So in this implementation the algorithm runs n specified
 * times (by default 50) and the solution is the best result. The best result is
 * the one with the lowest summation of the squares of the distances of each
 * point to their center. The square is used in order to penalize more integer
 * that are more further from the center.
 *
 * @author Goncalo Fonseca
 */
public class Grouper {

    private double error;
    private int nGroups;
    private int nIntegers;
    private int nIterations = 50;
    private int[] integers;

    private final List<Group> groups;
    private final List<Group> bestGroups;

    /**
     * Constructs an instance of grouper
     */
    public Grouper() {
        this.groups = new ArrayList<>();
        this.bestGroups = new ArrayList<>();
    }

    /**
     * Constructs an instance of grouper with the specified number of iterations
     *
     * @param nIterations number of times to find the best solution
     */
    public Grouper(int nIterations) {
        if (nIterations < 1) {
            throw new IllegalArgumentException("nIterations must be at least 1");
        }
        this.groups = new ArrayList<>();
        this.bestGroups = new ArrayList<>();
        this.nIterations = nIterations;
    }

    /**
     * Returns the best solution
     *
     * @return ArrayList with solution
     */
    public ArrayList getGroups() {
        ArrayList<ArrayList<Integer>> iGroups = new ArrayList<>();
        bestGroups.forEach((group) -> {
            iGroups.add(group.getGroup());
        });

        return iGroups;
    }

    /**
     * Sets the number of iterations of the algorithm
     *
     * @param iterations number of interations of the algorithm
     */
    public void setIterations(int iterations) {
        if (iterations < 1) {
            throw new IllegalArgumentException("nIterations must be at least 1");
        }
        this.nIterations = iterations;
    }

    /**
     * Initializes integers and nGroups to the specified value and runs the
     * algorithm to divide the integers into nGroups groups depending on how
     * close they are to each other. The algorithm is runned nIteration times
     * and saves the best result. The integers length must be equal or bigger
     * than the nGroups and must have at least nGroups different integers.
     *
     * @param integers Array of integers to group
     * @param nGroups Number of groups to generate
     */
    public void group(int[] integers, int nGroups) {
        if (integers == null) {
            throw new NullPointerException("integers has no content");
        }
        if (integers.length == 0) {
            throw new NullPointerException("integers has no content");
        }
        if (integers.length < nGroups) {
            throw new IllegalArgumentException("Integer length cannot be smaller than nGroups");
        }
        if (Arrays.stream(integers).distinct().count() < nGroups) {
            throw new IllegalArgumentException("Integers must have at least nGroups different numbers");
        }

        this.integers = integers;
        this.nIntegers = integers.length;
        this.nGroups = nGroups;
        error = Integer.MAX_VALUE;

        if (integers.length == nGroups) {
            groupIntegerLenghtEqualsnGroups();
        }

        createGroups();
        for (int i = 0; i < nIterations; i++) {
            generateCenters();
            calculate();
            calculateError();
        }
    }

    /**
     * Draw on the console the division of the groups. Used just to have a
     * visual idea of the solution.
     */
    public void visualize() {
        if (bestGroups.isEmpty()) {
            return;
        }
        ArrayList<Integer> limits = new ArrayList<>();
        ArrayList<Integer> groupss = new ArrayList<>();
        for (Group group : bestGroups) {
            group.sort();
            limits.add(group.getGroup().get(0));
            limits.add(group.getGroup().get(group.getGroup().size() - 1) + 1);
            groupss.addAll(group.getGroup());
        }
        Collections.sort(groupss, (n1, n2) -> {
            return n1 - n2;
        });

        for (int i = groupss.get(0); i <= groupss.get(groupss.size() - 1); i++) {
            if (limits.contains(i)) {
                System.out.print("| ");
            }
            if (groupss.contains(i)) {
                System.out.print(i + " ");
            } else {
                System.out.print(". ");
            }
        }
        System.out.print("|\n");
    }

    /**
     * Constructs nGroups of Group
     */
    private void createGroups() {
        groups.removeAll(groups);
        for (int i = 0; i < nGroups; i++) {
            groups.add(new Group());
        }
    }

    /**
     * Defines unrepeatable aleatory values within the minimum and maximum of
     * the integers to the centers
     */
    private void generateCenters() {
        ArrayList<Integer> centers = new ArrayList<>();

        Random randomGenerator = new Random();
        int position;
        for (int i = 0; i < nGroups; i++) {
            do {
                position = randomGenerator.nextInt(nIntegers);
            } while (centers.contains(integers[position]));
            centers.add(integers[position]);
            groups.get(i).setCenter(integers[position]);
        }
    }

    /**
     * Runs the algorithm until the centers stop changing
     */
    private void calculate() {
        boolean finish = false;

        while (!finish) {

            clearGroups();

            List<Double> lastCenters = getCenters();

            assignIntegers();

            calculcateCenters();

            List<Double> currentCenter = getCenters();

            double differenceCenters = 0;
            for (int i = 0; i < nGroups; i++) {
                differenceCenters += Math.abs(lastCenters.get(i) - currentCenter.get(i));
            }

            if (differenceCenters == 0.0) {
                finish = true;
            }

        }
    }

    /**
     * Gets the center of every group
     *
     * @return ArrayList with the center of each group
     */
    private List getCenters() {
        List<Double> centers = new ArrayList();
        groups.forEach((group) -> {
            centers.add(group.getCenter());
        });
        return centers;
    }

    /**
     * Removes all integers from every group. The groups will be empty after
     * this call returns.
     */
    private void clearGroups() {
        groups.forEach((group) -> {
            group.clear();
        });
    }

    /**
     * Assignes each integer to the closest center group.
     */
    private void assignIntegers() {
        double max = Double.MAX_VALUE;
        double min = max;
        int nGroup = 0;
        double distance;

        for (int i = 0; i < nIntegers; i++) {
            for (int j = 0; j < nGroups; j++) {
                distance = Math.abs(integers[i] - groups.get(j).getCenter());

                if (distance < min) {
                    min = distance;
                    nGroup = j;
                }
            }
            groups.get(nGroup).addInteger(integers[i]);
            min = max;
        }
    }

    /**
     * Calculates the center of every group
     */
    private void calculcateCenters() {
        groups.forEach((group) -> {
            group.recalculateCenter();
        });
    }

    /**
     * Measures the quality of the solution and if it is the best so far
     * assignes it to bestGroups. The quality of the solution is summation of
     * the error of all groups.
     */
    private void calculateError() {
        double thisIterationaerror = 0;
        for (Group group : groups) {
            thisIterationaerror += group.error();
        }
        if (thisIterationaerror < error) {
            error = thisIterationaerror;
            bestGroups.removeAll(bestGroups);
            groups.forEach((group) -> {
                bestGroups.add(group.copy());
            });
        }
    }

    /**
     * Group integers in case of nGroups equals Integers length
     */
    private void groupIntegerLenghtEqualsnGroups() {
        for (int i = 0; i < integers.length; i++) {
            ArrayList<Integer> array = new ArrayList<>();
            array.add(integers[i]);
            bestGroups.add(new Group(array));
        }
    }

    /**
     * Inner class responsible for holding information and all the methods
     * related to group
     */
    private class Group {

        private double center;
        private ArrayList<Integer> array;

        /**
         * Constructs a new instance of Group.
         */
        private Group() {
            this.array = new ArrayList<>();
        }

        /**
         * Constructs a new instance of group with the speciefied integers
         *
         * @param array ArrayList with integers
         */
        private Group(ArrayList<Integer> array) {
            this.array = new ArrayList<>(array);
        }

        /**
         * Returns an ArrayList with all integers of this group
         *
         * @return ArrayList containig all integers
         */
        private ArrayList<Integer> getGroup() {
            return array;
        }

        /**
         * Returns the center of this group
         *
         * @return center
         */
        private double getCenter() {
            return center;
        }

        /**
         * Sets the center of this group
         *
         * @param center
         */
        private void setCenter(int center) {
            this.center = center;
        }

        /**
         * Constructs a Group containing the integers of this group, in the
         * order they are returned by the collection's iterator.
         *
         * @return new Group
         */
        private Group copy() {
            return new Group(new ArrayList<>(array));
        }

        /**
         * Appends the specified integer to the end of the group
         *
         * @param i integer to be appended to the group
         */
        private void addInteger(int i) {
            array.add(i);
        }

        /**
         * Removes all integers from this group. The group will be empty after
         * this call returns.
         */
        private void clear() {
            array.clear();
        }

        /**
         * Calculates the center of the group. The center of the group is the
         * mean of all integers.
         */
        private void recalculateCenter() {
            double sum = 0;
            if (array.isEmpty()) {
                return;
            }
            for (int i = 0; i < array.size(); i++) {
                sum += array.get(i);
            }
            this.center = sum / array.size();
        }

        /**
         * Sorts the list increasingly. This method is only used by the
         * visualize method. Doesn't need to be used to run the algorithm.
         */
        private void sort() {
            Collections.sort(array, (n1, n2) -> {
                return n1 - n2;
            });
        }

        /**
         * Measure the quality of the group. Sum the squares of all distances to
         * the center. Uses square in order to penalize more integers that are
         * further from the center
         *
         * @return Summation of the squares of the distances to the center
         */
        private double error() {
            double error = 0;

            for (int i = 0; i < array.size(); i++) {
                error += Math.pow((array.get(i) - center), 2);
            }

            return error;
        }
    }

}
