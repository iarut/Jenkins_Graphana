package org.example.model;

import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        Product product = new Product();
        Class<Product> productClass = Product.class;
        Field[] fields = productClass.getDeclaredFields();
        for (Field field : fields) {

            for(Annotation annotation : field.getAnnotations()) {
                System.out.println(" - " + annotation.annotationType().getSimpleName());

                Class<? extends Annotation> type = annotation.annotationType();

                for (Method method : type.getDeclaredMethods()) {
                    try {
                        Object value = method.invoke(annotation);
                        System.out.printf("    %s = %s%n", method.getName(), value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Method[] methods = productClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        Annotation[] annotations = productClass.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation.annotationType().getSimpleName());
        }
    }
}