# Background #
The generator idiom is used by GWT to implement many of its sophisticated features including:
  * The RPC proxy and accompanying (de)serializer for reachable types.
  * Message bundle support.
  * ImageBundles which generate a single image from a collection of many.
For more info on these fantastic features refer to the doco, which is found on both the GWT home page and within the download.

## Emitting new types ##
This section does not concern how a genearator decides what code to generate but rather how it interacts with the GWT environment. Two major components are used throughout a code generation session, the GWT `com.google.gwt.core.ext.typeinfo.TypeOracle` and its accompanying helpers and `com.google.gwt.user.rebind.SourceWrite`. As their names imply one answers questions about available types, whilst the other is used to create the source file for new types.

## Comparing GWT Type Oracle with java reflection ##
The type oracle provides an interface to discover what types are available and methods to inspect various properties including types, constructors, methods and fields. The type oracle provided a purely read only view of the types available.

The GWT type oracle has many parallels with the reflection facilities available in a typical java environment.

The table shows a simplified mapping between reflection abstractions and their corresponding GWT equivalent.

| Reflection | GWT Type Oracle |
|:-----------|:----------------|
| `Class`    | `com.google.gwt.core.ext.typeinfo.JClassType`, `com.google.gwt.core.ext.typeinfo.JType`, `com.google.gwt.core.ext.typeinfo.JPrimitiveType` |
| `java.lang.reflect.Method` | `com.google.gwt.core.ext.typeinfo.JMethod` |
| `java.lang.reflect.Constructor` | `com.google.gwt.core.ext.typeinfo.JConstructor` |
| `java.lang.reflect.Field` | `com.google.gwt.core.ext.typeinfo.JField` |

The GWT Type Oracle and its accompanying classes provide many methods to retrieve the name of a type, test is visibility, iterate over a list of all fields and more. With some experience in reflection it is quite easy to find the equivalent method on the matching GWT Type oracle abstraction. They have almost the same method signatures.



# Motivation #
Currently GWT generators use the SourceWriter class to emit new types by printing java. The sample below is taken from `ProxyCreator` the class responsible for generating GWT RPC clients.

To create new types one must use `com.google.gwt.user.rebind.ClassSourceFileComposerFactory` to eventually create a new SourceWriter. The factory is a bean that includes methods to set the super type, which interfaces are implemented and so on. After the skeleton of the class is created the SourceWriter may be used to print java statements. Anything text can be printed and together these will be used to build the entire source file.

Whilst a powerful construct code generation within GWT is quite low level with a number of issues outstanding.

  * It is hard to visualise what is being created by print statements. This must be discovered by running the generator with lots of trial and error.
  * Syntax errors and malformed code are only discovered when the compiler complains after the source file is completed and committed.
  * Using print statements is a extremely verbose way to output emit a method especially when most of the method body and declaration is boilerplate.
  * No facility is currently present to create templates replacing place holders with dynamic values.
  * The current system does not provide a way to copy class components (constructors, methods, or fields) from one class to another. Often when sub-classing it would be useful to clone the overridden method and then just replace its body.
  * It is not possible to test components which require a TypeOracle outside of a generator.

The sample below is taken from [`http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/com/google/gwt/user/rebind/rpc/ProxyCreator.java` ProxyCreator](.md) and demonstrates some of the points listed above.

```
w.println("final "
				+ (ClientSerializationStreamReader.class.getName() + " streamReader = new "
						+ ClientSerializationStreamReader.class.getName() + "(SERIALIZER);"));
		w.println("final "
				+ (ClientSerializationStreamWriter.class.getName() + " streamWriter = new "
						+ ClientSerializationStreamWriter.class.getName() + "(SERIALIZER, GWT.getModuleBaseURL(), SERIALIZATION_POLICY);"));
		w.println("try {");
		w.indent();
		{
			w.print("__" + method.getName() + "(streamWriter");
			for (i = 0; i < params.length; i++) {
				w.print(", " + params[i].getName());
			}
			w.println(");");
		}
		w.outdent();
```



# The solution #
The rocket generator package attempts to solve some of the very problems described above.
The rocket.generator package does not change how deferred binding works. It is however a richer abstraction that makes it easier to author new generators. A GWT generator which only works inside the hosted mode environment/compiler and may only be tested inside a GwtTestCase. A rocket enhanced generator may also use the jdk as the source of its types making it possible to test within a normal Junit TestCase.

### Creating class components via factories ###
Each class component contains factories to create its enclosed components.

List below are the factory methods of [NewType http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/generator/rebind/type/NewType.java ](.md).
```
interface NewType{
	NewField newField();
	NewMethod newMethod();
	NewNestedType newNestedType();
	NewAnonymousNestedType newAnonymousNestedType();	
	NewNestedInterfaceType newNestedInterfaceType();

	... non factory methods omitted
}
```

