#Work with computed,inline and rule styles in a cross browser manner.
# Motivation #

GWT provides a number of methods within the DOM class to help with the inline style of an element.
  * DOM.setStyleAttribute(Element element,String attribute,String value);
  * DOM.getStyleAttribute(Element element,String attribute);
  * DOM.setIntStyleAttribute(Element element,String attribute,int value);
  * DOM.getIntStyleAttribute(Element element,String attribute);

These four methods only address some of the functionality that may be needed by a developer, and fail to address the following issues.
  * Querying the computed style of element.
  * Ability to get and set the style belonging to a stylesheet rule.
  * Inability to use standard attribute name and values in a cross browser compatible way.
  * Richer set of css units with auto conversion - get a ex value in pixels.

## Why worry about cross browser attribute name/values ##
The ability to use standard attribute names and value cannot be undestated. Of particular interest is the need for a special case for the opacity attribute. The example below only attempts to update the opacity of an element.

```
	Element element = ... // get Element from somewhere
	
	String value = "opacity=0.5";
	// special test for ie
	if( internet explorer ){
		value = "alpha(opacity=50)";
	}
	DOM.setStyleAttribute( element, value );
```

# The solution #
The rocket.style.Style module attempts to address many of the points pointed out above.

  * `rocket.style.client.InlineStyle` - read/write styles to an element's inline style.
  * `rocket.style.client.ComputedStyle` - reads computed styles for the given element.
  * `rocket.style.client.RuleStyle` - read/write styles belonging to a Rule which in turn belongs to a style sheet.
  * `rocket.style.client.Css` - Includes constants for all styles.

The methods below list the common methods available in all Style classes.
  * getDouble(String attribute, CssUnit unit, double defaultValue);
  * setDouble(String attribute, double value, CssUnit unit);
  * getInteger(String attribute, CssUnit unit, int defaultValue);
  * setInteger(String attribute, int value, CssUnit unit);
  * getString(String attribute);
  * setString(String attribute, String value);
  * getUrl(String attribute)
  * setUrl(String attribute,String url)
  * remove(String) - removes a style from an element.

Additional methods are present that provide a Map view of all the style attributes for an element. For more details refer to the javadoc.

## Cross browser Attribute names/values ##
Two examples below are given that show something that would require quite a bit of code and jsni without the rocket.style.Style module.

### Inline Opacity ###
The motivation section discussed the need for a special case to set a new opacity upon an element. The example shows how this would be achieved using the InlineStyle.
```
	Element element = ... // get Element from somewhere
	InlineStyle inlineStyle = InlineStyle.getInlineStyle( element );
	inlineStyle.setString( Css.OPACITY, 0.5, CssUnit.PERCENTAGE );
```

### Computed colour values ###
Working with colour values is another problematic concern for any developer. Internet Explorer again is the primary cause of headaches for developers as it choses to have different values from all the other values. Rather than converting source returning values in a standard value it returns the original value. A developer would thus have to support the following forms of a string value when reading a colour value. The list below shows 4 different values to represent the red colour.
  * rgb( 255,0,0) - standard form **always** returned by Firefox, Opera and Safari.
  * "red" - a named colour
  * #ff0000 - a hex triplet
  * #f00 - a three digit hex triplet

Reading a colour value thus becomes quite troublesome, however doing so with ComputedStyle is quite simple as the example below shows.
```
	Element element = ...// get Element from somewhere
	ComputedStyle computedStyle = ComputedStyle.getComputedStyle( element );
	Colour shouldBeRed = computedStyle.getColour( Css.BACKGROUND_COLOR );
```

There are many other attributes that are special cases. So far quite a few are taken care of by the rocket classes, however if you find one thats handled wrong or not yet supported feel free to post a message or send an email.

# Getting started #
After downloading and unzipping the download archive the last step required is to inherit the `rocket.style.Style` module. This is achieved by adding an inherits element to your application module.

```
<module>
 	<!-- inherit GWT -->
	<inherits name="com.google.gwt.user.User" />		

 	<!-- inherit the style module -->
	<inherits name="rocket.style.Style" />		

 	<!-- inherit any other modules you might need, for more details about editing a module.gwt.xml refer to the GWT doco. -->

</module>
```




# Unit/Functional tests #

For further examples refer to the unit and functional tests that accompany the distribution.

  * `rocket.style.test.style.client.StyleTest`
  * `rocket.style.test.stylesheet.client.StyleSheetTest`