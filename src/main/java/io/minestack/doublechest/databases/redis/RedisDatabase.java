package io.minestack.doublechest.databases.redis;

import io.minestack.doublechest.databases.Database;
import io.minestack.doublechest.model.network.repository.redis.RedisNetworkRepository;
import io.minestack.doublechest.model.node.repository.redis.RedisNetworkNodeRepository;
import io.minestack.doublechest.model.node.repository.redis.RedisNodePublicAddressRepository;
import io.minestack.doublechest.model.node.repository.redis.RedisNodeRepository;
import io.minestack.doublechest.model.plugin.repository.redis.RedisPluginConfigRepository;
import io.minestack.doublechest.model.plugin.repository.redis.RedisPluginHolderPluginRepository;
import io.minestack.doublechest.model.plugin.repository.redis.RedisPluginRepository;
import io.minestack.doublechest.model.plugin.repository.redis.RedisPluginVersionRepository;
import io.minestack.doublechest.model.pluginhandler.bungeetype.repository.redis.RedisBungeeTypeRepository;
import io.minestack.doublechest.model.pluginhandler.servertype.repository.redis.RedisNetworkServerTypeRepository;
import io.minestack.doublechest.model.pluginhandler.servertype.repository.redis.RedisServerTypeRepository;
import io.minestack.doublechest.model.world.repository.redis.RedisServerTypeWorldRepository;
import io.minestack.doublechest.model.world.repository.redis.RedisWorldRepository;
import io.minestack.doublechest.model.world.repository.redis.RedisWorldVersionRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

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
    private final RedisNetworkNodeRepository networkNodeRepository;

    @Getter
    private final RedisServerTypeRepository serverTypeRepository;

    @Getter
    private final RedisNetworkServerTypeRepository networkServerTypeRepository;

    @Getter
    private final RedisWorldRepository worldRepository;

    @Getter
    private final RedisWorldVersionRepository worldVersionRepository;

    @Getter
    private final RedisServerTypeWorldRepository serverTypeWorldRepository;

    @Getter
    private final RedisPluginRepository pluginRepository;

    @Getter
    private final RedisPluginConfigRepository pluginConfigRepository;

    @Getter
    private final RedisPluginVersionRepository pluginVersionRepository;

    @Getter
    private final RedisPluginHolderPluginRepository pluginHolderPluginRepository;

    @Getter
    private final RedisBungeeTypeRepository bungeeTypeRepository;

    public RedisDatabase(HostAndPort jedisHost) {
        this.jedisHost = jedisHost;
        networkRepository = new RedisNetworkRepository(this);
        nodeRepository = new RedisNodeRepository(this);
        networkNodeRepository = new RedisNetworkNodeRepository(this);
        nodePublicAddressRepository = new RedisNodePublicAddressRepository(this);
        serverTypeRepository = new RedisServerTypeRepository(this);
        networkServerTypeRepository = new RedisNetworkServerTypeRepository(this);
        worldRepository = new RedisWorldRepository(this);
        worldVersionRepository = new RedisWorldVersionRepository(this);
        serverTypeWorldRepository = new RedisServerTypeWorldRepository(this);
        pluginRepository = new RedisPluginRepository(this);
        pluginConfigRepository = new RedisPluginConfigRepository(this);
        pluginVersionRepository = new RedisPluginVersionRepository(this);
        pluginHolderPluginRepository = new RedisPluginHolderPluginRepository(this);
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

    public Object executeCommand(RedisCommand command) throws Exception {
        return executeCommand(command, Object.class);
    }

    public <T> T executeCommand(RedisCommand command, Class<T> resultClass) throws Exception {
        Jedis jedis = pool.getResource();

        Object result = null;
        int tryNumber = 1;

        while (tryNumber < 100) {
            log.info("Executing Redis Command "+command.getCommandName()+" Try Number: "+tryNumber);
            if (command.keysToWatch().length > 0) {
                jedis.watch(command.keysToWatch());
            }
            if (command.conditional(jedis)) {
                Transaction transaction = jedis.multi();
                command.command(transaction);
                if (transaction.exec() != null) {
                    result = command.response();
                    break;
                } else {
                    tryNumber += 1;
                    log.warn("Error executing command "+command.getCommandName()+" trying again...");
                }
            } else {
                if (command.keysToWatch().length > 0) {
                    jedis.unwatch();
                }
                break;
            }
        }

        if (tryNumber == 100) {
            throw new JedisException("Redis Command " + command.getCommandName() + " tried to execute 100 times.");
        }

        pool.returnResource(jedis);
        return resultClass.cast(result);
    }

}
