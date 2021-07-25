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

package com.journeyOS.github.ui.fragment.files.adapter;

import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.github.R;

import butterknife.BindView;

public class ReposFileHolder extends BaseViewHolder<ReposFileData> {

    @BindView(R.id.file_type)
    AppCompatImageView fileType;
    @BindView(R.id.file_name)
    TextView fileName;
    @BindView(R.id.file_size)
    TextView fileSize;

    public ReposFileHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(ReposFileData data, int position) {
        fileName.setText(data.name);
        if (data.isFile) {
            if (data.size == 0) {
                fileType.setImageResource(R.drawable.svg_folder_share);
            } else {
                fileType.setImageResource(R.drawable.svg_file);
                fileSize.setText(BaseUtils.getSizeString(data.size));
            }
        } else {
            fileType.setImageResource(R.drawable.svg_folder);
            fileSize.setText("");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_file;
    }
}
