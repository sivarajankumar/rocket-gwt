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
package rocket.widget.client.tabpanel;

import rocket.widget.client.Html;

import com.google.gwt.user.client.ui.Widget;

/**
 * The VerticalTabPanel class is the base class for both the LeftTabPanel and
 * RightTabPanel classes.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract class VerticalTabPanel extends TabPanel {

	protected VerticalTabPanel() {
		super();
	}

	protected Widget createTabBarBeforeSpacer() {
		final Html widget = new Html("&nbsp;");
		widget.setStyleName(this.getTabBarBeforeSpacerStyleName());
		widget.setHeight("100%");
		return widget;
	}

	protected abstract String getTabBarBeforeSpacerStyleName();

	protected Widget createTabBarAfterSpacer() {
		final Html widget = new Html("&nbsp;");
		widget.setStyleName(getTabBarAfterSpacerStyleName());
		widget.setHeight("100%");
		return widget;
	}

	protected abstract String getTabBarAfterSpacerStyleName();
}
