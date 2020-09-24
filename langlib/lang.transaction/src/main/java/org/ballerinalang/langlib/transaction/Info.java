/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.langlib.transaction;

import org.ballerinalang.jvm.BallerinaErrors;
import org.ballerinalang.jvm.scheduling.Scheduler;
import org.ballerinalang.jvm.api.BStringUtils;
import org.ballerinalang.jvm.api.values.BString;
import org.ballerinalang.jvm.scheduling.Strand;
import org.ballerinalang.jvm.transactions.TransactionLocalContext;
import org.ballerinalang.jvm.values.MapValue;

/**
 * Extern function transaction:info.
 *
 * @since 2.0.0-preview1
 */
public class Info {

    public static MapValue<BString, Object> info() {
        Strand strand = Scheduler.getStrand();
        if (IsTransactional.isTransactional()) {
            TransactionLocalContext context = strand.currentTrxContext;
            return (MapValue<BString, Object>) context.getInfoRecord();
        }
        throw BErrorCreator.createError(BStringUtils
                .fromString("cannot call info() if the strand is not in transaction mode"));
    }
}
