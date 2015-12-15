#Summary of the design and objectives of the new Event system.

# Motivation #
The way events are modelled in GWT could have been better.
  * Event objects are bare and dont have defined methods.
  * The DOM class contains a number of event methods related, all method names are typically prefixed with "event".
  * It is possible to call any of the `com.google.gwt.user.client.ui.DOM.eventXXX` methods regardless of whether it makes sense to do so, eg calling DOM.eventGetKey inside a mouse move event.
  * Too many static methods, int constants and bitmasks.
  * Many listener methods methods lack any reference to the javascript Event object, forcing developers to access the Event singleton.

## com.google.gwt.user.client.ui.KeyboardListener ##

The KeyboardListener defines three methods that correspond to the key down, key press and key up browser events.
```
	onKeyDown(Widget sender, char key, int modifier)
	onKeyPress(Widget, char, int)
	onKeyUp(Widget, char, int)
```

The above method signatures are not extensible and thus a workaround was needed to make it possible to do other event related things like preventing the default action or cancel bubbling.

These sorts of use cases like this obviously motivated the new introduction of a getter to retrieve the Event being processed - `com.google.gwt.user.client.DOM.eventGetCurrentEvent()`.

# Solution #
The first step in solving the above problems involves modelling a type system that matches the event heirarchy of browser events. The nested list shows the class heirarchy of the Event types that live in `rocket.event.client`.

  * Event
    * BlurEvent
    * ChangeEvent
    * FocusEvent
    * ImageLoadFailedEvent
    * ImageLoadSuccessEvent
    * KeyEvent
      * KeyDownEvent
      * KeyPressEvent
      * KeyUpEvent
    * MouseEvent
      * MouseButtonEvent
        * MouseClickEvent
        * MouseDoubleClickEvent
        * MouseDownEvent
        * MouseUpEvent
      * MouseMoveEvent
      * MouseOutEvent
      * MouseOverEvent
      * MouseWheelEvent
    * ScrollEvent

The above classes are simply wrappers around the real Event object each contains methods that delegate to the GWT DOM class passing the wrapped event. This approach solves a number of problems listed in the motivation section.

## rocket.event.client.Event ##
The `rocket.event.client.Event` class is the common base class for all javascript events. It thus holds a number of instance methods that are common to all events.
  * cancelBubble(boolean) - stops event bubbling.
  * getEvent() - retrieves the wrapped event.
  * Element getTarget() - reports the element that was the target of the event.
  * Widget getWidget();
  * stop() - prevents the default action from occuring.
  * asXXX - a number of methods that may be used to cast to its real type. eg asFocus() returns FocusEvent if it was a focus event or null if its something else.

There are a number of other methods but they can be ignored for now.

## rocket.event.client.KeyEvent ##

The KeyEvent class is a common base for the three key event types.

## Subclasses ##
  * KeyDownEvent
  * KeyPressEvent
  * KeyUpEvent

The three subclasses add no new methods, they only serve to identify the different event type.

### Methods ###
The KeyEvent contains all the common methods to all key events.
  * getKey()
  * setKey()
  * isAlt() - Was the alt key pressed ? Notice no need to extract a bit out of bit mask.
  * isControl()
  * isRepeatedKey()
  * isShift()
  * int getFunctionKey() - returns 0 for F1, 1 for F2 etc.
  * isAlpha() - Was the pressed key a-z ???
  * isBackspace() - True if the key pressed was backspace.
  * isCursorDown()
  * isCursorLeft()
  * isCursorRight()
  * isCursorUp()
  * isDelete()
  * isDigit() - True if the key pressed was any of the digits 0 thru 9.
  * isEditing() - True if the key pressed was END, ENTER, HOME, INSERT etc.
  * isEnd()
  * isEnter()
  * isEscape()
  * isHome()
  * isInsert()
  * isModifier() - True if the key pressed is ALT, CONTROL, META, or SHIFT
  * isNavigation() - True if the key pressed was any of the CURSORS, HOME etc.
  * isPageDown()
  * isPageUp()
  * isTab()

Many of the isXXX methods remove the need to ever use bit masks or constants holding key values.

## Example ##
Writing a `rocket.event.client.KeyEventListener` is much clear, everything related to the event and the widget responsible are all available upon the KeyEvent. The code below shows how to prevent the entering of any non digit keys into a TextBox.

