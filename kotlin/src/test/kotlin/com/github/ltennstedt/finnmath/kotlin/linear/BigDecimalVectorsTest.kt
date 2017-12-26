/*
 * Copyright 2017 Lars Tennstedt
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

package com.github.ltennstedt.finnmath.kotlin.linear

import assertk.assert
import assertk.assertions.isEqualTo
import com.github.ltennstedt.finnmath.core.util.MathRandom
import org.junit.Test

class BigDecimalVectorsTest {
    private val bound = 10L
    private val scale = 2
    private val size = 4
    private val howMany = 10
    private val mathRandom = MathRandom(7)
    private val vectors = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany)
    private val others = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany)

    @Test
    fun plusShouldSucceed() {
        vectors.forEach {
            others.forEach { other ->
                assert(it + other).isEqualTo(it.add(other))
            }
        }
    }

    @Test
    fun minusShouldSucceed() {
        vectors.forEach {
            others.forEach { other ->
                assert(it - other).isEqualTo(it.subtract(other))
            }
        }
    }

    @Test
    fun timesShouldSucceed() {
        vectors.forEach {
            others.forEach { other ->
                assert(it * other).isEqualTo(it.dotProduct(other))
            }
        }
    }

    @Test
    fun unaryMinusShouldSucceed() {
        vectors.forEach {
            assert(-it).isEqualTo(it.negate())
        }
    }
}


