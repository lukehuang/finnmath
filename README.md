# finnMath: Math library for the JVM based on BigInteger and BigDecimal

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

finnMath is written in Xtend and uses Gradle as its build automation tool so it should interop 
seamlessly with other JVM technologies. 
Dependencies are Guava, Commons Lang and SLF4J and for tests AssertJ and JUnit.

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
	gradle publishToMavenLocal

This will clone the remote Git repository, build finnmath and install it into your local Maven repository.

## Developing

I recommend the Eclipse IDE for Xtend development but you are free to use whatever IDE or editor you want.
You will need the Xtend plugin and optional the EGit plugin for Git and the Buildship plugin for Gradle
integration. Push requests are very welcome.

The Eclipse plugin for Gradle is applied to the build so that the command

    gradle eclipse

will create an Eclipse project inside your cloned folder which is ready to import.

The code formatting follows loosely the Google Java style guide found here on GitHub.    

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
* Vyacheslav Rusakov for the Gradle POM plugin
* GitHub Inc. for GitHub
* Judd Vinet and Aaron Griffin for Arch Linux
