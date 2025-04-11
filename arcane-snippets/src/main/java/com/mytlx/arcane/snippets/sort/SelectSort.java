package com.mytlx.arcane.snippets.sort;

import java.util.Arrays;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-19 22:46
 */
public class SelectSort {

    public static int[] sort(int[] nums) {
        if (nums.length <= 1) return nums;

        for (int i = 0; i < nums.length; i++) {
            int minIndex = i;
            for (int j = i; j < nums.length; j++) {
                if (nums[minIndex] > nums[j]) {
                    minIndex = j;
                }
            }
            if (minIndex == i) continue;
            int temp = nums[minIndex];
            nums[minIndex] = nums[i];
            nums[i] = temp;
        }

        return nums;
    }

    public static void main(String[] args) {
        int[] nums = RandomInput.getRandomInput(10);

        System.out.println("nums = " + Arrays.toString(nums));

        System.out.println("sort(nums) = " + Arrays.toString(sort(nums)));

    }

}
