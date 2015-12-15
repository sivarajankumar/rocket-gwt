# Motivation #

Currently GWT contains the very useful RPC mechanism, as a means to communicate with services hosted on a J2EE server. For circumstances where it is more desirable to wait for the server to push rather than poll the comet technique is surprisingly useful. Because of the way comet is typically implemented as a long lived HTTP connection, an application server with a lot of comet clients will quickly have its thread pool exhausted.

# Solution #
The rocket framework combines the object serialization support provided by GWT and its own client and server glue to provide a comet solution. A number of customisations were required to make all the browsers work and this is taken care of. Each time a new object is pushed down the callback registered on the client is provided with the deserialized incoming object. Behind the scenes the client takes care of other issues such as reestablishing broken connections and so on.

## Technical notes ##
  * A comet session is started with the creation of a hidden iframe which connects to the comet server servlet.
  * The servlet periodically pushes either serialized objects or heartbeats. The iframe page gradually continues to grow due to the incoming data.
  * Every so often the server disconnects, so the client can close the iframe in order to reclaim memory.
  * The client will attempt to reconnect automatically when a disconnect occurs.
  * Any java servlet container including tomcat may be used.

# Getting started #

The following steps below list what and why is needed to use this facility in your own development efforts.

## Importing the rocket module ##
Add the rocket module to your own application module.

```
<inherits name="rocket.remoting.Remoting" />
```

## Authoring the server component ##

A servlet must be created which extends `rocket.remoting.server.CometServerServlet` and also implements the `poll()` method. The poll method is responsible for blocking and also  pushing new objects to the client via the push method. The server may also terminate a comet session by calling terminate().

It is recommended that the poll method block for a short but not too long period time in such a manner as too avoid creating a a busy loop.

### Web Container Configuration ###
The web.xml fragment below gives an example of how to configure a Comet servlet.

```
<servlet>
	<servlet-class>rocket.remoting.test.comet.server.TestCometServerServlet</servlet-class>
	<servlet-name>TestCometServlet</servlet-name>
	
	<init-param>
		<param-name>maximum-bytes-written</param-name>
		<param-value>65536</param-value>
	</init-param>
  
	<init-param>
		<param-name>connection-timeout</param-name>
		<param-value>60000</param-value>
	</init-param>
  
</servlet>

<servlet-mapping>
	<servlet-name>TestCometServlet</servlet-name>
	<url-pattern>/comet</url-pattern>
</servlet-mapping>
```

The above xml demonstrates several important aspects being confugured.
  * Associate the comet servlet with a logical name.
  * The maximum-bytes-written parameter controls the number of bytes that are written before a comet http connection is recycled (closed and reopened).
  * The connection-timeout parameter controls how long a http connection is kept open before it is recycled (closed and reopened).
  * The comet servlet must be mapped to a known url. This url will be used by the client.

Because a hidden iframe is used to the time and amount of data accumulated by the hidden iframe page must be managed and reclaimed. In the case of the above example any http connection is closed after 60 seconds or 65536 bytes are written, which ever comes first.

## Authoring the client component ##

A number of steps must be performed to complete
  * Create a class that extends `rocket.remoting.client.CometClient`.
  * Create an abstract method called createProxy with annotation that communicates the payload type.
  * Register a `rocket.remoting.client.CometCallback` with the client to handle any incoming payload, server initiated termination or general failures (exceptions)exceptions.

# Sample #

The sample below shows a comet client and server.

## The model ##

```
package example.client;

public CometPayload{
	CometPayload( int counter ){
		this.counter = counter;
	}
	
	public int getCounter(){
		return counter;
	}
}
```

## The Server ##
The sample shows a server class that continually increments a counter pausing for a second between each query.
```
package example.server;

import rocket.remoting.server.comet.CometServerServlet;

public SampleCometServletServlet extends CometServerServlet{

	protected void poll( CometConnection cometConnection ){		
		try{
			Thread.sleep( 1000 );
		} catch ( InterruptedException ignore ){
		}
		cometConnection.push( new CometPayload( this.counter++ ));
	}

        int counter; 

}
```

## The client ##

A number of types must be created as part of the process of creating a comet client.
### Sample ###

The sample class below includes an example of the implemented abstract CometClient method.
The client may be used to control a single comet session via the `start()` and `stop()` methods.
```
package example.client;

import rocket.remoting.client.CometClient;
import rocket.remoting.client.CometServerConnectionFailureException;

/**
 * @comet-payloadType example.client.CometPayload
 * This has been promoted to a class annotation.
 */

public ExampleCometClient extends CometClient{
};
```

### Starting a comet session ###

The code sample below shows how to create and start a comet session with the server.

```
	ExampleCometClient client = (ExampleCometClient)GWT.create( ExampleCometClient.class );
	client.setServiceEntryPoint( "servlet-url" );
	client.setCallback( new CometCallback(){
		public void onPayload( final Object incoming ){
			CometPayload payload = (CometPayload) incoming;
			// do something with payload...
		}

		public void onFailure( final Throwable cause ){
			// handle problem
		}


		public void onTerminate(){
			// server has terminated session...
		}
	});
	client.start();
```

# Further samples #

For further examples refer to the unit test.
  * client - `rocket.remoting.test.comet.client.CometTest`
  * server - `rocket.remoting.test.comet.server.TestCometServerServlet`

To run the comet demo the embedded tomcat must be running.