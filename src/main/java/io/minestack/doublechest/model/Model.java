package io.minestack.doublechest.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

public abstract class Model {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private Timestamp updated_at;

    public abstract String getKey();

    public abstract HashMap<String, String> toHash();

    public abstract void fromHash(HashMap<String, String> hash) throws Exception;

}
