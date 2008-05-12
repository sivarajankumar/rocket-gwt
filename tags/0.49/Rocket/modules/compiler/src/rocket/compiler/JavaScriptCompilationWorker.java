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
package rocket.compiler;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.js.ast.JsProgram;

/**
 * Represents a component that visits a programs tree when it is in javascript ast form.
 * Typically a compilation worker will then attempt to perform some optimisation.
 * @author Miroslav Pokorny
 */
public interface JavaScriptCompilationWorker {
	boolean work(JsProgram jsprogram, TreeLogger logger);
}
