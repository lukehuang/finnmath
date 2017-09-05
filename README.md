# finnMath: Math library for the JVM based on BigInteger and BigDecimal

[![Build Status](https://travis-ci.org/togliu/finnmath.svg?branch=master)](https://travis-ci.org/togliu/finnmath)

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

finnMath is written in Xtend and uses Gradle as its build automation tool so it should interoperate seamlessly with 
other JVM technologies. Dependencies are Guava, Commons Lang and SLF4J and for tests AssertJ and JUnit.

## Implementation details
* All types are immutable.
* The matrices are based on ImmutableTable from Guava.
* The vectors are based on ImmutableMap from Guava.
* All leaf classes are final.
* Useful hashCode, equals and toString methods thanks to Xtend's Active Annotations
* Builders for vectors and matrices
* Early failing with meaningful messages
* Logging facade instead of a hard dependency on a specific logging framework

finnMath is open source and free software and is licensed under the permissive BSD 2-Clause License.

finnMath is still in a very early state and a work in progress.

## Building

#### Prerequisites
* JDK
* Gradle
* Git (optional)
#
    git clone https://github.com/togliu/finnmath.git
    cd finnmath
    gradle install

This will clone the remote Git repository, build finnmath and install it into your local Maven repository.

## Developing

I recommend the Eclipse IDE for Xtend development but you are free to use whatever IDE or editor you want.
You will need the Xtend plugin and optional the EGit plugin for Git and the Buildship plugin for Gradle
integration. Push requests are very welcome.

The Eclipse plugin for Gradle is applied to the build so that the command

    gradle eclipse

will create an Eclipse project inside your cloned folder which is ready to import.

The code formatting follows loosely the Google Java style guide found here on GitHub.    

#### Why finnMath?
I searched for a mathematical library for another project which is based on BigInteger and BigDecimal instead of 
primitives and I could not find one.

#### Why BigInteger or BigDecimal?
Both are protected against buffer overflows and BigDecimal is by far less prone to big rounding errors. In addition, 
you are able to control the rounding behaviour and the scale in the case of BigDecimal. The downsides of using them 
are the impact on the runtime speed and the cumbersome syntax in Java. 

#### Why Xtend?
I want to write a library which is usable from all JVM languages. Almost every JVM language tries to work seamlessly 
with Java. I do not expect the developers to test the interoperability with all the other languages besides Java and 
it would be presumptuous to expect that. So Java would be a wise choice but then a colleague mentioned Xtend. I chose 
Xtend because it compiles to Java source code instead of bytecode and therefore it exists no mismatch between Java 
and Xtend. Further it provides wonderful syntactic sugar for the arithmetic operations of BigInteger and BigDecimal 
and the great active annotations to eliminate boilerplate code similar to Lombok. 

#### Why AssertJ?
I find its assertions more readable in comparison to JUnit and Hamcrest and it is more IDE friendly.

#### Why Gradle?
Its DSL is less verbose than Maven's XML approach and thanks to Groovy customizing of all the tasks involved in the 
build process is very comfortable. I want to point out that conventions are a very good thing that simplifies a 
developer's life extremely and thanks to Maven convention over configuration arrived at the Java world.

## Thanks to
* Oracle for the JVM, Java and OpenJDK
* the Eclipse Foundation for Xtend and the Eclipse IDE
* Google for Guava
* the Apache Software Foundation for Groovy, Commons Lang and Commons Math
* QOS.ch for SLF4J 
* Joel Costigliola for AssertJ
* the JUnit team for JUnit
* Linus Torvalds for Git
* Gradle Inc. for Gradle
* the University of Maryland for FindBugs
* the developers of JaCoCo and EclEmma
* GitHub Inc. for GitHub
* Travis CI GmbH for Travis CI
* Stack Exchange Inc. for Stack Overflow
* Judd Vinet and Aaron Griffin for Arch Linux
