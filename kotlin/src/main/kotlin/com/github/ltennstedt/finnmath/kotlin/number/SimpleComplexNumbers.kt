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

package com.github.ltennstedt.finnmath.kotlin.number

import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber

operator fun SimpleComplexNumber.plus(summand: SimpleComplexNumber) = add(summand)

operator fun SimpleComplexNumber.minus(subtrahend: SimpleComplexNumber) = subtract(subtrahend)

operator fun SimpleComplexNumber.times(factor: SimpleComplexNumber) = multiply(factor)

operator fun SimpleComplexNumber.div(divisor: SimpleComplexNumber) = divide(divisor)

operator fun SimpleComplexNumber.unaryMinus() = negate()
