package org.easycassandra.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 *for Read and write a XML Document
 * @author otavio
 */
public class DomUtil {

 
    public static final String FILE = "cassandraSuperColunas.xml";

 /**
  * Create a Object from XML Document
  * @param file - a XML Document
  * @param objClass -
  * @return 
  */
    public static Object getDom(File file, @SuppressWarnings("rawtypes") Class objClass) {
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(objClass);
            Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
            return unmarshaller.unmarshal(file);
            
        } catch (JAXBException exception) {
             Logger.getLogger(DomUtil.class.getName()).log(Level.SEVERE, null, exception);
        }

        return null;
    }
/**
 * Create a document XML from The object
 * @param object
 * @return 
 */
    public static File getFileDom(Object object) {
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            File file = new File(FILE);
            marshaller.marshal(object, new FileWriter(file.getAbsolutePath()));
            return file;
        } catch (IOException | JAXBException exception) {
            Logger.getLogger(DomUtil.class.getName()).log(Level.SEVERE, null, exception);
        }

        return null;
    }
}