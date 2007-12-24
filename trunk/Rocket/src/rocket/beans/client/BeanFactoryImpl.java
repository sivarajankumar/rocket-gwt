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
package rocket.beans.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

/**
 * This class contains a number of common properties and methods that will be
 * required by all generated {@link BeanFactory} implementations.
 * 
 * @author Miroslav Pokorny
 */
abstract public class BeanFactoryImpl implements BeanFactory {

	public BeanFactoryImpl() {
		super();

		this.setFactoryBeans(this.createFactoryBeans());
		this.registerFactoryBeans();
		this.prepareFactoryBeans();
		this.registerAliases();
		this.loadEagerBeans();
	}

	/**
	 * This method is implemented by the code generator to create BeanFactory
	 * instances for each bean defined.
	 */
	abstract protected void registerFactoryBeans();

	/**
	 * Performs a dual role firstly it registers a bean and also sets the bean name upon the factory bean.
	 * @param name The bean name
	 * @param factoryBean The matching factory bean
	 */
	public void registerFactoryBean(final String name, final FactoryBean factoryBean) {
		if (factoryBean instanceof BeanNameAware) {
			final BeanNameAware beanNameAware = (BeanNameAware) factoryBean;
			beanNameAware.setBeanName(name);
		}

		this.getFactoryBeans().put(name, factoryBean);
	}

	/**
	 * Visits all factory beans setting the bean factory for BeanFactoryAware
	 * objects.
	 */
	protected void prepareFactoryBeans() {
		final Iterator iterator = this.getFactoryBeans().entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();

			final Object factoryBean = entry.getValue();

			if (factoryBean instanceof BeanNameAware) {
				final BeanNameAware beanNameAware = (BeanNameAware) factoryBean;
				beanNameAware.setBeanName((String) entry.getKey());
			}

			if (factoryBean instanceof BeanFactoryAware) {
				final BeanFactoryAware beanFactoryAware = (BeanFactoryAware) factoryBean;
				beanFactoryAware.setBeanFactory(this);
			}

		}
	}

	/**
	 * Updates the {@link #factoryBeans} map with aliases taken from {@link #getAliasesToBeans()} so that
	 * factory bean instances will exist at the original bean name and any aliases.
	 */
	protected void registerAliases() {
		final String aliasesToBeans = this.getAliasesToBeans();
		final String[] tokens = StringHelper.split(aliasesToBeans, ",", true);
		final Map factoryBeans = this.getFactoryBeans();

		for (int i = 0; i < tokens.length; i++) {
			final String token = tokens[i];
			final int equals = token.indexOf('=');
			final String alias = token.substring(0, equals);
			final String bean = token.substring(equals + 1);

			final FactoryBean factoryBean = this.getFactoryBean(bean);
			factoryBeans.put(alias, factoryBean);
		}
	}

	/**
	 * A list of comma separated alias to bean mappings.
	 * @return
	 */
	abstract protected String getAliasesToBeans();

	/**
	 * Retrieves and throws away all non lazy (eager) singleton beans.
	 */
	protected void loadEagerBeans() {
		final String commaSeparatedList = this.getEagerSingletonBeanNames();
		final String[] beanNames = StringHelper.split(commaSeparatedList, ",", true);
		for (int i = 0; i < beanNames.length; i++) {
			final String beanName = beanNames[i];
			Object ignored = this.getBean(beanName);

			if (false == GWT.isScript()) {
				PrimitiveHelper.checkTrue("The bean \"" + beanName + "\" must be a singleton.", this.isSingleton(beanName));
			}
		}
	}

	/**
	 * A comma separated list of beans that wish to be eagerly and not lazy loaded.
	 * 
	 * Eager beans will be loaded when the factory starts up.
	 * @return An array of beans which may be empty
	 */
	abstract protected String getEagerSingletonBeanNames();

	/**
	 * This map consists of all the bean factories that will return bean
	 * instances.
	 */
	private Map factoryBeans;

	protected Map getFactoryBeans() {
		ObjectHelper.checkNotNull("field:factoryBeans", factoryBeans);
		return this.factoryBeans;
	}

	protected void setFactoryBeans(final Map factoryBeans) {
		ObjectHelper.checkNotNull("parameter:factoryBeans", factoryBeans);
		this.factoryBeans = factoryBeans;
	}

	protected Map createFactoryBeans() {
		return new HashMap();
	}

	public Object getBean(final String name) {
		FactoryBean factoryBean = this.getFactoryBean(name);
		Object bean = null;
		try {
			bean = factoryBean.getObject();
		} catch (final RuntimeException runtimeException) {
			throw new BeanException("Unable to get bean \"" + name + "\" because " + runtimeException.getMessage(), runtimeException);
		}

		return bean;
	}

	public boolean isSingleton(String name) {
		return this.getFactoryBean(name).isSingleton();
	}

	/**
	 * Attempts to get the FactoryMethodBean given a name. If the factory is not
	 * found an exception is thrown.
	 * 
	 * @param name The bean name
	 * @return The FactoryBean identified by the given bean name.
	 * @throws UnableToFindBeanException if the bean doesnt exist.
	 */
	protected FactoryBean getFactoryBean(final String name) throws UnableToFindBeanException {
		final FactoryBean factory = (FactoryBean) this.getFactoryBeans().get(name);
		if (null == factory) {
			throwUnableToFindBean("Unable to find bean \"" + name + "\".");
		}
		return factory;
	}

	protected void throwUnableToFindBean(final String message) throws UnableToFindBeanException {
		throw new UnableToFindBeanException(message);
	}
}
