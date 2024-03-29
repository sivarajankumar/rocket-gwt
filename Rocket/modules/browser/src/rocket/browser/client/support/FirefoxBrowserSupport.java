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
 * Provides support for the Firefox browser where it defers from the standard
 * support.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class FirefoxBrowserSupport extends BrowserSupport {
	@Override
	public int getMousePageX(final Event event) {
		return event.getClientX();
	}

	@Override
	public int getMousePageY(final Event event) {
		return event.getClientY();
	}
}
