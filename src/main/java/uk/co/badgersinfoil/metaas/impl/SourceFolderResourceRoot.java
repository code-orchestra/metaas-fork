/*
 * SourceFolderResourceRoot.java
 * 
 * Copyright (c) 2006 David Holroyd
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO: files names are not enough; need to parse the file contents for the posibility of multiple top-level definitions

/**
 * A ResourceRoot implementation that finds QNames from ActionScript source
 * files in a source folder hierarchy.
 */
public class SourceFolderResourceRoot implements ResourceRoot {
	private File path;
	private List qnames = null;

	public SourceFolderResourceRoot(File path) {
		this.path = path;
	}

	public List getDefinitionQNames() {
		if (qnames == null) {
			List result = new ArrayList();
			loadQNames("", result);
			qnames = toQNames(result);
		}
		return qnames;
	}

	private List toQNames(List files) {
		List result = new ArrayList();
		for (Iterator i=files.iterator(); i.hasNext(); ) {
			String file = (String)i.next();
			result.add(toQName(file));
		}
		return result;
	}

	private ASQName toQName(String file) {
		String typeName = file.replace(File.separatorChar, '.').substring(0, file.length()-3);
		return new ASQName(typeName);
	}

	private void loadQNames(String subfolder, List result) {
		File here = new File(path, subfolder);
		File[] list = here.listFiles();
		for (int i=0; i<list.length; i++) {
			File entry = list[i];
			String name = entry.getName();
			String newname;
			if (subfolder.length() == 0) {
				newname = name;
			} else {
				newname = subfolder+File.separator+name;
			}
			if (entry.isDirectory()) {
				loadQNames(newname, result);
			} else {
				if (name.endsWith(".as")) {
					result.add(newname);
				}
			}
		}
	}
}