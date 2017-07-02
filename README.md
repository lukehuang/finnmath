# bigMath: A math library for the JVM based on BigInteger and BigDecimal

bigMath is a free software library for the JVM which provides or will provide implementations for fractions, 
complex numbers, matrixes and vectors and their arithmetics based on BigInteger and BigDecimal so that this 
library is not prone to buffer overflows and big rounding errors. The great Commons Math uses primitives 
so if you want to achieve great performance then please choose Commons Math instead.

Today bigMath consists of three parts:
* Linear algebra with matrixes and vectors
* Fractions and complex numbers
* A pseudo random generator

bigMath is written in Xtend and uses Gradle as its build automation tool.
Dependencies are Guava, Commons Lang and SLF4J and for tests AssertJ and JUnit.

bigMath is open source and free software and is licensed under the permissive BSD 2-Clause License.

bigMath is still in a very early state and a work in progress.

## Thanks to
* Oracle for the JVM and OpenJDK
* the Eclipse Foundation for Xtend and the Eclipse IDE
* Google for Guava
* the Apache Software Foundation for Groovy, Commons Lang and Commons Math
* QOS.ch for SLF4J 
* Joel Costigliola for AssertJ
* the JUnit team for JUnit
* Linus Torvalds for Git
* Gradle Inc. for Gradle
* Judd Vinet and Aaron Griffin for Arch Linux

