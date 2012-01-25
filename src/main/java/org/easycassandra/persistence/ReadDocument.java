package org.easycassandra.persistence;

import java.io.File;

import org.easycassandra.util.DomUtil;

/**
 * 
 * Class for read Document XML with informations
 * in ColumnFamilyIds
 * @see ColumnFamilyIds
 * @author otavio
 *
 */
public class ReadDocument {

	
	public ColumnFamilyIds read(){
		File file=new File(DomUtil.FILE);
		if(!file.exists()){
			return new ColumnFamilyIds();
		}
		return (ColumnFamilyIds) DomUtil.getDom(file, ColumnFamilyIds.class);
	}
	
}
