# Motivation #
Currently GWT does not provide a mechanism to easily glue pre existing elements to widget instances. It also does not include functionality to embed html, text files or execute templates on the client. In order to achieve the later one can:
  * Manually create a string containing the entire contents of the html/text file. Achieveable, but cumbersome and not very practical if the page is constantly being changed.
  * Make a Http request to the server to fetch the required file. When the response completes the Html widget can be created and added to the dom.

Embedding Html files has several advantages for any team that is a mixture of traditional and GWT development.
  * Html pages can be designed separately by a web designer.
  * Often it is useful to include some java logic within html pages, as is frequently done in jsps.

# Goals #
  * Make it easy to embedd html or templates at compile time, using automation and code generation.

# Non Goals #
  * Validation of ids is not performed at compile time. Any such errors arent discovered until the application is run.

# HtmlTemplateFactory #
The `rocket.widget.client.HtmlTemplateFactory` factory makes it easy to bind existing elements identified by an id to a widget by defining an interface with a number of getters along with annotations to
specify the id. The templating problem discribed above is also supported via annotations.

# Getting Started #
Import the `rocket.widget.Widget` module.

## Binding an element with id to a widget ##
### An example ###

The html page below represents a simple logon page. An example below describes how to prepare a interface that will bind the three elements below to a TextBox, Password and Button widgets.
```
  <html>
  	<body>
  		...
  		<form>
  			Login:    <input type="text"      id="username"/>
  			Password: <input type="password"  id="password"/>
  	        	<button id="submit">Login</button>
  		</form>
  		...
  	</body>
  </html>
```

### Defining the interface ###
The interface below has been prepared that descibes three methods to get a widget that will be bound to each of the three elements on the page above.
```
package ...

import rocket.widget.client.TextBox;
import rocket.widget.client.Button;
import rocket.widget.client.HtmlTemplateFactory;

public interface HijackedElementFactory extends HtmlTemplateFactory {

	/*
	 * @id username
	 */
	TextBox getUsername();

	/*
	 * @id password
	 */
	TextBox getPassword();


	/*
	 * @id submit
	 */
	Button getSubmit();
}
```

The actual id of the element is noted by including an @id annotation. The getUsername() method has a return type of TextBox and an id of "username". When the interface is realised any calls to it will return the same TextBox which will be bound to the username textbox.

  * The return type of the method denotes the widget that will be created from the located element. It must be compatible with the actual element. Eg dont try and hook up a TextBox with a Button a runtime exception will occur.
  * All subsequent calls to this method will return the same widget instance. The widget instance is cached (a singleton)
  * Initially any widget is attached to the main RootPanel.
  * Widgets may be removed from their initial parent RootPanel and added to other Panels.
  * All methods including listeners work as expected.
  * All methods must have no parameters.
  * Methods cannot return void - a factory must return something.

### Using the factory ###
The deferred binding mechanism is used to realise a class that implements the interface contract described above. The first step involves fetching an instance of the factory using deferred binding passing the class literal of the prepared HijackedElementFactory interface. The user may then call any of the getter methods to fetch the corresponding widget.

```
package ...

import com.google.gwt.core.client.GWT;

import rocket.widget.client.TextBox;
import rocket.widget.client.Button;
import rocket.widget.client.HtmlTemplateFactory;

...
	// inside some method...
	HijackedElementFactory factory = (HijackedElementFactory) GWT.create( HijackedElementFactory.class );

	TextBox username = factory.getUsername();
	TextBox password = factory.getPassword();
	TextBox submit = factory.getSubmit();

```

## Alternative way of binding elements without using a HtmlTemplateFactory ##
The rocket widgets provide an extra constructor that allows a user to pass in any element. The widget will then adopt that element providing its compatible (dont pass Button elements to TextBox widgets).
The sample below shows how this can be done.

```
package ...

import com.google.gwt.user.client.DOM
import rocket.widget.client.TextBox;
import rocket.widget.client.Button;

...

	TextBox username = new TextBox( DOM.getElementById( "username" ));
	System.out.println( username.isPassword ); // prints false

	TextBox password = new TextBox( DOM.getElementById( "password" ));
	System.out.println( password.isPassword ); // prints true

	Button submit = new Button( DOM.getElementById( "submit" ));

	System.out.println( submit.getParent() ); // will print RootPanel.
```


