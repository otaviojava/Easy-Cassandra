package org.easycassandra.bean.model;

import java.nio.ByteBuffer;

import org.easycassandra.persistence.cassandra.Customizable;
/**
 * custom to products.
 * @author otaviojava
 */
public class ProductsCustomData implements Customizable {

    @Override
	public ByteBuffer read(Object object) {
		Products products = (Products) object;
		StringBuilder result = new StringBuilder();
		result.append(products.getNome()).append("|");
		result.append(products.getValue()).append("|");
		result.append(products.getCountry());
		return ByteBuffer.wrap(result.toString().getBytes());

	}
    @Override
	public Object write(ByteBuffer byteBuffer) {
	    byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        String[] values = new String(result).split("\\|");

		Products products = new Products();
		products.setNome(values[0]);
		products.setValue(Double.valueOf(values[1]));
		products.setCountry(values[1]);
		return products;
	}

}
