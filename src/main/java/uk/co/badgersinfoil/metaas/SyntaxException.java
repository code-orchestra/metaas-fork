/*
 * SyntaxException.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.badgersinfoil.metaas;

/**
 * Thrown when ActionScript code which is syntactically invalid is encountered.
 * The cause, if defined, may contain further details describing what
 * syntactic problem was encountered.
 */
public class SyntaxException extends RuntimeException {

    private int lineNumber = -1;

    /**
	 * Constructs a new SyntaxException with the specified detail message.
	 */
	public SyntaxException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new SyntaxException with the specified cause.
	 */
	public SyntaxException(Exception e) {
		super(e);
	}

	public SyntaxException(String msg, Exception e) {
		super(msg, e);
	}

    public SyntaxException(String msg, Exception e, int lineNumber) {
		super(msg, e);
        this.lineNumber = lineNumber;
	}

    public int getLineNumber() {
        return lineNumber;
    }

}
