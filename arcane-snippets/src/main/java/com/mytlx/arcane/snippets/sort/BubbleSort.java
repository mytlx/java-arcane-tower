package com.mytlx.arcane.snippets.sort;

import java.util.Arrays;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-19 22:33
 */
public class BubbleSort {
    public static int[] sort(int[] nums) {
        if (nums.length <= 1) return nums;

        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }

        return nums;
    }

    public static void main(String[] args) {

        int[] nums = RandomInput.getRandomInput(10);

        System.out.println("random input nums = " + Arrays.toString(nums));

        System.out.println("sort(nums) = " + Arrays.toString(sort(nums)));
    }
}
