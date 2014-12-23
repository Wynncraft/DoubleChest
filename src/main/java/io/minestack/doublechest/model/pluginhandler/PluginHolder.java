package io.minestack.doublechest.model.pluginhandler;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public abstract class PluginHolder extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    private ArrayList<PluginHolderPlugin> plugins = new ArrayList<>();

    public PluginHolder(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
