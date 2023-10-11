/*
 * Copyright 2023 Dev Bwaim team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.bwaim.kustomalarm.core

import dev.bwaim.kustomalarm.core.Result.Error
import dev.bwaim.kustomalarm.core.Result.Error.UnexpectedError
import dev.bwaim.kustomalarm.core.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

public typealias DomainResult<T> = Result<T, Error<Any>>

public sealed interface Result<out T : Any?, out E : Any> {
    public data class Success<T : Any?>(public val value: T) : Result<T, Nothing>

    public sealed interface Error<E : Any> : Result<Nothing, E> {
        public val error: Throwable

        public data class ApiError(public override val error: DomainException) :
            Error<DomainException>

        public data class UnexpectedError(public override val error: Throwable) :
            Error<Nothing>
    }
}

public val <T : Any?> DomainResult<T>.value: T?
    get() = when (this) {
        is Success -> value
        is Error -> null
    }

public val <T : Any?> DomainResult<T>.error: Throwable?
    get() = when (this) {
        is Success -> null
        is Error -> error
    }

public suspend inline fun <T, R : Any?> T.executeCatching(
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend T.() -> R,
): DomainResult<R> = withContext(dispatcher) {
    runCatching { block().toDomainResult() }
        .getOrElse { exception: Throwable ->
            exception.rethrowIfCancellationException()
            UnexpectedError(exception)
        }
}

public fun <T : Any?> T.toDomainResult(): DomainResult<T> = Success(this)

public fun Throwable.rethrowIfCancellationException() {
    if (this is CancellationException) {
        throw this
    }
}
