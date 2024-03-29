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
package rocket.generator.rebind.type;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.NewConstructor;

/**
 * Represents a inner class being built.
 * 
 * @author Miroslav Pokorny
 */
public interface NewNestedType extends NewType {

	NewConstructor newConstructor();

	void addConstructor(NewConstructor constructor);

	Type getEnclosingType();

	String getNestedName();

	void setNestedName(String name);

	void setAbstract(boolean abstractt);

	void setFinal(boolean finall);

	void setStatic(boolean staticc);

	Visibility getVisibility();

	void setVisibility(Visibility visibility);
}
