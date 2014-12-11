package io.minestack.doublechest.model.servertype;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

public class ServerType implements Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int ram;

    @Getter
    @Setter
    private int players;

    @Override
    public String getKey() {
        return name;
    }

}
