# finnMath: Math library for the JVM based on BigInteger and BigDecimal

[![Build Status](https://travis-ci.org/togliu/finnmath.svg?branch=master)](https://travis-ci.org/togliu/finnmath)
[![codecov](https://codecov.io/gh/togliu/finnmath/branch/master/graph/badge.svg)](https://codecov.io/gh/togliu/finnmath)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://shields.io)

finnMath is a free software library for the JVM which provides or will provide implementations for fractions, 
complex numbers, matrices and vectors and their arithmetics based on BigInteger and BigDecimal so that this 
library is not prone to buffer overflows and big rounding errors. The great Commons Math uses primitives 
so if you want to achieve best performance then please choose Commons Math instead.

What finnMath will provide:
* Implementations for matrices and vectors
* Implementations for fractions and complex numbers
* A pseudo random generator for all these implementations
* A square root calculator for BigInteger and BigDecimal based on Heron's method

What finnMath will not provide:
* Solver for equations
* Matrix decomposition, reduction or factorization
* Polynomials

finnMath is written in Java and uses Maven as its build automation tool so it should interoperate seamlessly with 
other JVM technologies. Dependencies are Guava, Commons Lang and SLF4J and for tests AssertJ and JUnit.

## Implementation details
* All types are immutable.
* The matrices are based on ImmutableTable from Guava.
* The vectors are based on ImmutableMap from Guava.
* All leaf classes are final.
* Useful hashCode, equals and toString methods
* Builders for vectors and matrices
* Early failing with meaningful messages
* Logging facade instead of a hard dependency on a specific logging framework

## Javadoc and Maven site
* Javadoc: [link https://togliu.github.io/finnmath/javadoc/index.html]
* Maven site: [link https://togliu.github.io/finnmath/site/index.html]

finnMath is open source and free software and is licensed under the permissive Apache License.

finnMath is still in a very early state and a work in progress.

## Building

#### Prerequisites
* JDK
* Maven
* Git (optional)
#
    git clone https://github.com/togliu/finnmath.git
    cd finnmath
    mvn install

This will clone the remote Git repository, build finnmath and install it into your local Maven repository.

## Developing

The code formatting follows loosely the Google Java style guide found here on GitHub.    

#### Why finnMath?
I searched for a mathematical library for another project which should be based on BigInteger and BigDecimal instead 
of primitives and I could not find one.

#### Why BigInteger or BigDecimal?
Both are protected against buffer overflows and BigDecimal is by far less prone to big rounding errors. In addition, 
you are able to control the rounding behaviour and the scale in the case of BigDecimal. The downsides of using them 
are the impact on the runtime speed, the higher memory consumption and the cumbersome syntax. 

#### Why AssertJ?
I find its assertions more readable in comparison to JUnit and Hamcrest and the fluent assertions are more IDE 
friendly.

## Thanks to
* Oracle for the JVM, Java and OpenJDK
* the Eclipse Foundation for the Eclipse IDE
* JetBrains s.r.o. for IntelliJ IDEA Community Edition
* Google for Guava and Error Prone
* the Apache Software Foundation for Maven, Commons Lang, Commons Math and the Apache License
* QOS.ch for SLF4J 
* Joel Costigliola for AssertJ
* the JUnit team for JUnit
* Linus Torvalds for Git
* Thomas Broyer for the Gradle Error Prone plugin
* the developers of Checkstyle
* the University of Maryland for FindBugs
* the developers of PMD
* the developers of JaCoCo and EclEmma
* GitHub Inc. for GitHub
* Travis CI GmbH for Travis CI
* Codecov LLC for Codecov
* Stack Exchange Inc. for Stack Overflow
* Shields.io for Shields.io
* Judd Vinet and Aaron Griffin for Arch Linux
