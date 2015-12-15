# Purpose #
This purpose of the rocket.Beans module is to bring many of the features made popular and available to regular java via Spring to GWT authored client applications.

This wiki does not attempt to discuss or educate on why or how Spring and dependency injection or ioc works but rather how to use whats been built in rockets implementation.

# Getting started #

## Importing the rocket.Beans module ##
Add the following import statement to your own applications module.

```
<module>
	...
	<inherits name="rocket.beans.Beans" />	
```

# Whats supported #
Whilst most of the basic Spring DI features are available not all of them currently are. Some reasons for this include the limitations of how GWT java works.

## Bean scopes ##
  * Singletons
  * Prototypes

## Lifecycle control ##
  * lazy / eager singleton initialization - this attribute controls whether or not a singleton is lazy or eagerly initialized.
  * factory method - specify a static method to create the bean
  * destroy - singletons may specify a custom destroy method which is invoked when the bean factory is shutdown.
  * constructor - this element controls which constructor is used to create the bean.

## Special interfaces ##
  * BeanFactoryAware - beans that implement this interface to recieve a reference of the parent bean factory via setBeanFactory()
  * BeanNameAware - beans implement this interface to find out their bean name via setBeanName()
  * DisposableBean - singletons that implement this interface have their destroy() method called when the bean factory is shutdown.
  * InitializingBean - Beans that implement this interface have their afterPropertiesSet() method called after all properties are set.

## Factory Beans ##
FactoryBeans within a rocket bean factory perform the same role of being responsible for the construction of a bean on request by the parent bean factory. Because class objects are not available within the GWT environment an annotation is used to tell notify the bean factory generator what type of bean is being produced.
  * FactoryBeans if a bean is actually a FactoryBean then it will be asked to produce a bean to satisfy requests for the named bean.

## Interceptors ##
All the method advices and interceptors available in spring are available with some minor differences.
  * After finally advice
  * after returning advice
  * before advice
  * method interceptor

## Properties ##
Just about all the property types supported by spring are supported in rocket.
  * string
  * null
  * bean references
  * nested beans
  * list
  * set
  * map
  * image

## Extras ##
  * alias
  * including other xml files.
  * placeholder resolution
  * rpc

# Authoring a bean factory xml file #
The xml file format contains many similarities with a typical Spring file.

## bean ##
The bean tag includes a number of attributes that help define a single bean.

  * id - nested or anonymous beans dont require an id
  * class - The fully qualified class name of the bean.
  * scope - singleton | prototype
  * factoryMethod - if present must be a static method on the class that returns a new bean instance.
  * initMethod - if present this method is called after bean properties are set.

The following methods are only valid for singletons.
  * lazyInit - true|false  When "true" the singleton is initalized upon BeanFactory startup, "false" upon the first request.
  * destroyMethod - A public method on the singleton bean which is invoked when the BeanFactory is shutdown.

The example below creates a prototype bean (the example skips setting any properties).
```
	<bean 	id="magic" 
		class="example.client.Magic"
		scope="prototype"
		factory="create"
		initMethod="init" />
```

## constructor ##
Introducing the constructor tag within a bean allows the bean to be created using a constructor with one or more parameter values. Naturally the values (child value tags) must be compatible with any public constructor belonging to the bean class. More about value tags further down.

## properties / property ##
The properties tag is a container for one or more individual properties to be set upon a bean. Each property requires a property name and value.
Properties are not limited to just

The example below shows how to set a property called name with a String value of "rocket". The xml below assumes the bean class has a setter for this property etc.
```
	<bean ... >
		<properties>
			<property name="name">
				<value>rocket</value>
```

## null ##
This value may be used to set null values upon a property or constructor argument.

## value ##
This tag may be used to set a primitive or String value upon a property or constructor argument.
Primitive values are any of the following:
  * boolean
  * byte
  * short
  * int
  * long
  * float
  * double
  * char
Any attempt to set a number value of say ( 1) upon a boolean field will thus fail because the types are incompatible.

```
	<bean ... >
		<properties>
			<property name="name">
				<value>true</value>
			</property>
```

## images ##
The image tag may be used to set an image property. Behind the scenes an ImageFactory is generated which means that for supported browsers data urls will be used.

The example below sets an image property belonging to a bean. Take note that IE clients will download the image from a server whilst users of other browsers will source the image from a data url.
```
	<bean ... >
		<properties>
			<property name="someImage">
				<image file="/image.jpg" location="local"/>
			</property>
				
```

## bean-reference ##
This tag allows a bean property to set with another bean reference.

The example below assumes a property called "anotherBean" has a setter that is compatible with the type of "theOtherBean". If any of these arent true the generator complains.
```
	<bean ... >
		<properties>
			<property name="anotherBean">
				<bean-reference reference-id="theOtherBean"
			</property>
				
```

## nested bean ##
It is also possible to define a nested bean as a property of another bean.

