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

package bigmath.linear

import com.google.common.collect.Table
import java.math.BigDecimal
import java.util.Map
import org.apache.commons.lang3.builder.Builder

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

final class DecimalMatrix extends Matrix<DecimalMatrix, BigDecimal, DecimalVector> {
    protected new(Table<Integer, Integer, BigDecimal> table) {
        super(table)
    }

    override add(DecimalMatrix summand) {
    }

    override subtract(DecimalMatrix subtrahend) {
    }

    override multiply(DecimalMatrix factor) {
    }

    override multiplyVector(DecimalVector vector) {
    }

    override multiplyRowWithColumn(Map<Integer, BigDecimal> row, Map<Integer, BigDecimal> column) {
    }

    override scalarMultiply(BigDecimal scalar) {
    }

    override negate() {
    }

    override tr() {
    }

    override det() {
    }

    override square() {
        false
    }

    override triangular() {
        false
    }

    override upperTriangular() {
        false
    }

    override lowerTriangular() {
        false
    }

    override diagonal() {
        false
    }

    override id() {
        false
    }

    override invertible() {
        false
    }

    override transpose() {
        this
    }

    override symmetric() {
        false
    }

    override skewSymmetric() {
        false
    }

    override minor(Integer rowIndex, Integer columnIndex) {
        this
    }

    static def builder(int rowSize, int columnSize) {
        checkArgument(rowSize > 0, 'expected row size > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected column size > 0 but actual %s', columnSize)
        new DecimalMatrixBuilder(rowSize, columnSize)
    }

    static class DecimalMatrixBuilder extends MatrixBuilder<DecimalMatrixBuilder, DecimalMatrix, BigDecimal> implements Builder<DecimalMatrix> {
        private new(int rowSize, int columnSize) {
            super(rowSize, columnSize)
        }

        override build() {
            table.cellSet.forEach [
                requireNonNull(value, 'value')
            ]
            new DecimalMatrix(table)
        }
    }
}
