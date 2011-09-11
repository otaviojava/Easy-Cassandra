/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import org.easycassandra.util.DomUtil;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author otavio
 */
class WriteColumnReferenceID implements Runnable {

    private static AtomicBoolean execute = new AtomicBoolean(true);
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
