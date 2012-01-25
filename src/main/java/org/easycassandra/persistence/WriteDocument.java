package org.easycassandra.persistence;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.easycassandra.util.DomUtil;

/**
 * This class is for Write the informations about id in
 * Document XML
 * @author otavio
 *
 */
public class WriteDocument implements Runnable {

	private AtomicBoolean lock;
	
	private AtomicReference<ColumnFamilyIds> columnReference;
	
	
	
	public WriteDocument(AtomicBoolean lock,	AtomicReference<ColumnFamilyIds> columnReference) {
		this.lock = lock;
		this.columnReference = columnReference;
	}



	@Override
	public void run() {
		
			DomUtil.getFileDom(columnReference.get());
			try {
				Thread.sleep(3000l);
			} catch (InterruptedException e) {
			}
			lock.set(false);

		

	}

}
