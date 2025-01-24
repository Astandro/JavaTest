package test;

import lib.BubbleSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSortTest {
    public static void main(String[] args) {
        BubbleSort bubbleSort = new BubbleSort();

        // Test Case 1: Normal case with random numbers
        List<Integer> testCase1 = new ArrayList<Integer>(Arrays.asList(5,3,8,6,1,4,9));
        System.out.println("Original List: " + Arrays.toString(testCase1.toArray()));
        bubbleSort.sort(testCase1);
        System.out.println("Sorted List: " + Arrays.toString(testCase1.toArray()));

        // Test Case 2: Already sorted list
        List<Integer> testCase2 = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
        System.out.println("Original List: " + Arrays.toString(testCase2.toArray()));
        bubbleSort.sort(testCase2);
        System.out.println("Sorted List: " + Arrays.toString(testCase2.toArray()));

        // Test Case 3: Reverse sorted list
        List<Integer> testCase3 = new ArrayList<Integer>(Arrays.asList(9,8,7,6,5,4,3,2,1));
        System.out.println("Original List: " + Arrays.toString(testCase3.toArray()));
        bubbleSort.sort(testCase3);
        System.out.println("Sorted List: " + Arrays.toString(testCase3.toArray()));

        // Test Case 4: List with all identical element
        List<Integer> testCase4 = new ArrayList<Integer>(Arrays.asList(5,5,5,5,5,5,5));
        System.out.println("Original List: " + Arrays.toString(testCase4.toArray()));
        bubbleSort.sort(testCase4);
        System.out.println("Sorted List: " + Arrays.toString(testCase4.toArray()));

        // Test Case 5: List with negative elements
        List<Integer> testCase5 = new ArrayList<Integer>(Arrays.asList(-5,-3,-10,-8,-19));
        System.out.println("Original List: " + Arrays.toString(testCase5.toArray()));
        bubbleSort.sort(testCase5);
        System.out.println("Sorted List: " + Arrays.toString(testCase5.toArray()));

        // Test Case 6: List with single element
        List<Integer> testCase6 = new ArrayList<Integer>(List.of(100));
        System.out.println("Original List: " + Arrays.toString(testCase6.toArray()));
        bubbleSort.sort(testCase6);
        System.out.println("Sorted List: " + Arrays.toString(testCase6.toArray()));

        // Test Case 7: Empty list
        List<Integer> testCase7 = new ArrayList<Integer>();
        System.out.println("Original List: " + Arrays.toString(testCase7.toArray()));
        bubbleSort.sort(testCase7);
        System.out.println("Sorted List: " + Arrays.toString(testCase7.toArray()));

        // Test Case 8: Large list (stress test)
        List<Integer> testCase8 = new ArrayList<Integer>(Arrays.asList(12,42,23,64,34,36,7,84,53,34,345,334,234,234,234,2,34,2342,34,234,234,34,35,3453,23,23,2334,432,2));
        System.out.println("Original List: " + Arrays.toString(testCase8.toArray()));
        bubbleSort.sort(testCase8);
        System.out.println("Sorted List: " + Arrays.toString(testCase8.toArray()));
    }
}
