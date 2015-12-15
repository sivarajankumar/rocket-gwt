# Motivation #

GWT already provides serialization of objects to and from the server with its RPC module.

## GWT RPC Weaknesses ##
  * Make serialization publicly available without hacks or modifying the current RPC sub system.
  * It is not possible to backlist serializable types from ever been candidates for serialization.
  * A graph serialized on the client cannot be deserialized back by the client.
  * Type merging in both directions client 

&lt;-&gt;

 server and vice versa.
  * It is not possible to write a single serializer for multiple types.

### Type merging ###
Type merging is the process where one attempts to merge instances of one type into another type. A perfect example is the possibility to serialize any List from the server so that it appears as a
ArrayList on the client. The same is also true on the client. Currently it is not possible to serialize any of the anonymous List implements from methods such as java.util.Arrays.asList(Object[.md](.md))
from the client to the server. To get around this the developer must copy the anonymous List implementation into an ArrayList and then serialize that.

## The solution ##
### SerializationFactory ###
A serialization factory class is publicly available. A developer may specify the types to be written and read using annotations. Only types reachable from those listed may be serialized from the
provided serialization factory.


  * Create an interface that extends rocket.serialization.client.SerializationFactoryComposer
  * The interface should not include any methods.
  * For each that represents the root of a heiarchy that should be read/written use the Serialization-readableTypes and Serialization-writableTypes annotations.

```
  	package examples;
  	
  	/**
  	 *
  	 * @Serialization-readableTypes examples.SerializableType
  	 * @Serialization-writableTypes examples.AnotherSerializableType
  	 */
  	interface MySerializationFactory extends rocket.serialization.client.SerializationFactory{
  	
  	}
```

Types that include any collection type fields must include additional annotations to denote their contents.

| Collection type | Annotation | Comment |
|:----------------|:-----------|:--------|
| java.util.List  | @Serialization-listElementType | List element type |
| java.util.Set   | @Serialization-setElementType  | Set element type  |
| java.util.Map   | @Serialization-mapKeyType      | Map key type      |
| java.util.Map   | @Serialization-mapValueType    | Map value type    |

### An example ###
The code below illustrates a type that holds a single string value that will be serialized.
```
	package example
	
	public class SerializableType implements java.io.Serializable{
		String string;
	}
```

The code below has been prepared to provide a SerializationFactory that may be used to serialize or deserialize any SerializableType instance.

```
  	package examples;
  	
  	/**
  	 *
  	 * @Serialization-readableTypes examples.SerializableType
  	 * @Serialization-writableTypes examples.SerializableType
  	 */
  	interface MySerializationFactory extends rocket.serialization.client.SerializationFactoryComposer{
  	
  	}
```

The code below includes two steps one that serializes an instance and another that reads the instance back.
The values of the two will be identical.

```
	package examples;
	
	...
	
	void writeThenReadBack(){
		// create serialization factory instance...
		final SerializationFactory factory = (SerializationFactory) GWT.create( MySerializationFactory.class );
		
		// create the instance...
		final SerializableType instance = new SerializableType();
		instance.string ="Apple";
		
		// grab an ObjectOutputStream to serialize...
		final rocket.serialization.client.ObjectOutputStream output = factory.createObjectOutputStream();
		output.writeObject( instance );
		
		// get the serialized stream as text...
		final String stream = output.getText();
		
		// read a *clone* of instance back...
		final rocket.serialization.client.ObjectInputStream input = factory.createObjectInputStream( stream );
		final SerializedType clone = (SerializableType) input.readObject();
		
		// the string field should be equal
		System.out.println( instance.string.equals( clone.string ) ); // should print true...
		
	}

```

## Stream format ##

The stream format is json encoded array, with the first portion containing the string table and the second part of the array values.

  * String table
    * The number of strings in the table (all Strings appear within the String table including serialized instances).
    * n individual strings composing the table
  * Data
    * Reference
      * 0 - null instance
      * 1 - new instance - New instances are added to a seen map. The first instance is given a backreference of -1, the next -2 and so on working away from 0.
    * Fields are written for the most dervied type first after sorting them alphabetically. This process continues for each super type until Object is hit.
      * + 2+ - string table reference. Take 2 from value to locate String within String table.
      * + < 0 - object backreference.

## Unit/Functional tests ##

