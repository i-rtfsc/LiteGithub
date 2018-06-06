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

package com.journeyOS.core;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.journeyOS.base.Constant;
import com.journeyOS.base.persistence.SpUtils;
import com.squareup.picasso.Picasso;

public final class ImageEngine {

    private ImageEngine() {
        throw new RuntimeException("ImageEngine cannot be initialized!");
    }

    public static void load(Context context, String url, ImageView view, int defaultResId) {
        String engine = SpUtils.getInstant().getString(Constant.IMAGE_ENGINE, Constant.IMAGE_ENGINE_GLIDE);
        if (engine.equals(Constant.IMAGE_ENGINE_GLIDE)) {
            RequestOptions options = new RequestOptions().placeholder(defaultResId);

            boolean useCache = SpUtils.getInstant().getBoolean(Constant.USE_CACHE, true);
            if (useCache) {
                options.diskCacheStrategy(DiskCacheStrategy.ALL);
            }

            Glide.with(context).load(url).apply(options).into(view);
        } else if (engine.equals(Constant.IMAGE_ENGINE_PICASSO)) {
            Picasso.with(context).load(url).placeholder(defaultResId).into(view);
        }
    }

}
