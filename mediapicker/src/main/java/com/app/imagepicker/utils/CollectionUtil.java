/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/12.
 */
public class CollectionUtil {

    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }

    /**
     * @param c
     * @param <T>
     * @return true is empty
     */
    public static <T> boolean isEmpty(T... c) {
        return c == null || c.length == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

}