List below are some of the interesting factory methods of [NewMethod http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/generator/rebind/method/NewMethod.java ](.md) that make it easy to override methods.
I personally find it easiest to write a templated class and then override the bare minimum - the stuff that can only be satisfied by the generator.
```
interface NewMethod
	void setEnclosingType(Type enclosingType);

	void setAbstract(boolean abstractt);

	void setFinal(boolean finall);

	void setStatic(boolean staticc);

	void setNative(boolean nativee);

	void setVisibility(Visibility visibility);

	void setName(String name);

	NewMethodParameter newParameter();

	void addParameter(NewMethodParameter parameter);

	void addThrownTypes(Type thrownTypes);

	void setReturnType(Type returnType);

	CodeBlock getBody();

	void setBody(CodeBlock body);
}
```

### Templates ###
One of the most useful features of the package are templates. Templates are text files containing java code with placeholders for dynamic
  * Templates are readable, after all they are text files with placeholders.
  * Templates may contain other templates.
  * The CollectionTemplatedTile makes it very easy to set a template multiple times each once for each parameter or a list of something else.
  * Theres no need to print a single line of code again.
  * Makes it very easy to write jsni. Inserted types, methods, fields into templates are all written using jsni notation. To make this happen simply set the enclosing method "native" property.
  * All type references are now automatically fully qualified. Removes the need to add import statements.
  * Authoring jsni with references to java types, methods or fields is simple as their references are emitted using jsni notation automatically.


# Examples - How the BeanFactoryGenerator is put together #

The [http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/beans/rebind/BeanFactoryGenerator.java BeanFactoryGenerator ](.md) generates a [http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/beans/client/BeanFactory.java BeanFactory ](.md) that realises an xml file filled with beans.

A different FactoryBean sub class is generated for each defined bean. Depending on its scope the appropriate FactoryBean is selected. The generator sub classes[http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/beans/client/SingletonFactoryBean.java SingletonFactoryBean ](.md) concentrating on filling the following three methods.
  * Object createInstance() throws Exception;
  * void satisfyProperties(final Object instance) throws Exception;
  * protected void satisfyInit(final Object instance) throws Exception;

## A simple template example (creating a bean instance from a factory method) ##
The `createInstance` method is implemented using either of three possible templates.
  * deferred binding
  * new constructor with parameters
  * factory method

The sample below concentrates on a bean that is created from a factory.

The method that overrides createInstance for beans created from a static factory method.
```
	protected void overrideFactoryBeanCreateInstanceFactoryMethod(final Bean bean, final String factoryMethodName) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		StringHelper.checkNotEmpty("parameter:factoryMethodName", factoryMethodName);

		final Type beanType = bean.getType();
		final Method factoryMethod = beanType.findMethod(factoryMethodName, Collections.EMPTY_LIST);
		if (null == factoryMethod || false == factoryMethod.isStatic() || factoryMethod.getVisibility() != Visibility.PUBLIC) {
			this.throwFactoryMethodNotFound(bean, factoryMethodName);
		}

		this.getGeneratorContext().debug("Inserting statement in FactoryBean.createInstance to call " + factoryMethod + " for bean " + bean);

		final NewMethod newFactoryMethod = this.createCreateInstanceMethod(bean.getFactoryBean());

		final FactoryMethodTemplatedFile body = new FactoryMethodTemplatedFile();
		body.setFactoryType(beanType);
		body.setFactoryMethod(factoryMethod);

		newFactoryMethod.setBody(body);
	}

	protected NewMethod createCreateInstanceMethod(final NewType factoryBean) {
		ObjectHelper.checkNotNull("parameter:factoryBean", factoryBean);

		final Method method = factoryBean.getMostDerivedMethod("createInstance", Collections.EMPTY_LIST);

		// finds the abstract method and clones it.		
		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		newMethod.setNative( false );
		
		return newMethod;
	}
```

The template
```
	// factory-method.txt

	// invokes a static method on a type.
	return ${factoryType}.${factoryMethod}();
```

To override and implement the createInstance methods requires only a few steps.
  * Locate the method to override. factoryBean.getMostDerivedMethod
  * Copy it - method.copy
  * Set the only dynamic part of the template the factory method - new FactoryMethodTemplatedFile().

# Emebedding one template within another #
The next example below concentrates on the ability to repeatedly insert one template within another. Two templates work in tandem one generate satisfyProperties method.

