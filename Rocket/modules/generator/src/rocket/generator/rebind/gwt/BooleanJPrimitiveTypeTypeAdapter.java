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
package rocket.generator.rebind.gwt;

import rocket.generator.rebind.primitive.BooleanPrimitiveType;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * A singleton that presents the primitive boolean type.
 * 
 * @author Miroslav Pokorny
 */
public class BooleanJPrimitiveTypeTypeAdapter extends BooleanPrimitiveType {

	public BooleanJPrimitiveTypeTypeAdapter( final TypeOracleGeneratorContext context ){
		super();
		
		this.setGeneratorContext(context);
	}
	
	public String getName() {
		return this.getJPrimitiveType().getQualifiedSourceName();
	}

	public String getSimpleName() {
		return this.getJPrimitiveType().getSimpleSourceName();
	}

	public String getJsniNotation() {
		return this.getJPrimitiveType().getJNISignature();
	}

	protected JPrimitiveType getJPrimitiveType() {
		return JPrimitiveType.BOOLEAN;
	}

	@Override
	public String toString() {
		return "boolean";
	}
}
