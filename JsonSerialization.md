# Motivation #

GWT currently provides the very powerful and yet simple RPC mechanism that seemlessly brings rpc with java object serialization to browser based fat clients. This however has a limitation in that the backend must also be a j2ee server. In order to invoke json services developers must manually marshal json values to and from java objects.

# The Solution #
The rocket.json package includes a simple automate the mapping between java objects and json.

The module supports the following mappings between json to java.

| JSON Type | Possible Java Type equivalents |
|:----------|:-------------------------------|
| objects   | java instances or a Map composed of key/value pairs |
| arrays    | become a List or Set of instances |
| numbers   | become any of the numeric primitive `byte, short, int, long, float, or double`. |
| strings   | `java.lang.String` or chars(which are actually useless but included for completeness) |
| booleans  | `booleans`                     |

## Marking a class as serializable ##

Following in java / gwt tradition a marker interface must be implemented by classes that wish to be serializable. In the case of the rocket library this is `rocket.json.client.JsonSerializable`.


_By design the `java.io.Serializable` and `com.google.gwt.user.client.rpc.IsSerializable` are already mapped to a different Generator which produces (de)serializers for the GWT rpc subsystem._


## Fields annotations ##

The `rocket json` package needs some help in mapping a json stream into a java object.
Only non transient, instance fields will be serializable. The visibilty of the field does not affect its ability to be (de)serialized.


_The generator (`rocket.json.rebind.JsonSerializerGenerator`) uses JSNI to set each of the individual fields belonging to the instance being built. Because JSNI code is used field visibility is not an issue, and setters are not required._

### Json objects ###
Each json object which is ultimately made up of key/value pairs maps to a single java type. Each field belonging to the type must use annotations to note which json object property it is mapped to.

```
	/**
	 * Maps a value referened by the "bar" key to the foo field for the enclosed class.
	 *
	 * @jsonSerialization-javascriptPropertyName bar
	 */
	private String foo;
```

### Json array ###
An annotation must be included to tell the system the type of each element of the list or set.
```
	/**
	 * Something about the foo field..
 	 *
	 * The elements of the foo json array will be interpretted as Strings.
	 *
	 * @jsonSerialization-listElementType <java.lang.String>
	 */
	private String foo;
```

# Getting started #

The following steps below list what and why is needed to use this facility in your own development efforts.


## Importing the rocket module ##

Add the rocket module to your own project module.

```
<inherits name="Rocket.User" />
```

## Sample Json ##
The sample below includes a simple json object graph which includes all json types. It will be used as the basis for the contrived Java classes created below.

```
	{
		"foo":	true;           // json boolean
		"bar":  123;  	        // json number
		"array": [ 1, 2, 3 ];	// json array
		"object" {
		   "baz":  "apple"; 	// json string
		}
	}
```

## Annotating the bound types ##

Two separate classes are required, one for each json object present in the convulted example given above.
The outter object includes 4 properties.

  * foo
  * bar
  * array
  * object

The code snippet below includes all the fields and required annotations that would be required to map the outter object to a class called
OutterClass.


```
package example;
import rocket.json.client.JsonSerializable;

public class OutterClass implements JsonSerializable{
	/**
	 * @jsonSerialization-javascriptPropertyName foo
	 */
	private boolean foo;

	/**
	 * @jsonSerialization-javascriptPropertyName bar
	 */
	private int number;

	/**
	 * @jsonSerialization-javascriptPropertyName array
	 * @jsonSerialization-listElementType <java.lang.Integer>
	 */
	private java.util.List number;

	/**
	 * @jsonSerialization-javascriptPropertyName object
	 */
	private InnerClass innerClass;
}
```


The inner object includes 1 property.

  * baz


The code snippet below includes all the fields and required annotations that would be required to map the inner object to a class called InnerClass.
```
package example;
import rocket.json.client.JsonSerializable;

public class InnerClass implements JsonSerializable{
	/**
	* @jsonSerialization-javascriptPropertyName baz
	*/
	private String baz;
}
```

## Binding json to and from a java object instance ##

Include required import statements.
```
import rocket.json.client.JsonSerializer;
```


To de serialize a json object to java a reference a JsonSerializer must obtained using deferred binding.

```
JSONObject jsonObject = // already got it from somewhere...
JsonSerializer serializer = (JsonSerializer) GWT.create( SomeClass.class );
```


Then proceed to ask the serializer to build a java object from the given json object.
A `rocket.json.client.JsonDeserializationException` (its not a checked exception) will be thrown if anything goes wrong.

```
SomeClass instance = (SomeClass) serializer.writeJson( jsonObject );
```

To serialize from java back to json is quite simple as demonstrated by the example below.
```
JSONValue json = serializer.writeJson( instance );
```


# Further samples #

For further examples refer to the unit test
  * `rocket.json.test.client.JsonGwtTestCase`

