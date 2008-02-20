/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.dom.client;

import java.util.ArrayList;
import java.util.List;

import rocket.browser.client.Browser;
import rocket.collection.client.CollectionsHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A collection of useful methods relating to manipulating the DOM
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Dom {

	/**
	 * Convenience method that removes an element from its parent.
	 * 
	 * @param element
	 */
	public static void removeFromParent(final Element element) {
		DOM.removeChild(DOM.getParent(element), element);
	}
	
	/**
	 * Retrieves the container element which contains this child element. This
	 * is particularly useful when calculating coordinates for positioned
	 * element.
	 * 
	 * @param element
	 * @return
	 */
	public static Element getContainer(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		return Dom.getContainer0(element);
	}

	native static private Element getContainer0(final Element element)/*-{
	 var container = null;
	 var element0 = element;
	 while( element0 ){
	 // stop if this element is absolutely/relative positioned. 
	 var position = element0.style.position.toLowerCase();
	 if( "absolute" == position || "relative" == position ){
	 container = element0;
	 break;
	 }             
	 element0 = element0.offsetParent;
	 }
	 return container;    
	 }-*/;

	/**
	 * Retrieves the relative x/left coordinates of the given element relative
	 * to its parent container element. This is particularly useful if one
	 * wishes to absolutely position a widget having added it to the dom.
	 * 
	 * @param element
	 * @return The pixel value
	 */
	public static int getContainerLeftOffset(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		return getContainerLeftOffset0(element);
	}

	native static private int getContainerLeftOffset0(final Element element) /*-{
	 var left = 0;
	 var element0 = element;
	 
	 while( element0 ){ 
	 var position = element.style.position;
	 if( "absolute" == position || "relative" == position ){
	 break;
	 }		
	 
	 left = left + element0.offsetLeft;
	 element0 = element0.offsetParent;  
	 }
	 
	 return left;	 
	 }-*/;

	/**
	 * Retrieves the relative y/top coordinates of the given element relative to
	 * its parent container element. This is particularly useful if one wishes
	 * to absolutely position a widget having added it to the dom.
	 * 
	 * @param element
	 * @return The pixel value
	 */
	public static int getContainerTopOffset(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		return getContainerTopOffset0(element);
	}

	native static private int getContainerTopOffset0(final Element element) /*-{
	 var top = 0;
	 var element0 = element;
	 
	 while( element0 ){ 
	 var position = element.style.position;
	 if( "absolute" == position || "relative" == position ){
	 break;
	 }		
	 
	 top = top + element0.offsetTop;
	 element0 = element0.offsetParent;  
	 }
	 
	 return top;	 
	 }-*/;

	/**
	 * Helper which tests if the given element is of the specified tag.
	 * 
	 * @param element
	 * @param tagName
	 * @return
	 */
	public static boolean isTag(final Element element, final String tagName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		StringHelper.checkNotEmpty("parameter:tagName", tagName);

		final String actualTagName = getTagName(element);
		return actualTagName == null ? false : compareTagNames(actualTagName, tagName);
	}

	public static String getTagName(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		final String tagName = DOM.getElementProperty(element, DomConstants.TAG_NAME);
		return tagName;
	}

	public static void checkTagName(final String name, final Element element, final String expectedTagName) {
		ObjectHelper.checkNotNull(name, element);
		StringHelper.checkNotEmpty(name, expectedTagName);

		if (false == isTag(element, expectedTagName)) {
			SystemHelper.fail(name, "The " + name + " is not of the expected tag type, expected \"" + expectedTagName + "\", but got \""
					+ getTagName(element) + "\".");
		}
	}

	public static void checkInput(final String name, final Element element, final String type) {
		if (false == isInput(element, type)) {
			SystemHelper.fail("parameter:element", "The input field " + name + " is not of the expected type, type\"" + type
					+ "\", element: " + DOM.toString(element));
		}
	}

	/**
	 * Tests if the given element is an INPUT tag of the requested type.
	 * 
	 * @param element The element being tested.
	 * @param type The type attribute
	 * @return True if the element is the specified INPUT tag.
	 */
	public static boolean isInput(final Element element, final String type) {
		ObjectHelper.checkNotNull("parameter:element", element);
		StringHelper.checkNotEmpty("parameter:type", type);

		boolean is = false;
		while (true) {
			if (false == isTag(element, DomConstants.INPUT_TAG)) {
				is = false;
				break;
			}

			final String actualType = DOM.getElementAttribute(element, DomConstants.INPUT_TAG_TYPE);
			is = type.equalsIgnoreCase(actualType);
			break;
		}
		return is;
	}

	public static boolean compareTagNames(final String tagName, final String otherTagName) {
		StringHelper.checkNotNull("parameter:tagName", tagName);
		StringHelper.checkNotNull("parameter:otherTagName", otherTagName);

		return tagName.equalsIgnoreCase(otherTagName);
	}

	// VIEWS OF DOM COLLECTIONS
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This method may be used to find the first child of the same tag type as
	 * specified by the parameter:childTagName. If none is found a null is
	 * returned.
	 * 
	 * @param parent
	 * @param childTagNameToFind
	 * @return
	 */
	public static Element findFirstChildOfType(final Element parent, final String childTagNameToFind) {
		ObjectHelper.checkNotNull("parameter:parent", parent);
		StringHelper.checkNotEmpty("parameter:childTagNameToFind", childTagNameToFind);

		Element found = null;
		final int childCount = DOM.getChildCount(parent);
		for (int i = 0; i < childCount; i++) {
			final Element child = DOM.getChild(parent, i);
			if (isTag(child, childTagNameToFind)) {
				found = child;
				break;
			}
		}

		return found;
	}

	/**
	 * Creates a list and populates it with all immediate child elements of the
	 * given element.
	 * 
	 * @param parent
	 * @param childTagNameToFind
	 * @return A read only list of Elements.
	 */
	public static List findAllChildrenOfType(final Element parent, final String childTagNameToFind) {
		ObjectHelper.checkNotNull("parameter:parent", parent);
		StringHelper.checkNotEmpty("parameter:childTagNameToFind", childTagNameToFind);

		final List found = new ArrayList();
		final int childCount = DOM.getChildCount(parent);
		for (int i = 0; i < childCount; i++) {
			final Element child = DOM.getChild(parent, i);
			if (isTag(child, childTagNameToFind)) {
				found.add(child);
			}
		}
		return CollectionsHelper.unmodifiableList(found);
	}

	/**
	 * Requests the browser to set focus on the given element.
	 * 
	 * @param focusElement
	 *            the element to receive focus.
	 */
	public static void setFocus(final Element focusElement) {
		ObjectHelper.checkNotNull("paraemter:focusElement", focusElement);

		setFocus0(focusElement);
	}

	private native static void setFocus0(final Element element)/*-{
	 if( element.focus ){
	 element.focus();
	 };
	 }-*/;

	/**
	 * Makes a clone of the given element.
	 * 
	 * @param element
	 * @param deepCopy
	 *            When true performs a deep copy (ie children are also cloned).
	 * @return The cloned element
	 */
	public static Element cloneElement(final Element element, final boolean deepCopy) {
		ObjectHelper.checkNotNull("parameter:element", element);
		return cloneElement0(element, deepCopy);
	}

	native private static Element cloneElement0(final Element element, final boolean deepCopy)/*-{
	 return element.cloneNode( deepCopy );
	 }-*/;

	/**
	 * Retrieves the body of the current document.
	 * 
	 * @return The body element
	 */
	native public static Element getBody()/*-{
	 return $doc.body;
	 }-*/;

	/**
	 * Retrieves the x offset between a child and its parent container in pixels
	 * 
	 * @param element
	 * @return The value in pixels
	 */
	public static int getOffsetLeft(final Element element) {
		return ObjectHelper.getInteger(element, "offsetLeft");
	}

	/**
	 * Retrieves the y offset between a child and its parent container in pixels
	 * 
	 * @param element
	 * @return The value in pixels
	 */
	public static int getOffsetTop(final Element element) {
		return ObjectHelper.getInteger(element, "offsetTop");
	}

	/**
	 * Retrieves the client width of the given element. This is width in pixels
	 * less any decorations such as border or margins.
	 * 
	 * @param element
	 * @return The value in pixels
	 */
	public static int getClientWidth(final Element element) {
		return ObjectHelper.getInteger(element, "clientWidth");
	}

	/**
	 * Retrieves the client height of the given element. This is height in
	 * pixels less any decorations such as border or margins.
	 * 
	 * @param element
	 * @return The value in pixels
	 */
	public static int getClientHeight(final Element element) {
		return ObjectHelper.getInteger(element, "clientHeight");
	}

	/**
	 * Tests if the given element is attached to the dom.
	 * 
	 * @param element
	 * @return A flag indicating whether or not the element is in fact attached to the document.
	 */
	public static boolean isAttached(final Element element) {
		return DOM.isOrHasChild(Dom.getBody(), element);
	}
}