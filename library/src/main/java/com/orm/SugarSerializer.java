package com.orm;

import java.lang.reflect.Field;

/**
 * A custom serializer that the client can implement to serialize any data structure the way they
 * want. Must be used in conjunction with {@link com.orm.annotation.Serialize} annotation.
 *
 * TODO:
 * - Use a generic serializer instead of just String
 * - Accept multiple, and register each with a type
 */
public interface SugarSerializer {

    String serialize(Field field, Object data);

    Object deserialize(Field field, String serializedData);

}
