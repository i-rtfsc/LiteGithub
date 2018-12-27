/*
 * Copyright (c) 2018 anqi.huang@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyOS.base.guide;

import android.animation.TypeEvaluator;
import android.graphics.RectF;

public class RegionEvaluator implements TypeEvaluator {
    private RectF startRegion, endRegion;

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        startRegion = (RectF) startValue;
        endRegion = (RectF) endValue;
        float dl = fraction * (endRegion.left - startRegion.left) + startRegion.left;
        float dt = fraction * (endRegion.top - startRegion.top) + startRegion.top;
        float dr = fraction * (endRegion.right - startRegion.right) + startRegion.right;
        float db = fraction * (endRegion.bottom - startRegion.bottom) + startRegion.bottom;
        return new RectF(dl, dt, dr, db);
    }
}