The [set properties](http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/beans/rebind/properties/set-properties.txt) which provides the body for the satisfyProperties method.
The [SetPropertiesTemplatedFile](http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/beans/rebind/properties/SetPropertiesTemplatedFile.java) uses [CollectionTemplateCodeBlock ](http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/generator/rebind/codeblock/CollectionTemplatedCodeBlock.java) to loop over a collection of bean properties.
```
	// set-properties.txt
	
	// cast the instance parameter to a typed local variable called "instance0"
	final ${beanType} instance0 = (${beanType}) ${instance};

	// set the individual properties
	${setIndividualProperties}
```
  * **beanType** is a placeholder for actual bean instance type.
  * **instance** the parameter name of the bean instance parameter.
  * **setIndividualProperties** a placeholder for where the individual set property statements will appear.

The [set property](http://rocket-gwt.googlecode.com/svn/trunk/Rocket/src/rocket/beans/rebind/properties/set-property.txt) which is inserted for each property being set.
```
	// set-property.txt

	// invoke the setter passing the new property value...
	instance0.${setter}( ${value} );
```
  * **setter** The setter method for the property being set.
  * **value** The new property value be it a literal or another bean reference.

The method is the actual method within the generator that finds the setters each of the properties passed. Most of the code is concerned with locating the values for each of the placeholders of the template.
All static stuff that doesnt change is within the template with only dynamic values populated by the method below.
```
	protected void overrideFactoryBeanSatisfyProperties(final Bean bean, final List properties) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		ObjectHelper.checkNotNull("parameter:properties", properties);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("Attempting to create a method which will set " + properties.size() + " properties upon " + bean);

		final Type voidType = this.getGeneratorContext().getVoid();
		final Type beanType = bean.getType();

		final SetPropertiesTemplatedFile body = new SetPropertiesTemplatedFile();
		body.setBean(beanType);

		final NewType factoryBean = bean.getFactoryBean();
		final Method method = factoryBean.getMostDerivedMethod(Constants.SATISFY_PROPERTIES, this.getParameterListWithOnlyObject());
		
		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		newMethod.setNative( false );
		
		body.setInstance((MethodParameter) newMethod.getParameters().get(0));
		newMethod.setBody(body);

		// loop thru all properties
		final Iterator propertyIterator = properties.iterator();
		while (propertyIterator.hasNext()) {
			final PropertyTag propertyTag = (PropertyTag) propertyIterator.next();
			final String propertyName = propertyTag.getPropertyName();
			final String setterName = GeneratorHelper.buildSetterName(propertyName);

			context.debug("Searching for setter method [" + setterName + "] for property upon bean " + bean);

			final Value value = this.asValue(propertyTag.getValue());
			final List matching = new ArrayList();

			final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
				protected boolean visit(final Method method) {

					while (true) {
						// names dont match
						if (false == method.getName().equals(setterName)) {
							break;
						}
						// return type must be void
						if (false == method.getReturnType().equals(voidType)) {
							break;
						}
						// parameter types must be compatible...
						final List parameters = method.getParameters();
						if (parameters.size() != 1) {
							break;
						}

						final MethodParameter parameter = (MethodParameter) parameters.get(0);
						final Type propertyType = parameter.getType();
						if (false == value.isCompatibleWith(propertyType)) {
							break;
						}
						value.setType(propertyType);
						matching.add(method);
						break;
					}

					return false;
				}

				protected boolean skipJavaLangObjectMethods() {
					return true;
				}
			};

			visitor.start(beanType);
			if (matching.isEmpty()) {
				this.throwUnableToFindSetter(bean, propertyName);
			}
			if (matching.size() != 1) {
				this.throwTooManySettersFound(bean, propertyName);
			}

			final Method setter = (Method) matching.get(0);
			body.addProperty(setter, value);

			context.debug("Inserted statement to set property [" + propertyName + "] upon bean " + bean);

		}
	}
```


The process required by the factory bean to set all defined properties are
  * Cast the instance parameter from Object to the actual bean type.
  * invoke none or more set property statements with the appropriate value or reference.



# Testing generator components outside of a generator #

The generator package includes two GeneratorContexts (the rocket equivalent of a TypeOracle).
  * `rocket.generator.rebind.GeneratorContext` which uses the GWT TypeOracle.
  * `rocket.generator.rebind.java.JavaGeneratorContext` uses the jdk as the type source

# Further examples #
For further examples refer to the unit tests.
The tests below execute within the GWT compiler and use the GWT TypeOracle
  * `rocket.generator.test.generator.client.GeneratorGwtTestCase`
  * `rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockGwtTestCase`

The list below includes a number of junit tests that use the generator module but use the jdk as the type source.
  * `rocket.generator.test.AllMethodsVisitorTestCase`
  * `rocket.generator.test.CollectionTemplateTestCase`
  * `rocket.generator.test.GeneratorHelperTestCase`
  * `rocket.generator.test.MethodTestCase`
  * `rocket.generator.test.StringBufferSourceWriterTestCase`
  * `rocket.generator.test.TypeTestCase`
  * `rocket.generator.test.VirtualMethodTestCase`
  * 