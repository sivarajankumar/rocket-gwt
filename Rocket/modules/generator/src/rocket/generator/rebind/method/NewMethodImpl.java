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
package rocket.generator.rebind.method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorException;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.metadata.MetaData;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameterImpl;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Represents a new method that will be added to a new class being built
 * 
 * @author Miroslav Pokorny
 */
public class NewMethodImpl extends AbstractMethod implements NewMethod {

	public NewMethodImpl() {
		super();

		this.setComments("");
		this.setMetaData(this.createMetaData());
	}

	/**
	 * When true indicates that this method is abstract
	 */
	private boolean abstractt;

	public boolean isAbstract() {
		return abstractt;
	}

	public void setAbstract(final boolean abstractt) {
		this.abstractt = abstractt;
	}

	/**
	 * When true indicates that this method is final
	 */
	private boolean finall;

	public boolean isFinal() {
		return finall;
	}

	public void setFinal(final boolean finall) {
		this.finall = finall;
	}

	/**
	 * When true indicates that this method is static
	 */
	private boolean staticc;

	public boolean isStatic() {
		return staticc;
	}

	public void setStatic(final boolean staticc) {
		this.staticc = staticc;
	}

	/**
	 * When true indicates that this method is native
	 */
	private boolean nativee;

	public boolean isNative() {
		return nativee;
	}

	public void setNative(final boolean nativee) {
		this.nativee = nativee;
	}

	public void setVisibility(final Visibility visibility) {
		super.setVisibility(visibility);
	}

	/**
	 * The name of the method.
	 */
	private String name;

	public String getName() {
		GeneratorHelper.checkJavaMethodName("field:name", name);
		return name;
	}

	protected boolean hasName() {
		return null != name;
	}

	public void setName(final String name) {
		GeneratorHelper.checkJavaMethodName("parameter:name", name);
		this.name = name;
	}

	protected List<MethodParameter> createParameters() {
		return new ArrayList<MethodParameter>();
	}

	public NewMethodParameter newParameter() {
		final NewMethodParameterImpl parameter = new NewMethodParameterImpl();
		parameter.setGeneratorContext(this.getGeneratorContext());

		this.addParameter(parameter);

		return parameter;
	}

	public void addParameter(final NewMethodParameter parameter) {
		Checker.notNull("parameter:parameter", parameter);

		this.getParameters().add(parameter);
		parameter.setEnclosingMethod(this);
	}

	protected Set<Type> createThrownTypes() {
		return new HashSet<Type>();
	}

	public void addThrownTypes(final Type thrownTypes) {
		Checker.notNull("thrownTypes:thrownTypes", thrownTypes);
		this.getThrownTypes().add(thrownTypes);
	}

	public void setReturnType(final Type returnType) {
		super.setReturnType(returnType);
	}

	/**
	 * A code block which may or may not statements etc that hold the body of
	 * this method
	 */
	private CodeBlock body;

	public CodeBlock getBody() {
		Checker.notNull("field:body", body);
		return this.body;
	}

	protected boolean hasBody() {
		return null != this.body;
	}

	public void setBody(final CodeBlock body) {
		Checker.notNull("parameter:body", body);
		this.body = body;
	}

	/**
	 * Any text which will appear within javadoc comments for this field.
	 */
	private String comments;

	public String getComments() {
		Checker.notNull("field:comments", comments);
		return comments;
	}

	public void setComments(final String comments) {
		Checker.notNull("parameter:comments", comments);
		this.comments = comments;
	}

	public void addMetaData(final String name, final String value) {
		this.getMetaData().add(name, value);
	}

	public List<String> getMetadataValues(final String name) {
		return this.getMetaData().getMetadataValues(name);
	}

	/**
	 * A container which holds any meta data that is added to a new field
	 * instance.
	 */
	private MetaData metaData;

	protected MetaData getMetaData() {
		Checker.notNull("field:metaData", metaData);
		return this.metaData;
	}

	protected void setMetaData(final MetaData metaData) {
		Checker.notNull("parameter:metaData", metaData);
		this.metaData = metaData;
	}

	protected MetaData createMetaData() {
		return new MetaData();
	}

	/**
	 * Writes the method in its entirety to the provided SourceWriter
	 * 
	 * @param writer
	 */
	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		this.log();

		this.writeComments(writer);

		// FIXME HACK HACK - get rid of this when proper annotations are supported via a fascade for the GWT classes
		// over to proper annotations.
		writer.println("@com.google.gwt.core.client.UnsafeNativeLong");

		this.writeDeclaration(writer);

