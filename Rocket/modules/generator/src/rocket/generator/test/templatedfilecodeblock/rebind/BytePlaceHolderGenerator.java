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
package rocket.generator.test.templatedfilecodeblock.rebind;

import rocket.generator.rebind.codeblock.ByteLiteral;
import rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockTestConstants;

public class BytePlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "BytePlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "getByteValue";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void visitTemplate(final Template template) {
		template.set("byteValue", new ByteLiteral(TemplatedFileCodeBlockTestConstants.BYTE));
	}

	protected String getNewMethodReturnType() {
		return Byte.TYPE.getName();
	}
}
