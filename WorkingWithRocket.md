#Getting started and understanding how, why and where things are like they are...
# Motivation #
Developers new to GWT or rocket need a better understanding of how, why and where things are in the rocket project. It also includes coding standards used as a guide to better understand how things are put together.

# Details #
All classes follow a number of simple rules.

## Using ##
To use anything thats bundled with the rocket library.
  * Add rocket.jar to your project's classpath (before gwt-user.jar).
  * Add rocket-stacktrace.jar (before gwt-user.jar) to include support for web mode stacktraces.
  * Inherit rocket.User
```
<inherits name="rocket.User" />
```

## Packaging ##
  * client Contains classes that ultimately are translated to javascript.
  * rebind Contains any generators that assist client counterparts. These are typically accessed using deferred binding with an appropriate class literal.
  * server Contains the server side counterpart that work in tandem with a client.
  * test contains the corresponding tests for the component. To find tests for a client class navigate to the corresponding test subpackage. Tests are named to match the client component.

## Classes ##
  * If a component consists of more than a single (a few) classes move them into their own sub package.
  * Many components are templates that require the developer to override and fill in the gaps.
  * Never try and do too much in a single class.
  * Keep classes small with a single purpose so they can be assembled into larger artefacts.

## Constants ##
  * Never define constants in interfaces or classse.
  * All constants that should not be visible outside a package are placed in a class called **Constants**.
  * If some constants should be visible outside the package name the class XXXConstants where XXX is the module name.
  * Always provide a toString() that dumps important fields.
  * If possible try and make helpers package private.

## Constructors ##
  * Classes typically have no arguments constructors.

## Fields ##
  * Fields are always private.
  * Never directly access fields, use the getter or setter.
  * Initialize fields via a factory.
  * Use interface references whereever possible.

## Methods ##
  * Always test incoming parameters - aka defensive programming -> use rocket.client.util.XXX.check
  * Avoid code duplication, move reusable code into a helper or super class.
  * Methods are categorised into two parts, methods that manipulate values and objects and other methods that coordinate a process.
  * Never group unrelated manipulations in a single method. Break up them into multiple independent methods. This makes it easy to sub class to change behaviour.
  * Always use factories whereever possible rather than new XXX.
  * Always try and make minimum number of methods public.
  * Make method names descriptive.
  * Name asserting methods checkXXX.
  * Methods should never be more than a screen full in length.
  * Make all jsni (javascript native interface) methods private with a corresponding regular java method to check parameters etc. Never hit the jsni method go via its java guard.

## References ##
  * Always try and make variable and parameter references final.
  * Always try and use interface references rather than concrete types.

## Tests ##
  * All reusable components come with a test. Tests also come with a matching eclipse xxx.launch file.
  * The test suite [http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/test/RocketTestSuite.java RocketTestSuite ](.md) contains all tests.
  * To code interactive tests consider using rocket.testing.client.WebPageTestRunner

# Components #
  * When starting out look at the corresponding test for the component.

# Feedback #
  * If anything doesnt work, run the corresponding test or check it as a reference.
  * Post to the [http://groups.google.com/group/rocket-gwt forum ](.md) if you have any questions, comments...