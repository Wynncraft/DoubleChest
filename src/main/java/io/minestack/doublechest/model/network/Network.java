package io.minestack.doublechest.model.network;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.node.NetworkNode;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Network extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    private ArrayList<NetworkNode> nodes = new ArrayList<>();

    @Getter
    private ArrayList<NetworkServerType> serverTypes = new ArrayList<>();

}