		if (this.isAbstract()) {
			this.writeAbstractMethod(writer);

		} else {
			this.writeBodyOpen(writer);

			writer.indent();
			this.writeBody(writer);
			writer.outdent();

			this.writeBodyClose(writer);
		}
	}

	protected void log() {
		final StringBuffer buf = new StringBuffer();

		if (this.isFinal()) {
			buf.append("final ");
		}
		if (this.isAbstract()) {
			buf.append("abstract ");
		}
		if (this.isStatic()) {
			buf.append("static ");
		}
		if (this.isNative()) {
			buf.append("native ");
		}
		buf.append(this.getVisibility().toString());
		buf.append(' ');// Yes package private methods will have two spaces
		// here...

		final Type returnType = this.getReturnType();
		buf.append(returnType.getName());
		buf.append(' ');

		buf.append(this.getName());
		buf.append('(');

		final Iterator<MethodParameter> parameters = this.getParameters().iterator();
		while (parameters.hasNext()) {
			final MethodParameter parameter = parameters.next();
			final Type parameterType = parameter.getType();
			buf.append(parameterType.getName());

			if (parameters.hasNext()) {
				buf.append(',');
			}
		}

		buf.append(')');

		this.getGeneratorContext().debug(buf.toString());
	}

	protected void writeComments(final SourceWriter writer) {
		GeneratorHelper.writeComments(this.getComments(), this.getMetaData(), writer);
	}

	/**
	 * Writes the method declaration of this method. final static ${visibility}
	 * ${name} ( ${parameter-type parameter-name} )
	 * 
	 * @param writer
	 */
	protected void writeDeclaration(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		if (this.isFinal()) {
			writer.print("final ");
		}
		if (this.isStatic()) {
			writer.print("static ");
		}
		if (this.isNative()) {
			writer.print("native ");
		}

		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.getReturnType().getName());
		writer.print(" ");
		writer.print(this.getName());
		writer.print("(");

		this.writeParameters(writer);

		writer.print(")");

		GeneratorHelper.writeThrownTypes(this.getThrownTypes(), writer);
	}

	/**
	 * Writes out the parameters belonging to this method as a comma separated
	 * list.
	 * 
	 * @param writer
	 */
	protected void writeParameters(final SourceWriter writer) {
		GeneratorHelper.writeClassComponents(this.getParameters(), writer, true, false);
	}

	protected void writeBodyOpen(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		if (this.isNative()) {
			writer.println("/*-{");
		} else {
			writer.println("{");
		}
	}

	/**
	 * Writes the body of this method.
	 * 
	 * @param writer
	 */
	public void writeBody(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final CodeBlock body = this.getBody();
		if (false == body.isEmpty()) {
			writer.indent();
			body.write(writer);
			writer.outdent();
		}
	}

	protected void writeBodyClose(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		if (this.isNative()) {
			writer.println("}-*/;");
		} else {
			writer.println("} // " + this.getName());
		}
	}

	protected void writeAbstractMethod(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		if (this.hasBody()) {
			throw new GeneratorException("Inconsistent state abstract method contains a body " + this);
		}

		writer.println(";");
	}

	/**
	 * Builds a string representation of this method in the following form
	 * 
	 * <ul>
	 * <li>Native</li>
	 * <li>final</li>
	 * <li>Static</li>
	 * <li>Abstract</li>
	 * <li>Visibility</li>
	 * <li>Return type</li>
	 * <li>Enclosing type</li>
	 * <li>Name</li>
	 * <li>Left parenthesis</li>
	 * <li>comma separated parameter types</li>
	 * <li>Right parenethesis</li>
	 * </ul>
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		if (this.isNative()) {
			builder.append("native ");
		}
		if (this.isFinal()) {
			builder.append("final ");
		}
		if (this.isStatic()) {
			builder.append("static ");
		}
		if (this.isAbstract()) {
			builder.append("abstract ");
		}

		builder.append(this.getVisibility());
		builder.append(' ');

		if (this.hasReturnType()) {
			builder.append(this.getReturnType().getName());
			builder.append(' ');
		}

		if (this.hasEnclosingType()) {
			builder.append(this.getEnclosingType().getName());
			builder.append('.');
		}

		if (this.hasName()) {
			builder.append(this.getName());
		}

		builder.append('(');
		if (this.hasParameters()) {
			final Iterator<MethodParameter> parameters = this.getParameters().iterator();
			while (parameters.hasNext()) {
				final NewMethodParameter parameter = (NewMethodParameter)parameters.next();
				builder.append(parameter.getType());

				if (parameters.hasNext()) {
					builder.append(", ");
				}
			}
		}
		builder.append(')');

		return builder.toString();
	}
}
