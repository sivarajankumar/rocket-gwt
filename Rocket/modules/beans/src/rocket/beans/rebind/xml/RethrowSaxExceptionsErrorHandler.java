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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Simply rethrows all presented exceptions
 * 
 * @author Miroslav Pokorny
 */
public class RethrowSaxExceptionsErrorHandler implements ErrorHandler {

	public void fatalError(final SAXParseException caught) throws SAXException {
		throw caught;
	}

	public void error(final SAXParseException caught) throws SAXParseException {
		throw caught;
	}

	public void warning(final SAXParseException caught) throws SAXParseException {
		throw caught;
	}
}
