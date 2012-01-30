package org.easycassandra.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;



public final class FileUtil {
 
    private static final int BUFFER_FILE = 512;



    private FileUtil() {
        // Construtor privado
    }

	public static File byteFromFile(byte[] data, String arquivoDestino) {
		FileOutputStream fos = null;
		byte[] buf = null;
		int read = 0;
		ByteArrayInputStream input = null;
		File arquivo = new File(arquivoDestino);
		try {

			fos = new FileOutputStream(arquivo);
			buf = new byte[BUFFER_FILE];

			input = new ByteArrayInputStream(data);
			while ((read = input.read(buf)) != -1) {
				fos.write(buf, 0, read);
			}

			fos.flush();
			fos.close();

		} catch (Exception exception) {
			Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null,
					exception);
		}

		return arquivo;
	}

	public static byte[] fileToByteArray(File file) {

		InputStream inputStream = null;

		try {

			inputStream = new FileInputStream(file);

			byte[] bytes = new byte[(int) file.length()];

			inputStream.read(bytes);

			inputStream.close();

			byte[] arquivoByte = new byte[bytes.length];
                        System.arraycopy(bytes, 0, arquivoByte, 0, bytes.length);
                        
		        return arquivoByte;

		} catch (IOException exception) {
			Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null,
					exception);
		}

		return new byte[0];
	}

}
