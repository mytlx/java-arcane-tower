package com.mytlx.arcane.snippets.sort;

import java.util.Random;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-19 22:50
 */
public class RandomInput {

    public static int[] getRandomInput(int length) {
        int[] nums = new int[length];
        for (int i = 0; i < length; i++) {
            nums[i] = i + 1;
        }
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int j = random.nextInt(length);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        return nums;
    }

}
