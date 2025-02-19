package com.mytlx.arcane.scratches.sort;

import java.util.Arrays;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-19 22:57
 */
public class InsertionSort {

    public static int[] sort(int[] nums) {
        if (nums.length <= 1) return nums;

        for (int i = 1; i < nums.length; i++) {
            for (int j = i; j > 0; j--) {
                if (nums[j] < nums[j - 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j - 1];
                    nums[j - 1] = temp;
                }
            }
        }

        return nums;
    }

    public static void main(String[] args) {
        int[] nums = RandomInput.getRandomInput(10);

        System.out.println("nums = " + Arrays.toString(nums));

        System.out.println("sort(nums) = " + Arrays.toString(sort(nums)));
    }

}
