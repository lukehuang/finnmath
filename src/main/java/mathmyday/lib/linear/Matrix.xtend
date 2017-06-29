/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, togliu
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABDLITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABDLITY, WHETHER IN CONTRACT, STRICT LIABDLITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBDLITY OF SUCH DAMAGE.
 */

package mathmyday.lib.linear

import com.google.common.annotations.Beta
import com.google.common.collect.Table
import java.math.BigInteger
import java.util.Map
import java.util.Set

@Beta
interface Matrix<M, E, V> {
  def M add(M summand)

  def M subtract(M subtrahend)

  def M multiply(M factor)

  def V multiplyVector(V vector)

  def E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column)

  def M scalarMultiply(E scalar)

  def M negate()

  def E tr()

  def E det()

  def Set<Integer> rowIndexes()

  def Set<Integer> columnIndexes()

  def Map<Integer, Map<Integer, E>> rows()

  def Map<Integer, Map<Integer, E>> columns()

  def E entry(Integer rowIndex, Integer columnIndex)

  def Map<Integer, E> row(Integer index)

  def Map<Integer, E> column(Integer index)

  def BigInteger size()

  def int rowSize()

  def int columnSize()

  def boolean square()

  def boolean triangular()

  def boolean upperTriangular()

  def boolean lowerTriangular()

  def boolean diagonal()

  def boolean id()

  def boolean invertible()

  def M transpose()

  def boolean symmetric()

  def boolean skewSymmetric()

  def M minor(Integer rowIndex, Integer columnIndex)

  def Table<Integer, Integer, E> getTable()
}
