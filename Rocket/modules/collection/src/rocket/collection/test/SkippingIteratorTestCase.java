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
package rocket.collection.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import rocket.collection.client.SkippingIterator;

public class SkippingIteratorTestCase extends TestCase {
	
	public void testRepeatedHasNext() {

		final List<Object> list = new ArrayList<Object>();
		list.add("apple");
		list.add("banana");
		list.add(new Integer(2));

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
	}

	public void testRepeatedHasNextAgain() {
		final List<Object> list = new ArrayList<Object>();
		list.add("apple");
		list.add("banana");
		list.add("carrot");

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
	}

	public void testNextNextRepeatedHasNext() {
		final List<Object> list = new ArrayList<Object>();
		list.add(new Integer(0));
		list.add("banana");
		list.add("carrot");

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
	}

	public void testIterator() {
		final List<Object> list = new ArrayList<Object>();
		list.add("apple");
		list.add("banana");
		list.add("carrot");

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		assertTrue(iterator.hasNext());
		assertEquals(list.get(0), iterator.next());

		assertTrue(iterator.hasNext());
		assertEquals(list.get(1), iterator.next());

		assertTrue(iterator.hasNext());
		assertEquals(list.get(2), iterator.next());

		assertFalse(iterator.hasNext());
	}

	public void testIteratorSkips() {
		final List<Object> list = new ArrayList<Object>();
		final List<Object> expected = new ArrayList<Object>();

		list.add("apple");
		expected.add("apple");

		list.add(new Integer(1));

		list.add("carrot");
		expected.add("carrot");

		final Iterator<Object> expectedIterator = expected.iterator();

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		assertTrue(iterator.hasNext());
		assertEquals(expectedIterator.next(), iterator.next());

		assertTrue(iterator.hasNext());
		assertEquals(expectedIterator.next(), iterator.next());

		assertFalse(iterator.hasNext());
	}

	public void testNextNextSkipsElement() {
		final List<Object> list = new ArrayList<Object>();
		final List<Object> expected = new ArrayList<Object>();

		list.add("apple");
		expected.add("apple");

		list.add(new Integer(1));

		list.add("carrot");
		expected.add("carrot");

		list.add("dog");
		expected.add("dog");

		list.add(new Integer(4));
		list.add(new Integer(5));
		list.add(new Integer(6));

		list.add("zebra");
		expected.add("zebra");

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		final Iterator<Object> expectedIterator = expected.iterator();

		while (expectedIterator.hasNext()) {
			assertTrue(iterator.hasNext());
			assertEquals(expectedIterator.next(), iterator.next());
		}

		assertFalse(iterator.hasNext());
	}

	public void testRemove() {
		final List<Object> list = new ArrayList<Object>();
		final List<Object> expected = new ArrayList<Object>();

		list.add("apple");
		expected.add("apple");

		list.add(new Integer(1));

		list.add("carrot");
		expected.add("carrot");

		list.add("dog");
		expected.add("dog");

		list.add(new Integer(4));
		list.add(new Integer(5));
		list.add(new Integer(6));

		list.add("zebra");
		expected.add("zebra");

		final TestSkippingIterator<Object> iterator = new TestSkippingIterator<Object>();
		iterator.setIterator(list.iterator());

		final Iterator<Object> expectedIterator = expected.iterator();

		while (expectedIterator.hasNext()) {
			assertTrue(iterator.hasNext());

			final Object visited = expectedIterator.next();
			assertEquals(visited, iterator.next());

			iterator.remove();

			assertFalse("The item " + visited + " was just removed and should not be present in the backing list.", list
					.contains(visited));
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 * This iterator skips objects that are not of type String.
	 * 
	 * @author Miroslav Pokorny (mP)
	 * 
	 */
	class TestSkippingIterator<E> extends SkippingIterator<E> {
		public boolean skip(final E visit) {
			return !(visit instanceof String);
		}
	}
}
