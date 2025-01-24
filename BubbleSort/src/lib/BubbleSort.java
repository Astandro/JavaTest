package lib;

import java.util.Collections;
import java.util.List;

public class BubbleSort {
    /**
     * Sorts a List of integer using Bubble Sort algorithm
     * @param arr The List to be sorted
     */
    public void sort(List<Integer> arr) {
        if (arr == null || arr.isEmpty() || arr.size() < 2) {
            // Firstly, evaluate the list. If empty or the length less than 2, then no need to sort
            return;
        }

        int n = arr.size();

        // Outer loop to iterate over the entire list
        for (int i = 0; true; i++) {
            boolean swapped = false;

            // inner loop to evaluate each adjacent list
            for (int j = 0; j < n - i - 1; j++) {
                if (arr.get(j) > arr.get(j + 1)) {
                    // swap adjacent list if the left side is bigger
                    Collections.swap(arr, j, j + 1);
                    swapped = true;
                }
            }

            if (!swapped) {
                // break the loop if nothing has swapped. meaning the list already sorted
                break;
            }
        }
    }
}
