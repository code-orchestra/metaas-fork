/*
 * BeforeAfterStrategy.java
 * 
 * Copyright (c) 2008 David Holroyd
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

package uk.co.badgersinfoil.metaas.visitor;

import uk.co.badgersinfoil.metaas.dom.ScriptElement;

public class BeforeAfterStrategy extends FilterStrategy {

	private ScriptElementStrategy before;
	private ScriptElementStrategy after;

	public BeforeAfterStrategy() {
		this.before = null;
		this.after = null;
	}

	public BeforeAfterStrategy(ScriptElementStrategy filtered, ScriptElementStrategy before, ScriptElementStrategy after) {
		super(filtered);
		this.before = before;
		this.after = after;
	}

	public void handle(ScriptElement element) {
		before(element);
		super.handle(element);
		after(element);
	}

	protected void after(ScriptElement element) {
		if (after != null) {
			after.handle(element);
		}
	}

	protected void before(ScriptElement element) {
		if (before != null) {
			before.handle(element);
		}
	}

	public ScriptElementStrategy getBefore() {
		return before;
	}

	public void setBefore(ScriptElementStrategy before) {
		this.before = before;
	}

	public ScriptElementStrategy getAfter() {
		return after;
	}

	public void setAfter(ScriptElementStrategy after) {
		this.after = after;
	}
}
