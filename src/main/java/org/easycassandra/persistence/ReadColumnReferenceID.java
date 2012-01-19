package org.easycassandra.persistence;

import org.easycassandra.util.DomUtil;
import java.io.File;

/**
 * For read The XML document and retrieve information about Column Family
 * @see  ColumnFamilyIds
 * @author otavio
 */
class ReadColumnReferenceID {

    /**
     * Retrieve the id of  Column Family
     * @return 
     */
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
