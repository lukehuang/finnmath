/*
 * Copyright 2017 Lars Tennstedt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by aplicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ltennstedt.finnmath.kotlin.linear

import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix

operator fun SimpleComplexNumberMatrix.plus(summand: SimpleComplexNumberMatrix) = add(summand)

operator fun SimpleComplexNumberMatrix.minus(subtrahend: SimpleComplexNumberMatrix) = subtract(subtrahend)

operator fun SimpleComplexNumberMatrix.times(factor: SimpleComplexNumberMatrix) = multiply(factor)

operator fun SimpleComplexNumberMatrix.unaryMinus() = negate()
