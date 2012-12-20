/*
 * ASQName.java
 * 
 * Copyright (c) 2007 David Holroyd
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

package uk.co.badgersinfoil.metaas.impl;

class ASQName {
	private String packagePrefix;
	private String localName;

	public ASQName(String packagePrefix, String localName) {
		this.packagePrefix = packagePrefix;
		this.localName = localName;
	}
	public boolean isQualified() {
		return packagePrefix != null;
	}
	public ASQName(String qname) {
		int pos = qname.lastIndexOf('.');
		if (pos != -1) {
			packagePrefix = qname.substring(0, pos);
			localName = qname.substring(pos+1);
		} else {
			packagePrefix = null;
			localName = qname;
		}
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getPackagePrefix() {
		return packagePrefix;
	}
	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}
	
	public String toString() {
		if (isQualified()) {
			return packagePrefix + "." + localName;
		}
		return localName;
	}

	// eclipse generated
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((localName == null) ? 0 : localName.hashCode());
		result = PRIME * result + ((packagePrefix == null) ? 0 : packagePrefix.hashCode());
		return result;
	}
	// eclipse generated
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ASQName other = (ASQName)obj;
		if (localName == null) {
			if (other.localName != null)
				return false;
		} else if (!localName.equals(other.localName))
			return false;
		if (packagePrefix == null) {
			if (other.packagePrefix != null)
				return false;
		} else if (!packagePrefix.equals(other.packagePrefix))
			return false;
		return true;
	}
}