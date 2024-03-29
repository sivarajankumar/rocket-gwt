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

package rocket.beans.test.generator.test;

import rocket.beans.rebind.BeanFactoryGenerator;
import rocket.generator.rebind.TestGenerator;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;

/**
 * This generator invokes the real
 * {@link BeanFactoryGenerator#generate(TreeLogger, com.google.gwt.core.ext.GeneratorContext, String)}
 * catching any exceptions that are thrown returning a FailedGenerateAttempt
 * instead.
 * 
 * @author Miroslav Pokorny
 */
public class TestBeanFactoryGenerator extends TestGenerator {

	protected Generator createGenerator() {
		return new BeanFactoryGenerator();
	}
}
