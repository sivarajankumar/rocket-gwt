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
package rocket.browser.client.support;

import com.google.gwt.user.client.Event;

/**
 * This class provides the support that a standards based browser following w3c
 * specifications would require.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class BrowserSupport {
	native public int getScrollX()/*-{
		 return $wnd.scrollX;
		 }-*/;

	native public int getScrollY()/*-{
		 return $wnd.scrollY;
		 }-*/;

	native public int getClientWidth()/*-{
		 return $wnd.innerWidth;
		 }-*/;

	native public int getClientHeight()/*-{
		 return $wnd.innerHeight;
		 }-*/;

	public int getMousePageX(final Event event) {
		return this.getScrollX() + event.getClientX();
	}

	public int getMousePageY(final Event event) {
		return this.getScrollY() + event.getClientY();
	}
}
