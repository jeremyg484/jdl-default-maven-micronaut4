package com.mycompany.myapp.config;

import com.mycompany.myapp.util.JHipsterProperties;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.util.Properties;
import javax.cache.CacheManager;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;

@Factory
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Singleton
    public CacheManager cacheManager(ApplicationContext applicationContext) {
        CacheManager cacheManager = new EhcacheCachingProvider()
            .getCacheManager(null, applicationContext.getClassLoader(), new Properties());
        customizeCacheManager(cacheManager);
        return cacheManager;
    }

    private void customizeCacheManager(CacheManager cm) {
        createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_CACHE);
        createCache(cm, com.mycompany.myapp.domain.User.class.getName());
        createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
        createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
        // jhipster-needle-ehcache-add-entry
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
