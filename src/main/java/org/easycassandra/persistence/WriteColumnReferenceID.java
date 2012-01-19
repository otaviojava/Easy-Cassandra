package org.easycassandra.persistence;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.easycassandra.util.DomUtil;

/**
 * Process in thread for create XML document
 * @author otavio
 */
class WriteColumnReferenceID implements Runnable {

    /**
     * for lock and unlock this resource
     */
    private static AtomicBoolean execute = new AtomicBoolean(true);
    /**
     * For salve in XML Document
     */
    private AtomicReference<ColumnFamilyIds> superColunaID;

    public WriteColumnReferenceID(AtomicReference<ColumnFamilyIds> superColunaID) {
        this.superColunaID = superColunaID;
    }

    @Override
    public void run() {
        while (execute.get()) {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException ex) {
            }
            DomUtil.getFileDom(superColunaID.get());
        }


    }

    public static void stop() {
        execute.set(false);
    }
}
