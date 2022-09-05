package learnorm.util;

import learnorm.annotation.*;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class EntityUtil {

    public static String resolveColumnName(Field field) {
        return Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::value)
                .orElse(field.getName());
    }

    public static <T> String resolveTableName(Class<T> entityType) {
        return Optional.ofNullable(entityType.getAnnotation(Table.class))
                .map(Table::value)
                .orElse(entityType.getSimpleName());
    }

    public static <T> Field getIdField(Class<T> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        "Field marked with @Id hasn't been found in class: " + entityType.getSimpleName())
                );
    }

    @SneakyThrows
    public static Object getId(Object entity) {
        var entityType = entity.getClass();
        var idField = getIdField(entityType);
        idField.setAccessible(true);
        return idField.get(entity);
    }

    public static boolean isRegularField(Field field) {
        return !isEntityField(field) && !isEntityCollectionField(field);
    }

    public static boolean isEntityField(Field field) {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    public static boolean isEntityCollectionField(Field field) {
        return field.isAnnotationPresent(OneToMany.class);
    }

    public static <T> Field getRelatedEntityField(Class<T> fromEntity, Class<?> toEntity) {
        return Arrays.stream(toEntity.getDeclaredFields())
                .filter(f -> f.getType().equals(fromEntity))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Related field not found"));
    }
}
