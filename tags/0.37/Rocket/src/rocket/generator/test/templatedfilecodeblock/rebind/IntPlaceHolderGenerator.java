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

import rocket.generator.rebind.codeblock.IntLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockTestConstants;

public class IntPlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "IntPlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "getIntValue";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void visitTemplacedFileCodeBlock(final TemplatedFileCodeBlock template) {
		template.setLiteral("intValue", new IntLiteral(TemplatedFileCodeBlockTestConstants.INT));
	}

	protected String getNewMethodReturnType() {
		return Integer.TYPE.getName();
	}
}
