package io.minestack.doublechest.databases.redis;

import io.minestack.doublechest.databases.Database;
import io.minestack.doublechest.model.network.RedisNetworkRepository;
import io.minestack.doublechest.model.node.RedisNodeInfoRepository;
import io.minestack.doublechest.model.node.RedisNodePublicAddressRepository;
import io.minestack.doublechest.model.node.RedisNodeRepository;
import io.minestack.doublechest.model.plugin.RedisPluginConfigRepository;
import io.minestack.doublechest.model.plugin.RedisPluginInfoRepository;
import io.minestack.doublechest.model.plugin.RedisPluginRepository;
import io.minestack.doublechest.model.plugin.RedisPluginVersionRepository;
import io.minestack.doublechest.model.type.bungeetype.RedisBungeeTypeRepository;
import io.minestack.doublechest.model.type.servertype.RedisServerTypeInfoRepository;
import io.minestack.doublechest.model.type.servertype.RedisServerTypeRepository;
import io.minestack.doublechest.model.world.RedisWorldInfoRepository;
import io.minestack.doublechest.model.world.RedisWorldRepository;
import io.minestack.doublechest.model.world.RedisWorldVersionRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Log4j2
public class RedisDatabase implements Database{

    private final HostAndPort jedisHost;
    private JedisPool pool;

    @Getter
    private final RedisNetworkRepository networkRepository;

    @Getter
    private final RedisNodeRepository nodeRepository;

    @Getter
    private final RedisNodePublicAddressRepository nodePublicAddressRepository;

    @Getter
    private final RedisNodeInfoRepository nodeInfoRepository;

    @Getter
    private final RedisServerTypeRepository serverTypeRepository;

    @Getter
    private final RedisServerTypeInfoRepository serverTypeInfoRepository;

    @Getter
    private final RedisWorldRepository worldRepository;

    @Getter
    private final RedisWorldVersionRepository worldVersionRepository;

    @Getter
    private final RedisWorldInfoRepository worldInfoRepository;

    @Getter
    private final RedisPluginRepository pluginRepository;

    @Getter
    private final RedisPluginConfigRepository pluginConfigRepository;

    @Getter
    private final RedisPluginVersionRepository pluginVersionRepository;

    @Getter
    private final RedisPluginInfoRepository pluginInfoRepository;

    @Getter
    private final RedisBungeeTypeRepository bungeeTypeRepository;

    public RedisDatabase(HostAndPort jedisHost) {
        this.jedisHost = jedisHost;
        networkRepository = new RedisNetworkRepository(this);
        nodeRepository = new RedisNodeRepository(this);
        nodeInfoRepository = new RedisNodeInfoRepository(this);
        nodePublicAddressRepository = new RedisNodePublicAddressRepository(this);
        serverTypeRepository = new RedisServerTypeRepository(this);
        serverTypeInfoRepository = new RedisServerTypeInfoRepository(this);
        worldRepository = new RedisWorldRepository(this);
        worldVersionRepository = new RedisWorldVersionRepository(this);
        worldInfoRepository = new RedisWorldInfoRepository(this);
        pluginRepository = new RedisPluginRepository(this);
        pluginConfigRepository = new RedisPluginConfigRepository(this);
        pluginVersionRepository = new RedisPluginVersionRepository(this);
        pluginInfoRepository = new RedisPluginInfoRepository(this);
        bungeeTypeRepository = new RedisBungeeTypeRepository(this);
    }

    @Override
    public void setupDatabase() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setMaxTotal(20);
        config.setMaxIdle(50);

        pool = new JedisPool(config, jedisHost.getHost(), jedisHost.getPort());
    }

    public Object executeCommand(RedisCommand command) {
        return executeCommand(command, Object.class);
    }

    public <T> T executeCommand(RedisCommand command, Class<T> resultClass) {
        Jedis jedis = pool.getResource();
        Object result = command.command(jedis);
        pool.returnResource(jedis);
        return resultClass.cast(result);
    }

}
