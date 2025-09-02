package com.mytlx.arcane.handcraft.schedule;

import lombok.Data;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 22:23:38
 */
@Data
public class HCJob implements Comparable<HCJob> {

    private Runnable task;

    private long startTime;

    private long delay;

    @Override
    public int compareTo(HCJob o) {
        return Long.compare(this.startTime, o.startTime);
    }
}
