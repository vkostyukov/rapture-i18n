[![Build Status](https://travis-ci.org/propensive/rapture-i18n.png?branch=master)](https://travis-ci.org/propensive/rapture-i18n)

# Rapture Internationalization (I18N)

Rapture I18N is a very simple library for working with typesafe localized
strings in Scala. It is part of the [Rapture](http://rapture.io/) project.

## Features

 - Simple format for writing language-specific string literals
 - Internationalized Strings' types reflect the languages they support
 - Strings are typechecked to ensure that strings for all languages are provided
 - Support for language-dependent string substitution
 - Internationalized strings may be treated as normal strings by specifying a
   default language
 - Convenient trait provided for writing 

## Tests

Tests for Rapture I18N are in their own project, [Rapture I18N
Tests](https://github.com/propensive/rapture-i18n-test)

## Availability

Rapture I18N 1.2.0 is available under the *Apache 2.0 License* from Maven Central
with group ID `com.propensive` and artifact ID `rapture-i18n_2.11`.

### SBT

You can include Rapture I18N as a dependency in your own project by adding the
following library dependency to your build file:

```scala
libraryDependencies ++= Seq("com.propensive" %% "rapture-i18n" % "1.2.0")
```

### Maven

If you use Maven, include the following dependency:

```xml
<dependency>
  <groupId>com.propensive</groupId>
  <artifactId>rapture-i18n_2.11</artifactId>
  <version>1.2.0</version>
</dependency>
```

### Building from source with SBT

To build Rapture I18N from source, follow these steps:

```
git clone git@github.com:propensive/rapture-i18n.git
cd rapture-i18n
sbt package
```

If the compilation is successful, the compiled JAR file should be found in the
directory for the appropriate Scala version in the `target` directory.

## Status

Rapture I18N is *experimental*. This means that the API is undergoing change,
and new features may be added or removed without changes being documented.

## Contributing

Rapture I18N -- like all the Rapture projects -- openly welcomes contributions!
We would love to receive pull requests of bugfixes and enhancements from other
developers. To avoid potential wasted effort, bugs should first be reported on
the Github issue tracker, and it's normally a good idea to talk about
enhancements on the Rapture mailing list before embarking on any development.
Alternatively, just send Jon Pretty (@propensive) a tweet to start a
conversation.

# Using Rapture I18N

First of all, import the following:

```
import rapture.i18n._
```

You can write an internationalized string by writing `en"Hello"` or
`fr"Bonjour"`. These have types I18nString[En] and I18nString[Fr], representing
English and French strings, respectively.

More importantly, you can define a single value representing an
internationalized string using the alternation operator (`|`), like this:

```
val greeting = en"Hello" | fr"Bonjour" | de"Guten Tag"
```

The value `greeting` here represents a string in three different languages, and
has the type `I18nString[En with Fr with De]`. Note that the `with` combinator
represents a type intersection, which is the chosen implementation for
combining multiple language types in a single type parameter.

Given a value such as `greeting`, we can access the string for a language of
our choice, simply by applying it as a type parameter to the identifier, e.g.
`greeting[En]` will produce `"Hello"`, whereas `greeting[De]` will produce
`"Guten Tag"`.

## Subtyping

Types for internationalized strings behave such that a value may overspecify
language strings for a given type, for example

```
val msgs: I18nString[En with Fr] = fr"Bonjour" | en"Hello" | de"Guten Tag"
```

but may not underspecify language strings, so this is not valid:

```
val msgs: I18nString[En with Fr] = en"Hello" | de"Guten Tag"
```

and will result in a compile error.

```
error: type mismatch;
 found   : rapture.i18n.I18nString[rapture.i18n.De]
 required: rapture.i18n.I18nString[rapture.i18n.Fr]
       val msgs: I18nString[En with Fr] = en"Hello" | de"Guten Tag"
                                                      ^
```

This helpfully indicates the missing language.

## Substitution

Internationalized strings may be specified with interpolated substitutions,
which can themselves be either ordinary `String`s or other internationalized
strings. In the latter case, the internationalized string must provide the
language of the internationalized string it is being substituted into, for
example this is valid:

```
val greeting = en"Hello" | fr"Bonjour"
en"In England we greet people with '$greeting'."
```

but not this,

```
val greeting = en"Hello" | fr"Bonjour"
de"In Deutschland sagen wir '$greeting'"
```

which is another compile error.

## Default langauges

It is often useful to pick a single language for use within some scope, and use
internationalized strings as if they are ordinary `String`s. This is achievable
by importing a default language, e.g.

```
import languages.es._
```

An internationalized string may then be used in place of an ordinary `String`, like so:

```
import languages.de._
val errorMsg = en"Oh no!" | de"Ach nein!"
throw new Exception(errorMsg)
```

which will throw an exception with the German error message.

Without the import of a default language, this would produce a type error at
compile time. Beware, though, of methods like `+` on `String` which simply call
`toString` on the operand, and consequently do not enforce the typesafe
conversion to the correct language string. These methods should be avoided.

## Standard methods

The `toString` method on `I18nString`s is implemented such that it should
quickly be obvious that the string is an internationalized, rather than an
ordinary, string. For a given internationalized string, `toString` will display
two-letter codes for all the languages it provides, followed by the contents of
the string in English, if available, or in some other language, if not.

For example,

```
scala> fr"Bonjour" | en"Hello"
res: I18nString[Fr with En] = fr|en:"Hello"
```

Additionally `hashCode` and `equals` are implemented such that `I18nString`s
can be usefully compared. Internationalized strings are treated as sets, so
reorderings are not significant.

