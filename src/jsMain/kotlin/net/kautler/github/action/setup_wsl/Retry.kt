/*
 * Copyright 2024-2025 Björn Kautler
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

package net.kautler.github.action.setup_wsl

import actions.core.debug
import actions.core.info

internal suspend inline fun <T> retry(amount: Int, crossinline block: suspend () -> T): T {
    (1..amount).map { i ->
        runCatching {
            return block()
        }.onFailure {
            if (i != amount) {
                debug(it.stackTraceToString())
                info("Failure happened, retrying (${it.message ?: it})")
            }
        }
    }.last().getOrThrow<Nothing>()
}
