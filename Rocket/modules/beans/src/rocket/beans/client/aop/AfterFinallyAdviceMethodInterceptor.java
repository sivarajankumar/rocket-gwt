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
package rocket.beans.client.aop;

import rocket.util.client.Checker;

/**
 * A simple adapter between a {@link MethodInterceptor} and an finally advice
 * 
 * This class is typically used by generated proxies as a bridge between a
 * {@link AfterFinallyAdvice} and a proxy.
 * 
 * @author Miroslav Pokorny
 */
public class AfterFinallyAdviceMethodInterceptor implements MethodInterceptor {

	public Object invoke(final MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		} finally {
			final AfterFinallyAdvice advice = this.getAfterFinallyAdvice();
			advice.afterFinally();
		}
	}

	private AfterFinallyAdvice afterFinallyAdvice;

	protected AfterFinallyAdvice getAfterFinallyAdvice() {
		Checker.notNull("field:afterFinallyAdvice", afterFinallyAdvice);
		return afterFinallyAdvice;
	}

	public void setAfterFinallyAdvice(final AfterFinallyAdvice finallyAdvice) {
		Checker.notNull("parameter:afterFinallyAdvice", finallyAdvice);
		this.afterFinallyAdvice = finallyAdvice;
	}

}
