/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import org.easycassandra.util.DomUtil;
import java.io.File;

/**
 *
 * @author otavio
 */
class ReadColumnReferenceID {

    public static ColumnFamilyIds getColunaID() {
        File file = new File("easycassandra.xml");
        if (!file.exists()) {
            return new ColumnFamilyIds();
        } else {

            ColumnFamilyIds superColunaID = (ColumnFamilyIds) DomUtil.getDom(file, ColumnFamilyIds.class);
            if (superColunaID != null) {

                return superColunaID;
            } else {

                return new ColumnFamilyIds();
            }


        }



    }
}
