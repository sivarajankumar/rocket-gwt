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

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * A TopTabPanel arranges its tab titles along the top edge with the remainder
 * allocated to the tab contents of the active tab.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TopTabPanel extends HorizonalTabPanel {

	public TopTabPanel() {
		super();
	}

	@Override
	DockPanel.DockLayoutConstant getTabBarDockPanelConstants() {
		return DockPanel.NORTH;
	}

	@Override
	protected TabPanel.TabBarPanel createTabBarPanel() {
		return this.createTabBarPanel(HasVerticalAlignment.ALIGN_BOTTOM);
	}

	@Override
	protected String getInitialStyleName() {
		return Constants.TOP_TAB_PANEL_STYLE;
	}

	@Override
	protected String getTabBarStyleName() {
		return Constants.TAB_BAR_STYLE;
	}

	@Override
	protected String getTabBarBeforeSpacerStyleName() {
		return Constants.BEFORE_SPACER_STYLE;
	}

	@Override
	protected String getTabBarAfterSpacerStyleName() {
		return Constants.AFTER_SPACER_STYLE;
	}

	@Override
	protected String getTabBarItemStyleName() {
		return Constants.ITEM_STYLE;
	}
	
	@Override
	protected String getTabBarItemLabelStyleName() {
		return Constants.ITEM_LABEL_STYLE;
	}

	@Override
	protected String getTabBarItemWidgetStyleName() {
		return Constants.ITEM_WIDGET_STYLE;
	}

	@Override
	protected String getTabBarItemSelectedStyleName() {
		return Constants.ITEM_SELECTED_STYLE;
	}

	@Override
	protected String getContentPanelStyleName() {
		return Constants.CONTENT_STYLE;
	}

}
