package org.easycassandra.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *for Read and write a XML Document
 * @author otavio
 */
public class DomUtil {

    private static Logger LOOGER = LoggerFactory.getLogger(DomUtil.class);
    public static final String FILE = "cassandraSuperColunas.xml";

 /**
  * Create a Object from XML Document
  * @param file - a XML Document
  * @param objClass -
  * @return 
  */
    public static Object getDom(File file, Class objClass) {
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(objClass);
            Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
            Object myclass = unmarshaller.unmarshal(file);
            return myclass;
        } catch (JAXBException ex) {
            LOOGER.error("Error parse to Object", ex);
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
        } catch (IOException | JAXBException ex) {
            LOOGER.error("Error parse to File", ex);
        }

        return null;
    }
}