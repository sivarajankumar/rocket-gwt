# Motivation #
The primary purpose of the rocket.remoting.client.json package is to make the steps to work with a remote server's json services almost the same as those required by a GWT RPC.

# The Solution #
Many of steps involved in using a RPC service have equivalents within the `rocket.remoting`.
  * Both require their respective value objects to be serializable. This is indicated by different marker interfaces.
  * A service interface must be defined.
  * A Async service interface must also be defined.
  * A proxy is retrieved using deferred binding.
  * The target url must then be set after casting to ServiceDefTarget.
  * A callback is notified of the rpc success.

Both systems take care of the marshalling / unmarshalling required to convert an encoded String to a JavaObject.

The rocket module uses another related module to handle its binding requirements `rocket.json.Json`. Refer to that wiki for more info on preparing JsonSerializable types.

# Getting started #

The following steps below list what and why is needed to use this facility in your own development efforts.

## Importing the rocket module ##

Add the rocket module to your own application module.

```
<inherits name="Rocket.User" />
```

## Creating a GWT Service interface ##

The sample below includes a simple service interface with all required annotations. Each service method must include a number of annotations to note the mechanism used to pass parameters to the servcer.


Two forms of available to pass parameters to the server.

### HttpRequestParameters ###
This simple method simply builds a query string that contains each of the method parameters. Data may be passed using either the GET or POST methods.

#### Method annotations ####
  * @jsonRpc-inputArguments annotation must be set to **requestParameters**.
  * @jsonRpc-httpMethod annotation may be set to either "GET" or "POST"
  * @jsonRpc-parameterName The name of the parameter name. This value is used as the key and the corresponding method argument becomes the value when building the query string.
  * The method return type must be a JsonSerializable type.

Input parameters can only be one of the following simple types
  * boolean
  * byte
  * short
  * int
  * long
  * float
  * double
  * char
  * java.lang.String

#### Creating a Service Interface ####
The code below provides an example of a method that uses the method arguments to builds a url with query string parameters, makes a GET request to the server and then deserializes the response into a Payload instance.
```
package example.client;

import rocket.remoting.client.json.RemoteJsonService;

public interface ServiceInterface1 extends RemoteJsonService{
	/**
	 * @jsonRpc-inputArguments requestParameters
	 * @jsonRpc-httpMethod GET
	 * @jsonRpc-parameterName one
	 * @jsonRpc-parameterName two
	 * @jsonRpc-parameterName three
	 */
	Payload usesRequestParameters( boolean first, int second, String third );
}
```

### JsonRpc ###
This method posts java objects to a json rpc service.
  * @jsonRpc-inputArguments annotation must be set to **jsonRcp**.
  * The service method may have only one parameter which must be JsonSerializable.
  * The method return type must be a JsonSerializable type.

The code below provides an example that uses JsonRpc to post a java object instance and then deserializes the response.

```
package example.client;

import rocket.remoting.client.json.RemoteJsonService;

public interface ServiceInterface2 extends RemoteJsonService{
	/**
	 * @jsonRpc-inputArguments jsonRpc
	 */
	Payload jsonRpc( Arguments );
}
```


## Creating the GWT Async interface counterpart ##

A Async interface must be created for the matching service interface. The rules for the creation of the async interface are identical to those of a GWT RPC Async service interface.

  * Append Async to the service interface name.
  * Copy all public service methods to the async service interface, changing the return type to void and adding a AsyncCallback argument.

The sample below gives the async complement of the ServiceInterface1Async interface previously defined above.
```
package example.client;

import rocket.remoting.client.json.RemoteJsonService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServiceInterface1Async{
	void usesRequestParameters( boolean first, int second, String third, AsyncCallback callback );
}
```



## Invoking the Json service ##

The statements required to execute the remote json service are almost a carbon copy of those required to execute a GWT rpc.

Import statements
```
   // skipped
```


Get a reference to the proxy via deferred binding.
```
ServiceInterface1Async service = (ServiceInterfaceAsync) GWT.create( ServiceInterface1.class );
```


Set the target url.
```
ServiceDefTarget serviceDefTarget = (ServiceDefTarget) service;
serviceDefTarget.setServiceEntryPoint( "targetUrl" );
```


Invoke the service. The given callback will be notified with the response or handle any errors encountered.


```
service.invokeServer( true, 1, "apple", new AsyncCallback(){
	public void onSuccess( final Object result ){
		// cast to Payload
		Payload  payload = (Payload ) result;

		// continue with appropriate logic.
	}

	public void onFailure( final Throwable throwable ){
		// oops handle the throwable which is actually a `rocket.remoting.client.json.RemoteJsonServiceException`
	}

} );

```


# Further samples #

For further examples refer to the unit test.
  * `rocket.remoting.test.remotejsonservice.client.RemoteJsonServiceGwtTestCase`