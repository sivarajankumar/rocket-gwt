# A log4j like logging framework on the client, supporting loggers with individual levels and implementations as well as globally including or excluding all logging statements.

# Logging #
The rocket logging module aims to brings a powerful logging facility to a GWT application, with the important distinction to offer the option to eliminate all logging statements from the generated javascript without any changes or recompilation to your application.  Before going into technical details lets examine some of what it can and cant do.

# Features #
The logging module provides in a simplified form some of the logging features available in Log4J.
  * Retrieve individual loggers by name.
  * Each logger can have a different logging level
  * Each logger can use a different implementation class.
  * A properties file is used as the source of all logging config.
  * No file changes are required to globally enable or disable all logging statements within an application.

## Compiled Javascript ##
When authoring a GWT application code size is a significant issue, therefore most of the time the delivered javascript should not contain any logging statements. For a variety of reasons, it is a valuable problem solving aide to have the ability to execute the application with debugging statements.

  * A query string parameter is used to determine which of the two generated javascript files is downloaded, one with logging statements, the other without. By default users of your application use the smaller, optimal form of the application.
  * Where ever possible logging statements below the threshold of the given logger are eliminated. This binding occurs during the compilation process.

# Getting started #
First inherit `rocket.logging.Logging` module into your application's module xml file.

```
	<inherit name="rocket.logging.Logging" />
```

# Configuration #

## File Location ##
The properties file is located as a resource on the application classpath.
  * By default the name of this file is `/rocket-Logging.properties`.
  * The actual name of the file may be changed by setting the system property `rocket.logging.Logging.config` to a classpath resource.

## File Layout ##
The properties file contains a number of key / value pairs.
  * The key contains the name of the logger (case sensitive).
  * A hierarchical search following Log4J standards is used to resolve a logger to a particular entry.
  * The value contains two values separated by commas, the first the logging level and the second the fully qualified name of a logger implementation.

## Logging levels ##
The list below contains all available logging levels starting from the lowest or most verbose all the way up to none which would result in all messages sent to that logger being ignored. The names of the levels are as follows. Note the names are case-sensitive.
  * Debug - logs all messages
  * Info
  * Warn
  * Error
  * Fatal - Only messages with a level of **Fatal** are logged, others are ignored.
  * None - ignores all messages.

## Properties file example ##

The example file below contains an example using the constructs outlined above.
```
	# Comments go in here...
	#
	example.logging,client.Specific=Debug,rocket.logging.client.SystemLogger
	example=Info,rocket.logging.client.EmailLogger
	#
	# All other unresolved named loggers default to the root entry setting.
	#
	root=Error,rocket.logging.client.GwtLogger
```
A request for "example.logging.client.Specific" will result in a SystemLogger which logs all messages.
A request for "example.xxx" will match the "example" logger and resolve to a hypothetical logger that emails all Info and above level messages. Note an EmailLogger implementation is not actually provided.

## Java ##
The snippet below uses the properties file above.
```
	package example.logging.client;

	import rocket.logging.client.Logger;
	import rocket.logging.client.LoggerFactory;	
	
	...
	
	Logger firstLogger = LoggerFactory.getLogger( "example.logging.client.First");
	Logger secondLogger = LoggerFactory.getLogger( "example.logging.client.Second");	
```

## Available loggers ##
The logging packages come with a number of prebuilt loggers.

The comments below ignore all messages below the configured threshhold for the individual logger. Some logging implementations result in no visible output when outside the hosted mode environment, such loggers contain a _yes_ in the _Hosted Mode only_ column of the table below..

| Logger | Comments | Hosted Mode only |
|:-------|:---------|:-----------------|
| `rocket.logging.client.ConsoleLogger` | Prints all events to System.out/System.err | Yes              |
| `rocket.logging.client.FirebugLogger` | Logs all events to Firebug (FireFox), Firebug lite is required for other browsers or none | No               |
| `rocket.logging.client.GwtLogger` | Logs all events using the GWT Tree logger. | Yes              |
| `rocket.logging.client.ServerService` | Publishes all events to a server service | No               |

### ServerService ###
Sends each and every logging event that is not below the configured threshhold of the logger to the server. Several service implementations are provided one that sends messages to Log4J, Commons Logging and JDK logging. It therefore sensible to coordinate the logging levels between the client and server portions of the application.
In other words, there is no value in sending debug messages for a particular named logger from the client if the log4j config has a level of info as these events would be ignored and result in unnecessary network traffic.

All three service implementations extend the `rocket.logging.server.LoggingServerService` which contains most of the logging logic, with each specialization overriding a single method which creates an adapter within the logging implementation and the service.

The `rocket.logging.client.ServerService` is abstract with a single methods that need to be overriden. The javadoc shown below contains details.

```
	/**
	 * Sub classes must override this method to return the url of the matching
	 * server logging service.
	 * 
	 * @return
	 */
	abstract protected String getServiceEntryPoint();
```

## Authoring a Logger ##
Because the logging module is not linked together with the rocket Beans network no dependency injection services are available, meaning that configuration must be hardcoded within the logger implementation.

  * Loggers must implement `rocket.logging.client.Logger`
  * Loggers must have a public constructor that takes a string.
  * The incoming string must be passed to super class string constructor using super( String );
  * Any configuration must be hardcoded. It is recommended that a further subclass be created and have its constructor set the relevant properties (following the ServerService class as an example).

Because the url of the logging service is unknown by the rocket library, users must sub class and override the ServerLogger.getServiceEntryPoint() method to return the url. This sub class would used in the properties file.

# Futher Samples #
For further examples refer to the unit test and related demos. The LoggingApplication is an interactive demo and is available within the download.

  * `rocket.logging.test.application.client.LoggingApplication`
  * `rocket.logging.test.LoggerFactoryImplTestCase`
  * `rocket.logging.test.LoggingLevelTestCase`
  * `rocket.logging.test.LoggingTestSuite`
  * `rocket.logging.test.PropertiesFileLoggingFactoryConfigTestCase`
  * `rocket.logging.test.loggerfactorygenerator.client.LoggerFactoryGeneratorGwtTestCase`
  * `rocket.logging.test.server.client.ServerLoggerGwtTestCase`
