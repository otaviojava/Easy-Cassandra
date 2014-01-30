/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.easycassandra.FieldType;
/**
 * class util to verify the relationship between java type and cassandra columns type.
 * @author otaviojava
 *
 */
enum VerifyRowUtil {
INTANCE;
	private Map<FieldType, VerifyRow> verifyMap;
	{
		verifyMap = new EnumMap<>(FieldType.class);
		verifyMap.put(FieldType.ENUM, new EnumVerify());
		verifyMap.put(FieldType.LIST, new ListVerify());
		verifyMap.put(FieldType.SET, new SetVerify());
		verifyMap.put(FieldType.MAP, new MapVerify());
		verifyMap.put(FieldType.CUSTOM, new CustomVerify());
		verifyMap.put(FieldType.COLLECTION, new CollectionVerify());
		verifyMap.put(FieldType.DEFAULT, new DefaultVerify());
	}

    /**
     * the interface based on the kind of field.
     * @param field
     *            the field to verify
     * @return VerifyRow interface
     */
    public VerifyRow factory(Field field) {
        return verifyMap.get(FieldType.getTypeByField(field));
    }

    /**
     * {@link VerifyRow} to custom fields.
     * @author otaviojava
     */
    class CustomVerify implements VerifyRow {
        @Override
        public List<String> getTypes(Field field) {
            return Arrays.asList(new String[]{"blob"});
        }
    }

    /**
     * {@link VerifyRow} to default fields.
     * @author otaviojava
     */
    class DefaultVerify implements VerifyRow {
        @Override
        public List<String> getTypes(Field field) {
            return RelationShipJavaCassandra.INSTANCE.getCQLType(field.getType().getName());
        }
    }
    /**
     * {@link VerifyRow} to Map feids.
     * @author otaviojava
     */
    class MapVerify implements VerifyRow {
        @Override
        public List<String> getTypes(Field field) {
            return Arrays.asList(new String[] { "map" });
        }
    }

    /**
     * {@link VerifyRow} to Set fields.
     * @author otaviojava
     */
    class SetVerify implements VerifyRow {
        @Override
        public List<String> getTypes(Field field) {
            return Arrays.asList(new String[] { "set" });
        }
    }

    /**
     * {@link VerifyRow} to List fields.
     * @author otaviojava
     */
    class ListVerify implements VerifyRow {
        @Override
        public List<String> getTypes(Field field) {
            return Arrays.asList(new String[] { "list" });
        }
    }
    /**
     * {@link VerifyRow} to enum fields.
     * @author otaviojava
     */
    class EnumVerify implements VerifyRow {
        @Override
        public List<String> getTypes(Field field) {
            return RelationShipJavaCassandra.INSTANCE.getCQLType(
                    ColumnUtil.DEFAULT_ENUM_CLASS.getName());
        }
    }
    /**
     * {@link VerifyRow} to Collection fields.
     * @author otaviojava
     */
    class CollectionVerify implements VerifyRow {
		@Override
		public List<String> getTypes(Field field) {
			VerifyRow verifyRow = verifyMap.get(FieldType.findCollectionbyQualifield(field));
			return verifyRow.getTypes(field);
		}
    }
    /**
     * contract to return the list of cassandra columns a determined which java
     *  type is acceptable.
     * @author otaviojava
     */
    public interface VerifyRow {
        /**
         *
         * @param field
         * @return
         */
        List<String> getTypes(Field field);
    }
}
