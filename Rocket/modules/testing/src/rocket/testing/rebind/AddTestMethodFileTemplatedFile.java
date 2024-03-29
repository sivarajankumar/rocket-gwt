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
package rocket.testing.rebind;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the add-test.txt template
 * 
 * @author Miroslav Pokorny
 */
class AddTestTemplatedFile extends TemplatedFileCodeBlock {

	public AddTestTemplatedFile() {
		super();
	}

	/**
	 * The test method
	 */
	private Method method;

	protected Method getMethod() {
		Checker.notNull("field:method", method);
		return this.method;
	}

	public void setMethod(final Method setter) {
		Checker.notNull("method:method", setter);
		this.method = setter;
	}

	protected StringLiteral getTestName() {
		return new StringLiteral(this.getMethod().getName());
	}

	protected Type getTest() {
		return this.getMethod().getEnclosingType();
	}

	protected String getResourceName() {
		return Constants.ADD_TEST_METHOD_TEMPLATE;
	}

	public InputStream getInputStream() {
		return super.getInputStream();
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.ADD_TEST_METHOD_METHOD.equals(name)) {
				value = this.getMethod();
				break;
			}
			if (Constants.ADD_TEST_METHOD_TEST_NAME.equals(name)) {
				value = this.getTestName();
				break;
			}
			if (Constants.ADD_TEST_METHOD_TEST_RUNNER.equals(name)) {
				value = this.getTest();
				break;
			}
			break;
		}
		return value;
	}
}
