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

import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix

operator fun BigIntegerMatrix.plus(summand: BigIntegerMatrix) = add(summand)

operator fun BigIntegerMatrix.minus(subtrahend: BigIntegerMatrix) = subtract(subtrahend)

operator fun BigIntegerMatrix.times(factor: BigIntegerMatrix) = multiply(factor)

operator fun BigIntegerMatrix.unaryMinus() = negate()
