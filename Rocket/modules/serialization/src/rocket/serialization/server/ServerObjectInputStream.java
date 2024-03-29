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
package rocket.serialization.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.serialization.client.Constants;
import rocket.serialization.client.ObjectInputStreamImpl;
import rocket.serialization.client.SerializationException;
import rocket.util.client.Checker;
import rocket.util.client.Utilities;

public class ServerObjectInputStream extends ObjectInputStreamImpl {

	public ServerObjectInputStream(final String stream) {
		super();

		this.setObjects(this.createObjects());
		this.prepare(stream);
	}

	@Override
	public boolean readBoolean() {
		return this.nextValue().equals("true");
	}

	@Override
	public byte readByte() {
		return Byte.parseByte(this.nextValue());
	}

	@Override
	public short readShort() {
		return Short.parseShort(this.nextValue());
	}

	@Override
	public int readInt() {
		return Integer.parseInt(this.nextValue());
	}

	@Override
	public long readLong() {
		final long hi = this.readInt();
		final long lo = this.readInt();

		return (hi << 32) | (lo & 0xffffffff);
	}

	@Override
	public float readFloat() {
		return Float.parseFloat(this.nextValue());
	}

	@Override
	public double readDouble() {
		return Double.parseDouble(this.nextValue());
	}

	@Override
	public char readChar() {
		return (char) Integer.parseInt(this.nextValue());
	}

	protected int readReference() {
		return this.readInt();
	}

	/**
	 * A cache of all the string values that have been scene...
	 */
	private Map<Integer, String> strings;

	protected Map<Integer, String> getStrings() {
		return this.strings;
	}

	protected void setStrings(final Map<Integer, String> strings) {
		this.strings = strings;
	}

	protected Map<Integer, String> createStrings() {
		return new HashMap<Integer, String>();
	}

	protected String getString(final int reference) {
		final String string = this.getStrings().get(reference);
		if (null == string) {
			throwInvalidStringReference(reference);
		}
		return string;
	}

	/**
	 * key: Objects value2: reference
	 */
	private Map<Integer, Object> objects;

	protected Map<Integer, Object> getObjects() {
		return this.objects;
	}

	protected void setObjects(final Map<Integer, Object> objects) {
		this.objects = objects;
	}

	protected Map<Integer, Object> createObjects() {
		return new HashMap<Integer, Object>();
	}

	protected int addObject(final Object object) {
		final Map<Integer, Object> objects = this.getObjects();
		final int reference = -(objects.size() + 1);
		objects.put(new Integer(reference), object);

		return reference;
	}

	protected void replaceObject(final int reference, final Object object) {
		final Map objects = this.getObjects();
		objects.put(reference, object);
	}

	protected Object getObject(final int reference) {
		final Map objects = this.getObjects();
		Object object = objects.get(reference);
		if (null == object) {
			this.throwInvalidObjectReference(reference);
		}
		return object;
	}

	// build string table...
	// build a different array of values.
	protected void prepare(final String stream) {
		Checker.notEmpty("parameter:stream", stream);

		if (false == stream.startsWith("[") && false == stream.endsWith("]")) {
			throw new SerializationException("Stream is not valid json.");
		}

		this.prepare0(stream.substring(1, stream.length() - 1));
	}

	protected void prepare0(final String stream) {
		final int comma = stream.indexOf(',');
		final String countString = stream.substring(0, comma);
		int count = Integer.parseInt(countString);
		if (count < 0) {
			throw new SerializationException("String table count is invalid, count: " + count);
		}

		// extract strings and add them to the string table...
		final Map<Integer, String> strings = this.createStrings();
		int j = comma + 1;
		for (int i = 0; i < count; i++) {
			final StringBuffer buf = new StringBuffer();

			// consume leading double quote...
			final char first = stream.charAt(j);
			j++;
			Checker.equals("must be start of quoted string", '\"', first);

			while (true) {
				// consume a char within the quoted string...
				final char c = stream.charAt(j);
				j++;

				// check if closing quote...
				if (c == '"') {
					break;
				}

				// not a backslash add literally...
				if (c != '\'') {
					buf.append(c);
					continue;
				}

				// handle escaped char...
				final char d = stream.charAt(j);
				j++;

				if (d == '\0') {
					buf.append('\0');
					continue;
				}
				if (d == '\t') {
					buf.append('\t');
					continue;
				}
				if (d == '\n') {
					buf.append('\n');
					continue;
				}
				if (d == '\r') {
					buf.append('\r');
					continue;
				}
				if (d == '\\') {
					buf.append('\\');
					continue;
				}
				if (d == '"') {
					buf.append('"');
					continue;
				}
				if (d == 'u') {
					final String codeString = stream.substring(j, j + 4);
					final int code = Integer.parseInt(codeString);
					buf.append((char) code);

					// consume trailing semi colon
					final char semiColon = stream.charAt(j);
					if (semiColon != ';') {
						throw new SerializationException("Stream is corrupted, expected semiColon got \"" + semiColon + "\".");
					}
					j++;
					continue;
				}
				throw new SerializationException("Unknown escaped char '" + d + "'.");
			}

			// add string to table...
			final Integer reference = new Integer(i + Constants.STRING_BIAS);
			strings.put(reference, buf.toString());

			// consume the trailing comma
			// if( i > 0 ){
			// consume comma...
			final char comma2 = stream.charAt(j);
			j++;
			Checker.equals("Unable to find separating comma", ',', comma2);
			// }
		}
		this.setStrings(strings);

		// j has pointer to start of values...
		final String[] values = Utilities.split(stream.substring(j), ",", true);
		this.setValues(values);
	}

	private String[] values;

	protected String[] getValues() {
		Checker.notNull("field:values", values);
		return this.values;
	}

	protected void setValues(final String[] values) {
		Checker.notNull("parameter:values", values);
		this.values = values;
	}

	private int valueIndex;

	protected int getValueIndex() {
		return this.valueIndex;
	}

	protected void setValueIndex(final int valueIndex) {
		this.valueIndex = valueIndex;
	}

	protected String nextValue() {
		final int valueIndex = this.getValueIndex();
		final String value = this.getValues()[valueIndex];
		this.setValueIndex(valueIndex + 1);
		return value;
	}

	protected Object readNewObject0(final String typeName) {
		Object object = null;

		boolean read = false;

		Class classs = this.getType(typeName);
		final Iterator readers = this.getObjectReaders().iterator();
		while (readers.hasNext()) {
			final ServerObjectReader reader = (ServerObjectReader) readers.next();
			if (reader.canRead(classs)) {
				read = true;
				object = reader.newInstance(typeName, this);
				this.addObject(object);
				reader.read(object, this);
				break;
			}
		}
		if (false == read) {
			this.throwUnableToDeserialize(typeName);
		}

		return object;
	}

	protected Class getType(final String typeName) {
		try {
			return Class.forName(typeName);
		} catch (final ClassNotFoundException classNotFound) {
			throw new SerializationException("Unable to instantiate " + typeName, classNotFound);
		}
	}

	private List<ServerObjectReader> objectReaders;

	protected List<ServerObjectReader> getObjectReaders() {
		Checker.notNull("field:objectReaders", objectReaders);
		return this.objectReaders;
	}

	public void setObjectReaders(final List<ServerObjectReader> objectReaders) {
		Checker.notNull("parameter:objectReaders", objectReaders);
		this.objectReaders = objectReaders;
	}

	protected List<ServerObjectReader> createObjectReaders() {
		return new ArrayList<ServerObjectReader>();
	}
}