```
	TextBox numberOnlyTextBox = new TextBox();
	numberOnlyTextBox.addKeyEventListener(new KeyEventListener(){
		public void onKeyDown(final KeyDownEvent event){
			if( event.isNavigation() || event.isEditing() || event.isModifier() ){
				return;
			}
			if( event.isDigit() ){
				return;
			}
			
			// since its any other key prevent it - stop it!!! could be a function key, any of the shifted number keys (exclaimation, at sign etc.)
			event.stop();
		}
		
		public void onKeyPress(final KeyPressEvent event){
			// do nothing
		}
		
		public void onKeyUp(final KeyUpEvent event){
			// do nothing
		}
	});
```

## Other XXXEventListener classes ##
A number of event listeners exist for each of the different category of event. The rocket basic widget replacements all use these new listeners rather than the traditional GWT listeners.
Event listeners that use the new event sub types exist and are used by all basic widgets.
  * `rocket.client.event.ChangeEventListener`
  * `rocket.client.event.FocusEventListener`
  * `rocket.client.event.ImageLoadEventListener`
  * `rocket.client.event.KeyEventListener`
  * `rocket.client.event.MouseEventListener`
  * `rocket.client.event.ScrollListener`
Each of the listener classes above receives the appropriate Event sub type which contains all the behaviour and properties relevant for that category of event.

## GWT EventListener and EventPreview and the Rocket events ##
A number of adapter classes exist that bridge the legacy event listener methods and the rocket events.

| GWT class | Rocket adapter class |
|:----------|:---------------------|
| `com.google.gwt.user.client.EventListener` | `rocket.event.client.EventListenerAdapter` |
| `com.google.gwt.user.client.EventPreview` | `rocket.event.client.EventPreviewAdapter` |

Each accepts a GWT Event, creates the wrapper and then dispatches to the appropriate do nothing method. Developers thus need to judicially override the appropriate method.

# Getting started #
The first step involves inheriting the `rocket.event.Event` module by adding an inherit element.
```
    <inherits name="rocket.event.Events" />
```

### Basic Widgets ###
Use any of the rocket basic widgets (refer to the wiki for more details).

## Authoring widgets/panels ##
To build your own widgets or panels that use the same low level events simply extend any of the purpose built classes:
  * `rocket.widget.client.Widget`
  * `rocket.widget.client.Composite` - Template that helps authoring a Composite
  * `rocket.widget.client.Panel` - Template class that makes it easy to author a panel that has many child widgets.
  * `rocket.widget.client.SimplePanel` - Template class that makes it easy to author a panel that has a single child widget.

Each of the above templated classes contains method that should be overridden to change or customize behaviour.

### Example ###
Therefore creating a widget that wraps a TEXTBOX element and allows users to register Key events would like something like this.
```
import rocket.event.client.*;
import rocket.widget.client.Widget;

public class SimpleTextBox extends Widget{

	// allow key event listeners to be registered...
	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);
		dispatcher.setKeyEventListeners(dispatcher.createKeyEventListeners());
	}	

	// EventBitMaskConstants includes constants with the same values as those found on com.google.gwt.user.client.Event. it just has a different name to avoid clashes between both Event types.
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.KEY_EVENTS | EventBitMaskConstants.CHANGE;
	}

	// add a public addXXX and removeXXX where XXX is KeyEventListener methods.
	public void addKeyEventListener(final KeyEventListener keyEventListener) {
		this.getEventListenerDispatcher().addKeyEventListener(keyEventListener);
	}

	public void removeKeyEventListener(final KeyEventListener keyEventListener) {
		this.getEventListenerDispatcher().removeKeyEventListener(keyEventListener);
	}

	// add other getters and setters to set properties like maxLength upon the wrapped element, etc.
}
```

The original Event is still fired via the EventListener.onBrowserEvent method.
  * Event is fired as a result of some browser event.
  * GWT locates the appropriate EventListener and passes the native event to EventListener.onBrowser(com.google.gwt.user.client.Event );
  * The rocket Widget or Panel builds a rocket.event.client.Event wrapper and calls onBrowser( rocket.event.client.Event );
  * Within the Rocket Widget/Panel onBrowser( rocket.event.client.Event ) all listeners are notified.


# Further examples #
For further samples refer to any of the following demo.
  * `rocket.widget.test.basicwidgets.client.BasicWidgetsTest`