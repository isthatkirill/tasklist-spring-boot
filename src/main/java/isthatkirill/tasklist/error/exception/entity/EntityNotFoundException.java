package isthatkirill.tasklist.error.exception.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author Kirill Emelyanov
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityClass, Long entityId) {
        super("Entity " + entityClass.getSimpleName() + " not found. Id=" + entityId);
    }

    public EntityNotFoundException(Class<?> entityClass, String entityName) {
        super("Entity " + entityClass.getSimpleName() + " not found. " + entityName);
    }

}
