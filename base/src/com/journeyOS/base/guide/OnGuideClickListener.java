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

public interface OnGuideClickListener {
    /**
     * 当遮罩层空白处被点击时候的回调
     */
    void onMask();

    /**
     * 当下一步按钮被点击时的回调
     *
     * @param nextIndex 下一目标的index
     */
    void onNext(int nextIndex);

    /**
     * 当前高亮/扫描目标区域被点击时的回调
     *
     * @param step 当前目标区域的step
     */
    void onTarget(int step);

    /**
     * 当跳过按钮被点击时的回调
     */
    void onJump();

    /**
     * 引导开始时候回调
     */
    void onGuideStart();

    /**
     * 跳转到下一个目标时候的回调
     *
     * @param nextStep 下一个目标的step
     */
    void onGuideNext(int nextStep);

    /**
     * 引导完成之后的回调
     */
    void onGuideFinished();
}