### Supported widgets ###
The table below includes a mapping between html elements to rocket widgets.

| Html element | Widget |
|:-------------|:-------|
| A            | `rocket.widgets.client.Hyperlink` |
| BUTTON       | `rocket.widgets.client.Button` |
| IMG          | `rocket.widgets.client.Image` |
| FORM         | `rocket.widgets.client.FormPanel` |
| INPUT type=checkbox | `rocket.widgets.client.CheckBox` |
| INPUT type=password | `rocket.widgets.client.TextBox` |
| INPUT type=radiobutton | `rocket.widgets.client.RadioButton` |
| INPUT type=text | `rocket.widgets.client.TextBox` |
| SELECT       | `rocket.widgets.client.ListBox` |
| TEXTAREA     | `rocket.widgets.client.TextArea` |

Dont worry even though the password is a TextBox reference the element remains as a password element masking any entered text. From a design perspective both elements have similar methods and fire the same events so it makes sense to have a single class to represent them. The TextBox.isPassword() method may be used to determine if the element is a regular text box or a password.

For more info refer to the [BasicWidgets ](.md)

## Embedding a Html / Template into an Application ##

The HtmlTemplateFactory also supports a new annotation when the return type of a defined method is Html. The @file annotation may be used to specify a file which will be included within the generated javascript. Any calls to that method will result in a new Html element containing the contents of the file without making any http requests back to the server. The file which has many similarities to a jsp may also contain blocks of java.

### An example ###

This simple example will show how to create embedd a simple template that contains some java to create the rows of a table. It will not show the definition of the value objects, lets just assume they exist.

### Defining the interface ###
The interface below has been prepared that descibes three methods to get a widget that will be bound to each of the three elements on the page above.
```
package ...

import rocket.widget.client.TextBox;
import rocket.widget.client.Button;
import rocket.widget.client.HtmlTemplateFactory;

public interface SampleTemplate extends HtmlTemplateFactory {

	/*
	 * @file /template.txt
	 */
	Html createTableWithRows( RowData[] rows );
}
```

### The template file ###
The template file is a simple text or html file with blocks of java (<% ...java... %>) or values (specified <%= value/variable %>). The syntax is very similar to that of jsps without the custom tags.
Any parameters to the method are available within the template using the same name.
```
<table>
	<tr>
		<td>Column 1</td><td>Column 2</td><td>Column 3</td>
	</tr>
	
<%
	// loop over each row creating a TR for each one.
	for( int i = 0; i < rows.length; i++ ){
		RowData row = rows[ i ];	
%>	
	<tr>
		<td><%= row.getColumn1() %></td><td><%= row.getColumn2() %></td><td><%= row.getColumn3() %></td>
	</tr>
<%
	} // end of for loop.
%>
</table>
```

  * The template can be pure html if one wishes or it may contain as much or as a little java code as your heart desires.
  * The method may have 0 or more parameters.


### Using the factory ###
The deferred binding mechanism is used to realise a class that implements the interface contract described above. The first step involves fetching an instance of the factory using deferred binding passing the class literal of the prepared HijackedElementFactory interface. The user may then call any of the getter methods to fetch the corresponding widget.

```
package ...

import com.google.gwt.core.client.GWT;

import rocket.widget.client.TextBox;
import rocket.widget.client.Button;
import rocket.widget.client.HtmlTemplateFactory;

...
	// inside some method...
	RowData[] rowData = createRowData(); // this imaginary method creates an array of RowData instances.
	
	// create the factory
	SampleTemplate factory = (SampleTemplate) GWT.create( SampleTemplate.class );
	
	// execute the template.
	Html tableWithRows = factory.createTableWithRows( rowData );
```

# Further samples #

For further examples refer to the unit test and related demos.
  * `rocket.widget.test.htmltemplatefactory.client.HtmlTemplateFactoryGwtTestCase`
  * `rocket.widget.test.basicwidgets.client.BasicWidgetsTest`