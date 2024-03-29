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
package rocket.serialization.test.server.reader;

import java.util.ArrayList;
import java.util.List;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.reader.ListReader;
import rocket.serialization.test.server.ServerTestCase;

public class ListReaderTestCase extends ServerTestCase {

	final String ARRAYLIST = ArrayList.class.getName();

	public void testReadEmptyList() {
		final String stream = "[1,\"" + ARRAYLIST + "\",1,2,0]";
		final ObjectInputStream reader = createObjectInputStream(stream, ListReader.instance);
		final List list = (List) reader.readObject();
		assertNotNull(list);
		assertEquals("" + list, 0, list.size());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadListWithNullElement() {
		final String stream = "[1,\"" + ARRAYLIST + "\",1,2,1,0]";
		final ObjectInputStream reader = createObjectInputStream(stream, ListReader.instance);
		final List list = (List) reader.readObject();
		assertNotNull(list);
		assertEquals("" + list, 1, list.size());
		assertNull("" + list, list.get(0));

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadListSingleElement() {
		final String stream = "[2,\"" + ARRAYLIST + "\",\"" + APPLE + "\",1,2,1,3]";
		final ObjectInputStream reader = createObjectInputStream(stream, ListReader.instance);
		final List list = (List) reader.readObject();
		assertNotNull(list);
		assertEquals("" + list, 1, list.size());
		assertEquals("" + list, APPLE, list.get(0));

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadListWithElements() {
		final String stream = "[3,\"" + ARRAYLIST + "\",\"" + APPLE + "\",\"" + BANANA + "\",1,2,3,3,4,0]";
		final ObjectInputStream reader = createObjectInputStream(stream, ListReader.instance);
		final List list = (List) reader.readObject();
		assertNotNull(list);
		assertEquals("" + list, 3, list.size());
		assertEquals("" + list, APPLE, list.get(0));
		assertEquals("" + list, BANANA, list.get(1));
		assertNull("" + list, list.get(2));

		this.verifyFurtherReadsFail(reader);
	}
}
