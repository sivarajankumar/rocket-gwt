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
package rocket.remoting.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FailedWebRequestException extends RuntimeException implements IsSerializable {

	public FailedWebRequestException() {
		super();
	}

	public FailedWebRequestException(final String message) {
		super(message);
	}

	public FailedWebRequestException(final String message, final Throwable caught) {
		super(message, caught);
	}

	public FailedWebRequestException(final Throwable caught) {
		super(caught);
	}
}
