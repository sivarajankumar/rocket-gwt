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
package rocket.beans.rebind.aop.proxyinterceptedmethod;

/**
 * A collection constants used by various classes within this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	final static String TEMPLATE = "proxy-intercepted-method.txt";

	final static String ADD_ADVICES = "addAdvices";

	final static String TARGET = "target";

	final static String WRAP_PARAMETERS = "wrapParameters";

	final static String INVOKE_TARGET_METHOD = "invokeTargetMethod";

	final static String INTERCEPTOR_CHAIN_INVOKE_PROCEED = "interceptorChainInvokeProceed";

	final static String RETHROW_EXPECTED_EXCEPTIONS = "rethrowExpectedExceptions";

	final static String EXCEPTION = Exception.class.getName();

	final static String RUNTIME_EXCEPTION = RuntimeException.class.getName();

	final static String METHOD_NAME = "methodName";

	final static String IS_METHOD_NATIVE = "isMethodNative";

	final static String ENCLOSING_TYPE = "enclosingType";
	final static String METHOD_RETURN_TYPE = "methodReturnType";
	final static String METHOD_PARAMETER_TYPENAMES = "methodParameterTypes";
}
