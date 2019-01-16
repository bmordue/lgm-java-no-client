package me.bmordue.lgm.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(me.bmordue.lgm.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Game.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Game.class.getName() + ".players", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Game.class.getName() + ".gameTurns", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.TurnOutcome.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.GameTurn.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.GameTurn.class.getName() + ".actors", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.GameTurn.class.getName() + ".playerTurns", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Player.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Player.class.getName() + ".actors", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Player.class.getName() + ".playerTurns", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Actor.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Actor.class.getName() + ".actorCommands", jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.Landscape.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.ActorCommand.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.PlayerTurn.class.getName(), jcacheConfiguration);
            cm.createCache(me.bmordue.lgm.domain.PlayerTurn.class.getName() + ".actorCommands", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
