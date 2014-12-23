package io.minestack.doublechest.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@RequiredArgsConstructor
public abstract class Model {

    @Getter
    private final ObjectId id;

    @Getter
    @Setter
    private Date updated_at;

    @Getter
    private final Date created_at;

}
