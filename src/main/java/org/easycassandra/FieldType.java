package org.easycassandra;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easycassandra.persistence.cassandra.ColumnUtil;

/**
 * enum that contains kinds of annotations to fields on java.
 */
public enum FieldType {
 ENUM, LIST, SET, MAP, COLLECTION, CUSTOM, DEFAULT;
 
 public static FieldType getTypeByField(Field field) {
	 
	 if (ColumnUtil.INTANCE.isEnumField(field)) {
	     return ENUM;
	 }
	 if (ColumnUtil.INTANCE.isList(field)) {
	     return LIST;
	 }
	 if (ColumnUtil.INTANCE.isSet(field)) {
	     return SET;
	 }
	 if (ColumnUtil.INTANCE.isMap(field)) {
	     return MAP;
	 }
	 if (ColumnUtil.INTANCE.isElementCollection(field)) {
	     return COLLECTION;
	 }
	 if (ColumnUtil.INTANCE.isCustom(field)) {
	     return CUSTOM;
	 }
	 return DEFAULT;
 }

 private static final String LIST_QUALIFIELD = "java.util.List";
 private static final String SET_QUALIFIELD = "java.util.Set";
 private static final String MAP_QUALIFIELD = "java.util.Map";

 public static FieldType findCollectionbyQualifield(Field field) {

		switch (field.getType().getName()) {
		case LIST_QUALIFIELD:
			return LIST;
		case SET_QUALIFIELD:
			return SET;
		case MAP_QUALIFIELD:
			return MAP;
		default:
			return DEFAULT;
		}
 }

}
