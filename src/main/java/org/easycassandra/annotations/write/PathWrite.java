/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import java.nio.file.Path;
/**
 * Class for transform Path in ByteBuffer
 * @author otavio
 */
public class PathWrite implements WriteInterface{

	
	private final FileWrite fileWrite=new FileWrite();
	@Override
	public ByteBuffer getBytebyObject(Object object) {
		
	Path path=(Path) object;
	
		return fileWrite.getBytebyObject(path.toFile());
	}

}
