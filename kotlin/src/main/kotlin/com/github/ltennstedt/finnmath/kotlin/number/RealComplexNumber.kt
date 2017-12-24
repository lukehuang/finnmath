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

import com.github.ltennstedt.finnmath.core.number.RealComplexNumber

operator fun RealComplexNumber.plus(summand: RealComplexNumber) = add(summand)

operator fun RealComplexNumber.minus(subtrahend: RealComplexNumber) = subtract(subtrahend)

operator fun RealComplexNumber.times(factor: RealComplexNumber) = multiply(factor)

operator fun RealComplexNumber.div(divisor: RealComplexNumber) = divide(divisor)

operator fun RealComplexNumber.unaryMinus() = negate()
