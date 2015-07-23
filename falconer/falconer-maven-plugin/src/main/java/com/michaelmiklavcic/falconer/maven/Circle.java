package com.michaelmiklavcic.falconer.maven;

@org.codehaus.plexus.component.annotations.Component(role = Shape.class)
public class Circle implements Shape {

    @Override
    public String sayHello() {
        return "Hello, I'm a circle!";
    }

}
