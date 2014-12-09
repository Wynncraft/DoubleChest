package io.minestack.doublechest.databases.riak;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import io.minestack.doublechest.databases.Database;
import lombok.extern.log4j.Log4j2;

import java.net.UnknownHostException;
import java.util.List;

@Log4j2
public class RiakDatabase implements Database {

    private final List<String> addresses;
    private RiakClient client;

    public RiakDatabase(List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public void setupDatabase() {
        RiakNode.Builder builder = new RiakNode.Builder();

        List<RiakNode> nodes;
        RiakCluster cluster;
        try {
            nodes = RiakNode.Builder.buildNodes(builder, addresses);
            cluster = new RiakCluster.Builder(nodes).build();
        } catch (UnknownHostException e) {
            log.error("Threw a SQLException in RiakDatabase::setupDatabase, full stack trace follows: ", e);
            return;
        }
        cluster.start();
        client = new RiakClient(cluster);
    }

    public RiakClient getClient() {
        return client;
    }
}
