package io.minestack.doublechest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public abstract class Model {

    @Getter
    @Setter
    private int id;

    public abstract String getKey();

    public abstract HashMap<String, Object> toHash();

}
