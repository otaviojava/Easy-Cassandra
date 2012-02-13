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
public final class DomUtil {

 
    public static final String FILE = "cassandraSuperColunas.xml";

 /**
  * Create a Object from XML Document
  * @param file - a XML Document
  * @param objClass -
  * @return the Object
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
 * @return the file within object
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
    
    private DomUtil(){
        
    }
}