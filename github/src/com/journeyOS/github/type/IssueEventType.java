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

package com.journeyOS.github.type;

import com.google.gson.annotations.SerializedName;

public enum IssueEventType {
    /**
     * The issue was closed by the actor. When the commit_id is present, it identifies the
     * commit that closed the issue using "closes / fixes #NN" syntax.
     */
    closed,
    /**
     * The issue was reopened by the actor.
     */
    reopened,
    commented,
    @SerializedName("comment_deleted") commentDeleted,


    /**
     * The issue title was changed.
     */
    renamed,
    /**
     * The issue was locked by the actor.
     */
    locked,
    /**
     * The issue was unlocked by the actor.
     */
    unlocked,

    /**
     * The issue was referenced from another issue. The `source` attribute contains the `id`,
     * `actor`, and `url` of the reference's source.
     */
    @SerializedName("cross-referenced") crossReferenced,


    /**
     * The issue was assigned to the actor.
     */
    assigned,
    /**
     * The actor was unassigned from the issue.
     */
    unassigned,
    /**
     * A label was added to the issue.
     */
    labeled,
    /**
     * A label was removed from the issue.
     */
    unlabeled,
    /**
     * The issue was added to a milestone.
     */
    milestoned,
    /**
     * The issue was removed from a milestone.
     */
    demilestoned,
}
