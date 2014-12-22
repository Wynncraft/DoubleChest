package io.minestack.doublechest.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public abstract class Model {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private Timestamp updated_at;

}
