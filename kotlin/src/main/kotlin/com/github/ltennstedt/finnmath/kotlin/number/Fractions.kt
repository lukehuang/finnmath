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

import com.github.ltennstedt.finnmath.core.number.Fraction

operator fun Fraction.plus(fraction: Fraction) = add(fraction)

operator fun Fraction.minus(fraction: Fraction) = subtract(fraction)

operator fun Fraction.times(fraction: Fraction) = multiply(fraction)

operator fun Fraction.div(fraction: Fraction) = divide(fraction)

operator fun Fraction.unaryMinus() = negate()

operator fun Fraction.inc() = add(Fraction.ONE)

operator fun Fraction.dec() = subtract(Fraction.ONE)
