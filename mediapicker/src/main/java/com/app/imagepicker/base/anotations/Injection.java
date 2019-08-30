/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.base.anotations;

import com.app.imagepicker.utils.CollectionUtil;
import com.app.imagepicker.utils.Preconditions;
import com.app.imagepicker.base.view.IBaseView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by stefan on 2018/1/12.
 */

public class Injection {

    public static <View extends IBaseView> void bindInject(Object o, View v) {
        Field[] fields = o.getClass().getDeclaredFields();
        if (!CollectionUtil.isEmpty(fields)) {
            for (Field f : fields) {
                if (f.getAnnotation(Inject.class) != null) {
                    f.setAccessible(true);
                    Class typeClass = f.getType();
                    try {
                        Class<View> vClass = (Class<View>) ((ParameterizedType) typeClass.getGenericSuperclass()).getActualTypeArguments()[0];
                        Constructor<View> c = typeClass.getConstructor(vClass);
                        Object p = c.newInstance(v);
                        p = Preconditions.checkNotNull(p);
                        f.set(o, p);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }




}
