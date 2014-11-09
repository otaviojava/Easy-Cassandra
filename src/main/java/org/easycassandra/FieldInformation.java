package org.easycassandra;

import java.lang.reflect.Field;

import org.easycassandra.util.ReflectionUtil;

/**
 * this data structured  has information about the field on a class.
 * @author otaviojava
 *
 */
public class FieldInformation implements Comparable<FieldInformation> {
    /**
     * the kind of field from annotation.
     */
    private FieldType type;
    /**
     * the field.
     */
    private Field field;
    /**
     * the name.
     */
    private String name;
    /**
     * if the {@link FieldInformation#type} been
     * {@link FieldType#COLLECTION} will use it to define
     * the kind of collection, if not will be
     * {@link FieldType#EMPTY}.
     */
    private FieldType collectionType;

    /**
     * if the {@link FieldInformation#type} been
     * {@link FieldType#COLLECTION}, {@link FieldType#SET}
     * {@link FieldType#LIST}, {@link FieldType#MAP} if not will {@link FieldType#EMPTY}.
     */
    private Class<?> key;
    /**
     * if the {@link FieldInformation#type} been
     * {@link FieldType#MAP}, if not will {@link FieldType#EMPTY}.
     */
    private Class<?> value;
    /**
     * indicates this field is embedded.
     */
    private boolean embedded;
    /**
     * indicates this field is key.
     */
    private boolean keyCheck;
    /**
     * indicates this field is partkey.
     */
    private boolean partkeyCheck;

    /**
     * indicates if the field is part of ordering cluster.
     */
    private boolean clusteringOrderCheck;

    /**
     * indicates the order
     */
    private Order order;

    /**
     * list of information when this field is embedded or embedded Id.
     */
    private ClassInformation subFields;

    /**
     * {@link FieldInformation#type}.
     * @return {@link FieldInformation#type}
     */
    public FieldType getType() {
        return type;
    }
    /**
     * {@link FieldInformation#field}.
     * @return {@link FieldInformation#field}
     */
    public Field getField() {
        return field;
    }

    /**
     * {@link FieldInformation#name}.
     * @return {@link FieldInformation#name}
     */
    public String getName() {
        return name;
    }
    /**
     * {@link FieldInformation#collectionType}.
     * @return {@link FieldInformation#collectionType}
     */
    public FieldType getCollectionType() {
        return collectionType;
    }
    /**
     * {@link FieldInformation#key}.
     * @return {@link FieldInformation#key}
     */
    public Class<?> getKey() {
        return key;
    }
    /**
     * {@link FieldInformation#value}.
     * @return {@link FieldInformation#value}
     */
    public Class<?> getValue() {
        return value;
    }
    /**
     * {@link FieldInformation#embedded}.
     * @return {@link FieldInformation#embedded}
     */
    public boolean isEmbedded() {
        return embedded;
    }
    /**
     * {@link FieldInformation#subFields}.
     * @return {@link FieldInformation#subFields}
     */
    public ClassInformation getSubFields() {
        return subFields;
    }
    public boolean isKeyCheck() {
        return keyCheck;
    }

    public boolean isPartKeyCheck() {
        return partkeyCheck;
    }

    public boolean isClusteringOrderCheck() {
        return clusteringOrderCheck;
    }

    public Order getOrder() {
        return order;
    }

    /**
     * create the class with field.
     * @param field - field to receive the information
     */
    FieldInformation(Field field) {
        this.embedded = ColumnUtil.INTANCE.isEmbeddedField(field)
                || ColumnUtil.INTANCE.isEmbeddedIdField(field);

        this.keyCheck = ColumnUtil.INTANCE.isEmbeddedIdField(field)
                || ColumnUtil.INTANCE.isIdField(field);

        this.partkeyCheck = ColumnUtil.INTANCE.isPartkeyField(field);

        this.clusteringOrderCheck = ColumnUtil.INTANCE.isClusteringOrderField(field);

        this.order = ColumnUtil.INTANCE.getClusterOrder(field);

        this.type = FieldType.getTypeByField(field);
        this.field = field;
        this.collectionType = FieldType.EMPTY;
        this.name = ColumnUtil.INTANCE.getColumnName(field);
        ReflectionUtil.INSTANCE.makeAccessible(field);
        if (embedded) {
            subFields = ClassInformations.INSTACE.getClass(field.getType());
        }
        definesType();

    }
    /**
     * constructor just to bynary search.
     * @param name the name of field
     */
    FieldInformation(String name) {
        this.name = name;
    }
    private void definesType() {
        switch (type) {
            case COLLECTION:
                this.collectionType = FieldType.findCollectionbyQualifield(field);
                definesCollectionType();
                break;
            case SET:
            case LIST:
                key = ReflectionUtil.INSTANCE.getGenericType(field);
                break;
            case MAP:
                ReflectionUtil.KeyValueClass keyValueClass = ReflectionUtil.INSTANCE
                    .getGenericKeyValue(field);
                this.key = keyValueClass.getKeyClass();
                this.value = keyValueClass.getValueClass();
                break;
            default:
                break;
        }
    }

    private void definesCollectionType() {
        switch (collectionType) {
            case SET:
            case LIST:
                key = ReflectionUtil.INSTANCE.getGenericType(field);
                break;
            case MAP:
                ReflectionUtil.KeyValueClass keyValueClass = ReflectionUtil.INSTANCE
                .getGenericKeyValue(field);
                this.key = keyValueClass.getKeyClass();
                this.value = keyValueClass.getValueClass();
                break;
            default:
                break;
        }
    }
    @Override
    public int compareTo(FieldInformation other) {
        return name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
