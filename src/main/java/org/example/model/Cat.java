package org.example;

import org.example.model.InjectRandomInt;
import org.springframework.stereotype.Component;

@Component
public class Cat {

    @InjectRandomInt(min=1, max =100)
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
