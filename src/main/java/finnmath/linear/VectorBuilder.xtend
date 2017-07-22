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

package finnmath.linear

import com.google.common.annotations.Beta
import java.util.HashMap
import java.util.Map
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@FinalFieldsConstructor
abstract class VectorBuilder<B, V, E> {
  @Accessors
  protected val Map<Integer, E> map = new HashMap

  private val Integer size

  def put(E entry) {
    requireNonNull(entry, 'entry')
    val index = map.size + 1
    checkArgument(map.size < size, 'expected index in [1, %s] but actual %s', size, index)
    map.put(index, entry)
    this
  }

  def put(Integer index, E entry) {
    requireNonNull(index, 'index')
    requireNonNull(entry, 'entry')
    checkArgument(0 < index && index <= size, 'expected index in [1, %s] but actual %s', size, index)
    map.put(index, entry)
    this
  }

  def putAll(E entry) {
    requireNonNull(entry, 'entry')
    (1 .. size).forEach [
      map.put(it, entry)
    ]
    this
  }

  def V build()
}