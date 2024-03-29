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
package rocket.beans.rebind.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Attempts to load the bean-factory dtd
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryDtdEntityResolver implements EntityResolver {
	public InputSource resolveEntity(final String publicId, final String systemId) throws IOException, SAXException {
		InputSource inputSource = null;

		if (Constants.PUBLIC_ID.equals(publicId)) {
			final InputStream inputStream = this.getClass().getResourceAsStream(Constants.DTD_FILE_NAME);
			if (null == inputStream) {
				throw new IOException("Unable to locate DTD file \"" + Constants.DTD_FILE_NAME + "\".");
			}
			inputSource = new InputSource(inputStream);
		}
		return inputSource;
	}
}
