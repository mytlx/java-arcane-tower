package com.mytlx.handcraft.rpc.utils;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 21:59:46
 */
public class KryoThreadLocal {

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    public static Kryo get() {
        return KRYO_THREAD_LOCAL.get();
    }

    public static void remove() {
        KRYO_THREAD_LOCAL.remove();
    }

}

