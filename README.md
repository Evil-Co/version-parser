Java Version Parser [![Build Status](http://assets.evil-co.com/build/JVPL-MASTER.png)](http://www.evil-co.com/ci/browse/JVPL-MASTER)
===============
This library provides a simple way to parse, compare and export human readable version numbers.

Examples
--------

You can find basic usage examples for the library in the ```src/test/java``` directory.

Compatible Version Formats
--------------------------

The library supports most popular version schemes:
* X
* X.X
* X.X.X
* X.X.X.X
* X.X-EXTRA
* X.X.X-EXTRA
* X.X.X.X-EXTRA

Extra may be one of the following values: ALPHA, A, BETA, B, SNAPSHOT and RC (all cases are supported). All of these
suffixes (except SNAPSHOT) may add an additional version (as example RC-2, RC-3 and so on). If no such version suffix is
provided the library will assume sub-version 0.

Compiling
---------

You need to have Maven installed (http://maven.apache.org). Once installed,
simply run:

	mvn clean install

Maven will automatically download dependencies for you. Note: For that to work,
be sure to add Maven to your "PATH".

Maven
-----

You can include this library into your projects easily by adding the following repository and dependency
to your project:

	<!- ... -->

	<repository>
		<id>evil-co</id>
		<url>http://nexus.evil-co.org/content/repositories/free/</url>
	</repository>

	<!- ... -->

	<dependency>
		<groupId>com.evilco</groupId>
		<artifactId>version</artifactId>
		<version>1.0.0</version>
	</dependency>

	<!- ... -->

Contributing
------------

We happily accept contributions. The best way to do this is to fork the project
on GitHub, add your changes, and then submit a pull request. We'll look at it,
make comments, and merge it into the project if everything works out.

By submitting code, you agree to license your code under the Apache 2.0 License.