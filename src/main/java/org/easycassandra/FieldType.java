package org.easycassandra;

import java.lang.reflect.Field;

/**
 * enum that contains kinds of annotations to fields on java.
 */
public enum FieldType {
 ENUM, LIST, SET, MAP, COLLECTION, CUSTOM, DEFAULT, EMPTY;

 /**
  * find you the kind of annotation on field and then define a enum type, follow the sequences:
  * <ul>
  *   <li>Enum</li>
  *   <li>List</li>
  *   <li>Set</li>
  *   <li>Map</li>
  *   <li>Collection</li>
  *   <li>Custom</li>
  *   <li>Default - if there is not a annotation</li>
  *   </ul>.
  * @param field - the field with annotation
  * @return the type
  */
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

    /**
     * find out the kind of enum on type of Collection.
     * @param field - the field with annotation
     * @return the enum type
     */
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
