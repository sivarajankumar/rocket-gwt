/*
 * Copyright 2006 Google Inc.
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
package com.google.gwt.user.client.ui.impl;

import com.google.gwt.user.client.Element;

/**
 * Implementation class used by {@link com.google.gwt.user.client.ui.TextBox}.
 */
public class TextBoxImpl {

	public native int getCursorPos(Element elem) /*-{
	   // Guard needed for FireFox.
	    try{
	      return elem.selectionStart;
	    } catch (e) {
	      return 0;
	    }
	 }-*/;

	public native int getSelectionLength(Element elem) /*-{
	   // Guard needed for FireFox.
	   try{
	     return elem.selectionEnd - elem.selectionStart;
	   } catch (e) {
	     return 0;
	   }
	 }-*/;

	public int getTextAreaCursorPos(Element elem) {
		return getCursorPos(elem);
	}

	public native void setSelectionRange(Element elem, int pos, int length) /*-{
	   elem.setSelectionRange(pos, pos + length);
	 }-*/;

	// ROCKET The fields/methods below were added to assist the Rocket
	// framework. When upgrading from GWT 1.5.2 reapply changes

	/**
	 * Delegates to {@link #getSelectionLength(Element)}. The TextBox widget
	 * calls this method instead allowing customisation for the IE special case.
	 * 
	 * @param element
	 * @return
	 */
	public int getTextAreaSelectionLength(final Element element) {
		return this.getSelectionLength(element);
	}

}
