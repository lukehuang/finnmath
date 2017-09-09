/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, Lars Tennstedt
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
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package finnmath.number

import com.google.common.annotations.Beta
import java.math.BigDecimal
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static java.util.Objects.requireNonNull

/**
 * Represents the scientific notation for a decimal number which uses {@link BigDecimal} as type for its coefficient 
 * and {@code int} as type for its exponent
 * <p>
 * decimal = coefficient * 10^exponent
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@EqualsHashCode
@ToString
final class ScientificNotation {
    /**
     * Coefficient of this scientific notation
     */
    @Accessors
    val BigDecimal coefficient

    /**
     * Exponent of this scientific notation
     */
    @Accessors
    val int exponent

    /**
     * Constructs a {@link ScientificNotation} from the given coefficient and exponent
     * 
     * @param coefficient the coefficient
     * @param exponent the exponent
     * @throws NullPointerException if {@code coefficient == null}
     * @since 1
     * @author Lars Tennstedt
     */
    new(BigDecimal coefficient, int exponent) {
        this.coefficient = requireNonNull(coefficient, 'coefficient')
        this.exponent = exponent
    }

    /**
     * Returns a string representation of this {@link ScientificNotation}
     * 
     * @return The string representation
     * @since 1
     * @author Lars Tennstedt
     */
    def asString() {
        if (exponent < 0)
            return '''«coefficient» * 10^(«exponent»)'''
        else if (exponent === 0)
            return '''«coefficient»'''
        else if (exponent === 1)
            return '''«coefficient» * 10'''
        '''«coefficient» * 10^«exponent»'''
    }
}
