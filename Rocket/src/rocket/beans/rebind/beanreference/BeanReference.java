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
package rocket.beans.rebind.beanreference;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Holds a bean reference value. This class also includes the logic that tests if a bean reference can be
 * set upon to a property
 * 
 * @author Miroslav Pokorny
 */
public class BeanReference extends Value {

	public boolean isCompatibleWith(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		return this.getType().isAssignableTo(type);
	}

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		final BeanReferenceTemplatedFile template = new BeanReferenceTemplatedFile();

		template.setType(this.getType());
		template.setId(this.getId());

		template.write(writer);
	}

	private String id;

	public String getId() {
		StringHelper.checkNotEmpty("field:id", id);
		return this.id;
	}

	public void setId(final String id) {
		StringHelper.checkNotEmpty("parameter:id", id);
		this.id = id;
	}
}
