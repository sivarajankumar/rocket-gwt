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
package rocket.beans.rebind.constructor;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.Checker;

/**
 * An abstraction for the constructor template
 * 
 * @author Miroslav Pokorny
 */
public class ConstructorTemplatedFile extends TemplatedFileCodeBlock {

	public ConstructorTemplatedFile() {
		super();
		this.setArguments(this.createArguments());
	}

	private Constructor bean;

	protected Constructor getBean() {
		Checker.notNull("field:bean", bean);
		return this.bean;
	}

	public void setBean(final Constructor bean) {
		Checker.notNull("parameter:bean", bean);
		this.bean = bean;
	}

	private List<Value> arguments;

	protected List<Value> getArguments() {
		Checker.notNull("field:addParameters", arguments);
		return this.arguments;
	}

	protected void setArguments(final List<Value> arguments) {
		Checker.notNull("parameter:arguments", arguments);
		this.arguments = arguments;
	}

	protected List<Value> createArguments() {
		return new ArrayList<Value>();
	}

	public void addArgument(final Value value) {
		Checker.notNull("parameter:value", value);
		this.getArguments().add(value);
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.CONSTRUCTOR.equals(name)) {
				value = this.getBean();
				break;
			}
			if (Constants.ARGUMENTS.equals(name)) {
				value = this.getArgumentsAsCodeBlock();
				break;
			}
			break;
		}
		return value;
	}

	protected CodeBlock getArgumentsAsCodeBlock() {
		final List<Value> values = this.getArguments();
		return new CollectionTemplatedCodeBlock<Value>() {

			@Override
			public InputStream getInputStream() {
				return new StringBufferInputStream("${value}");
			}

			@Override
			protected Object getValue0(final String name) {
				return values.get(this.getIndex());
			}

			@Override
			@SuppressWarnings("unchecked")
			protected Collection<Value> getCollection() {
				return values;
			}

			@Override
			protected void prepareToWrite(final Value value) {
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.print(",");
			}
		};
	}
}
