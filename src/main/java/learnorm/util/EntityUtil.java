package learnorm.util;

import learnorm.annotation.Column;
import learnorm.annotation.Id;
import learnorm.annotation.Table;

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
}
