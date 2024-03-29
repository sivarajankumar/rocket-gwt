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
package rocket.widget.client.slider;

import rocket.event.client.MouseEvent;
import rocket.style.client.Css;

/**
 * A VerticalSlider is a widget which allows a user to manipulate number value
 * by clicking on different areas of the widget moving along the y-axis.
 * 
 * @author Miroslav (mP)
 */
public class VerticalSlider extends Slider {
	public VerticalSlider() {
		super();
	}

	@Override
	protected String getInitialStyleName() {
		return Constants.VERTICAL_SLIDER_STYLE;
	}

	@Override
	protected String getHandleStyleName() {
		return Constants.VERTICAL_SLIDER_HANDLE_STYLE;
	}

	@Override
	protected String getBackgroundStyleName() {
		return Constants.VERTICAL_SLIDER_BACKGROUND_STYLE;
	}

	@Override
	protected String getSliderDraggingStyleName() {
		return Constants.VERTICAL_SLIDER_DRAGGING_STYLE;
	}

	@Override
	protected int getMousePageCoordinate(final MouseEvent event) {
		return event.getPageY();
	}

	@Override
	protected int getAbsoluteWidgetCoordinate() {
		return this.getElement().getAbsoluteTop();
	}

	@Override
	protected String getHandleCoordinateStylePropertyName() {
		return Css.TOP;
	}

	@Override
	protected int getSliderLength() {
		return this.getOffsetHeight();
	}

	@Override
	protected int getHandleLength() {
		return this.getHandle().getOffsetHeight();
	}
}
