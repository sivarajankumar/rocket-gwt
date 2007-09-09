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
package rocket.json.rebind;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the set-list templated file
 * 
 * @author Miroslav Pokorny
 */
public class SetFieldTemplatedFile extends TemplatedCodeBlock {

	public SetFieldTemplatedFile() {
		super();
		setNative(true);
	}

	private Field field;

	protected Field getField() {
		ObjectHelper.checkNotNull("list:list", field);
		return this.field;
	}

	public void setField(final Field field) {
		ObjectHelper.checkNotNull("parameter:list", field);
		this.field = field;
	}

	private MethodParameter instance;

	protected MethodParameter getInstance() {
		ObjectHelper.checkNotNull("list:instance", instance);
		return this.instance;
	}

	public void setInstance(final MethodParameter instance) {
		ObjectHelper.checkNotNull("parameter:instance", instance);
		this.instance = instance;
	}

	private MethodParameter value;

	protected MethodParameter getValue() {
		ObjectHelper.checkNotNull("list:value", value);
		return this.value;
	}

	public void setValue(final MethodParameter value) {
		ObjectHelper.checkNotNull("parameter:value", value);
		this.value = value;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.SET_FIELD_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.SET_FIELD_FIELD.equals(name)) {
				value = this.getField();
				break;
			}
			if (Constants.SET_FIELD_INSTANCE.equals(name)) {
				value = this.getInstance();
				break;
			}
			if (Constants.SET_FIELD_VALUE.equals(name)) {
				value = this.getValue();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in file [" + Constants.SET_FIELD_TEMPLATE
				+ "]");
	}
}
