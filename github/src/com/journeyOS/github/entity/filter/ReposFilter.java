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

package com.journeyOS.github.entity.filter;

import com.journeyOS.github.type.ReposSort;
import com.journeyOS.github.type.ReposType;
import com.journeyOS.github.type.SortDirection;

public class ReposFilter {
    public final static ReposFilter DEFAULT = new ReposFilter();

    public ReposType reposType = ReposType.All;
    public String type = reposType.name().toLowerCase();

    public ReposSort reposSort = ReposSort.Full_name;
    public String sort = reposSort.name().toLowerCase();

    public SortDirection sortDirection = SortDirection.DESC;
    public String direction = sortDirection.name().toLowerCase();

}
