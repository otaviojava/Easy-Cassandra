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

	private static final long TIME_SLEEP = 3000l;

	/**
	 * for see this resource is locked
	 */
	private AtomicBoolean lock;
	
	/**
	 * Information for Write in document
	 */
	private AtomicReference<ColumnFamilyIds> columnReference;
	
	
	/**
	 * Constructor
	 * @param lock
	 * @param columnReference
	 */
	public WriteDocument(AtomicBoolean lock,	AtomicReference<ColumnFamilyIds> columnReference) {
		this.lock = lock;
		this.columnReference = columnReference;
	}



	@Override
	public void run() {
		
			DomUtil.getFileDom(columnReference.get());
			try {
				Thread.sleep(TIME_SLEEP);
			} catch (InterruptedException e) {
			}
			lock.set(false);

		

	}

}
