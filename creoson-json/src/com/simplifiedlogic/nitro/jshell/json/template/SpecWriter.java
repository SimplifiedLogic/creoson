/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions: The above copyright 
 * notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.simplifiedlogic.nitro.jshell.json.template;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * This was a start on a class to write JSON data "prettily" to output.  Wound up not being used.
 * @author Adam Andrews
 *
 */
public class SpecWriter {

	private Writer w;
	private boolean pretty;
	
	public SpecWriter(Writer w) {
		this(w, false);
	}
	
	public SpecWriter(Writer w, boolean pretty) {
		this.w = w;
		this.pretty = pretty;
	}
	
	public void writeValue(int indent, String name, Object value, boolean comma) throws IOException {
		if (pretty) {
			for (int i=0; i<indent; i++) 
				w.write("    ");
		}
		w.write('"');
		w.write(name);
		w.write('"');
		w.write(":");
		if (value==null) {
			w.write('"');
			w.write('"');
		}
		else if (value instanceof String) {
			w.write('"');
			w.write(value.toString());
			w.write('"');
		}
		else if (value instanceof Map) {
			
		}
		else if (value instanceof List) {
			
		}
		else
			w.write(value.toString());
		if (comma)
			w.write(',');
		if (pretty)
			w.write('\n');
	}
	
	public void writeOpenBrace(int indent) throws IOException {
		writeChar(indent, '{', false);
	}

	public void writeCloseBrace(int indent, boolean comma) throws IOException {
		writeChar(indent, '}', comma);
	}

	public void writeOpenBracket(int indent) throws IOException {
		writeChar(indent, '[', false);
	}

	public void writeCloseBracket(int indent, boolean comma) throws IOException {
		writeChar(indent, ']', comma);
	}

	private void writeChar(int indent, char c, boolean comma) throws IOException {
		if (pretty) {
			for (int i=0; i<indent; i++) 
				w.write("    ");
		}
		w.write(c);
		if (comma)
			w.write(',');
		if (pretty)
			w.write('\n');
	}
	
	public void close() throws IOException {
		w.close();
	}

	public boolean isPretty() {
		return pretty;
	}

	public void setPretty(boolean pretty) {
		this.pretty = pretty;
	}
}
