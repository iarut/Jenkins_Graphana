package org.example.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.example.model.InjectRandomInt;

@Component
@Scope("prototype")
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
