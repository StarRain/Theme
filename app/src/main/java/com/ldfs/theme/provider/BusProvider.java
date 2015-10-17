/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ldfs.theme.provider;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Maintains a singleton newInstance for obtaining the bus. Ideally this would be replaced with a more efficient means
 * such as through injection directly into interested classes.
 */
public final class BusProvider {
    private static final AndroidBus BUS = new AndroidBus();

    private BusProvider() {
    }

    public static void post(Object object) {
        if (null == object) return;
        BUS.post(object);
    }

    public static void regist(Object object) {
        if (null == object) return;
        BUS.register(object);
    }

    public static void unregist(Object object) {
        if (null == object) return;
        BUS.unregister(object);
    }

    public static class AndroidBus extends Bus {

        private final Handler mainThread = new Handler(Looper.getMainLooper());

        public AndroidBus() {
            super(ThreadEnforcer.ANY);
        }

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mainThread.post(() -> post(event));
            }
        }

        @Override
        public void register(Object object) {
            super.register(object);
        }

        @Override
        public void unregister(Object object) {
            super.unregister(object);
        }
    }
}