The example below embeds the definition of theOtherBean rather than using the bean-reference of the example above.
```
	<bean ... >
		<properties>
			<property name="anotherBean">
				<bean
				<!-- rather than bean-reference theOtherBean simply includes its bean definition here -->
			</property>
				
```


## list, set, map ##
The list, set, map tags may be used to set a list, set or map property belonging to a bean. Any valid value tag (null,value,bean,bean-reference) may be an element or value of these collection types.

The example below populates each of the three collection types using the three tags.
```
	<bean ... >
		<properties>
			<property name="listProperty">				
				<list>
					<value>this list element is a String</value>
				</list>
			</property>
			<property name="setProperty">				
				<set>
					<value>this set element is a String</value>
				</set>
			</property>
			<property name="mapProperty">				
				<map>
					<map-entry key="the map key">
						<value>this map value is a String</value>
					</map>
				</map>
			</property>			
```

## rpc ##
The rpc tag may be used to create a bean that is actually of the following rpc types.
  * A gwt rpc client
  * A rocket java rpc client
  * A rocket json rpc client

The tag needs three attributes to define the properties of the rpc bean.
  * service-entry-point - the url of the server service.
  * service-interface - the service interface (For Gwt rpcs the interface that extends RemoteService)
  * id - the bean id.

## advice ##
Any bean may have one or more advices. Unfortunately advices allocations to beans is static and can only be via a definition(s) appearing in the xml file and cannot be done at runtime.

## alias ##
The alias tag may be used to introduce an alias for a bean. References to the bean may be to any of its alias or the bean's id itself. Using is equivalent in terms of the end result, ie using the alias or bean results in the same bean being delivered by the BeanFactory.

## include ##
This may be used to include another bean factory xml file. All bean definitions for the main file and included files are available in the resulting BeanFactory.
  * Included files can include other files. Cycles are detected and reported as errors.

The example below adds the definitions present in the MoreBeans.xml file to the current xml file.
```
	<include file="/example/client/MoreBeans.xml"
```

## placeholder ##
The placeholder tag loads a property file containing key/value pairs. Any placeholders found in an element or attribute in the parent xml file are substituted by values from the placeholder.
```
	<include file="<ithe name of the properties file shown below>">
	<bean 	id="bean"
		class="${beanClassName} 
		...
```

The contents of the properties file
```
beanClassName=example.client.Bean
```

The end result is the bean called "bean" will be of type "example.client.Bean".


# Working with the BeanFactory #
Because Gwt does not support runtime reflection, a GWT generator has been written which transforms a BeanFactory.xml into a class that implements
the 'rocket.beans.client.BeanFactory' interface. All beans defined in the xml file may be retrieved using the getBean(String) method passing the name of the bean. If the bean doesnt exist or cannot be created an exception is thrown.

As noted above each xml file has an accompanying sub interface of BeanFactory. Deferred binding is used to retrieve the class that is the BeanFactory.

The example below retrieves a BeanFactory generated from the xml file found in /examples/client/BeanFactory.xml. The second line of code,
then retrieves a bean called "someBean", with all dependencies and other dependency injection features satisfied. The third line makes a
request to the factory to question whether or not the bean is a singleton return true because it is.

```
	// realize bean factory from xml file. this is done at compile time.	
	rocket.beans.client.BeanFactory myBeanFactory = (rocket.beans.client.BeanFactory) GWT.create( examples.client.BeanFactory.class );

	// fetch a bean called foo
	example.client.Bean foo = (example.client.Bean) myBeanFactory.getBean( "someBean" );
	
	// is foo a singleton ? it should be...
	System.out.println( myBeanFactory.isSingleton( "someBean" ));
```

# More examples #
The beans module was developed using Test Driven Development (TDD) approach and thus contains many unit tests. Practically each and every feature that is supported includes an accompanying test which also includes an xml file that illustrates how its done in xml.

The factory beans, and interceptors are tested using simple unit tests. The actual generator which does the magic of transforming the xml file into a BeanFactory has its own GWT Test
The table below lists some examples.

| Component | Class | UnitTest | Comments |
|:----------|:------|:---------|:---------|
| PrototypeFactoryBean | rocket.beans.client.PrototypeFactoryBean | rocket.beans.test.PrototypeFactoryBeanTestCase | PFBs are generated by the generator to produce prototype beans |
| SingletonFactoryBean | rocket.beans.client.SingletonFactoryBean | rocket.beans.test.SingletonFactoryBeanTestCase | SFBs are generated by the generator to produce singleton beans |
| InterceptorChain | rocket.beans.client.aop.InterceptorChain | rocket.beans.test.InterceptorChainTestCase | The IC is responsible for invoking interceptors(advices) and then the adviced bean |
| BeanFactoryGenerator | rocket.beans.rebind.BeanFactoryGenerator | rocket.beans.test.beans.client.BeansGwtTestCase | The generator which transforms the xml file into a real BeanFactory at runtime |

The BeansGwtTestCase includes many tests each with accompanying xml file that illustrates the feature being tested. Each test includes a sub package of the almost the same name with all related files.