For further examples refer to the unit and functional tests that accompany the distribution.

  * rocket.serialization.test.SerializationTestSuite
  * rocket.serialization.test.client.reader.BooleanArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.BooleanReaderGwtTestCase
  * rocket.serialization.test.client.reader.ByteArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.ByteReaderGwtTestCase
  * rocket.serialization.test.client.reader.CharArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.CharacterReaderGwtTestCase
  * rocket.serialization.test.client.reader.DateReaderGwtTestCase
  * rocket.serialization.test.client.reader.DoubleArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.DoubleReaderGwtTestCase
  * rocket.serialization.test.client.reader.FloatArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.FloatReaderGwtTestCase
  * rocket.serialization.test.client.reader.IntArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.IntegerReaderGwtTestCase
  * rocket.serialization.test.client.reader.ListReaderGwtTestCase
  * rocket.serialization.test.client.reader.LongArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.LongReaderGwtTestCase
  * rocket.serialization.test.client.reader.MapReaderGwtTestCase
  * rocket.serialization.test.client.reader.SetReaderGwtTestCase
  * rocket.serialization.test.client.reader.ShortArrayReaderGwtTestCase
  * rocket.serialization.test.client.reader.ShortReaderGwtTestCase
  * rocket.serialization.test.client.reader.ThrowableReaderGwtTestCase
  * rocket.serialization.test.client.writer.BooleanArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.BooleanWriterGwtTestCase
  * rocket.serialization.test.client.writer.ByteArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.ByteWriterGwtTestCase
  * rocket.serialization.test.client.writer.CharArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.CharacterWriterGwtTestCase
  * rocket.serialization.test.client.writer.DateWriterGwtTestCase
  * rocket.serialization.test.client.writer.DoubleArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.DoubleWriterGwtTestCase
  * rocket.serialization.test.client.writer.FloatArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.FloatWriterGwtTestCase
  * rocket.serialization.test.client.writer.IntArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.IntegerWriterGwtTestCase
  * rocket.serialization.test.client.writer.ListWriterGwtTestCase
  * rocket.serialization.test.client.writer.LongArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.LongWriterGwtTestCase
  * rocket.serialization.test.client.writer.MapWriterGwtTestCase
  * rocket.serialization.test.client.writer.SetWriterGwtTestCase
  * rocket.serialization.test.client.writer.ShortArrayWriterGwtTestCase
  * rocket.serialization.test.client.writer.ShortWriterGwtTestCase
  * rocket.serialization.test.client.writer.ThrowableWriterGwtTestCase
  * rocket.serialization.test.clientobjectinputstream.client.ClientObjectInputStreamGwtTestCase
  * rocket.serialization.test.clientobjectoutputstream.client.ClientObjectOutputStreamGwtTestCase
  * rocket.serialization.test.rebind.objectreaderorwriterfinder.ObjectReaderOrWriterFinderTestCase
  * rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase
  * rocket.serialization.test.rebind.typematcher.TypeMatcherTestCase
  * rocket.serialization.test.server.ServerObjectInputStreamTestCase
  * rocket.serialization.test.server.ServerObjectOutputStreamTestCase
  * rocket.serialization.test.server.reader.BooleanArrayReaderTestCase
  * rocket.serialization.test.server.reader.BooleanReaderTestCase
  * rocket.serialization.test.server.reader.ByteArrayReaderTestCase
  * rocket.serialization.test.server.reader.ByteReaderTestCase
  * rocket.serialization.test.server.reader.CharArrayReaderTestCase
  * rocket.serialization.test.server.reader.CharacterReaderTestCase
  * rocket.serialization.test.server.reader.DateReaderTestCase
  * rocket.serialization.test.server.reader.DoubleArrayReaderTestCase
  * rocket.serialization.test.server.reader.DoubleReaderTestCase
  * rocket.serialization.test.server.reader.FloatArrayReaderTestCase
  * rocket.serialization.test.server.reader.FloatReaderTestCase
  * rocket.serialization.test.server.reader.IntArrayReaderTestCase
  * rocket.serialization.test.server.reader.IntegerReaderTestCase
  * rocket.serialization.test.server.reader.ListReaderTestCase
  * rocket.serialization.test.server.reader.LongArrayReaderTestCase
  * rocket.serialization.test.server.reader.LongReaderTestCase
  * rocket.serialization.test.server.reader.MapReaderTestCase
  * rocket.serialization.test.server.reader.SetReaderTestCase
  * rocket.serialization.test.server.reader.ShortArrayReaderTestCase
  * rocket.serialization.test.server.reader.ShortReaderTestCase
  * rocket.serialization.test.server.writer.BooleanArrayWriterTestCase
  * rocket.serialization.test.server.writer.BooleanWriterTestCase
  * rocket.serialization.test.server.writer.ByteArrayWriterTestCase
  * rocket.serialization.test.server.writer.ByteWriterTestCase
  * rocket.serialization.test.server.writer.CharArrayWriterTestCase
  * rocket.serialization.test.server.writer.CharacterWriterTestCase
  * rocket.serialization.test.server.writer.DateWriterTestCase
  * rocket.serialization.test.server.writer.DoubleArrayWriterTestCase
  * rocket.serialization.test.server.writer.DoubleWriterTestCase
  * rocket.serialization.test.server.writer.FloatArrayWriterTestCase
  * rocket.serialization.test.server.writer.FloatWriterTestCase
  * rocket.serialization.test.server.writer.IntArrayWriterTestCase
  * rocket.serialization.test.server.writer.IntegerWriterTestCase
  * rocket.serialization.test.server.writer.ListWriterTestCase
  * rocket.serialization.test.server.writer.LongArrayWriterTestCase
  * rocket.serialization.test.server.writer.LongWriterTestCase
  * rocket.serialization.test.server.writer.MapWriterTestCase
  * rocket.serialization.test.server.writer.SetWriterTestCase
  * rocket.serialization.test.server.writer.ShortArrayWriterTestCase
  * rocket.serialization.test.server.writer.ShortWriterTestCase