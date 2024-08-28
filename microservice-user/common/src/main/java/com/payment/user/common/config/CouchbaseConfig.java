package com.payment.user.common.config;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Slf4j
@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    //TODO couchbase ile yapılan cache yapısı aynı şekilde redis ile de yapılabilir

    @Value("${couchbase.host}")
    private String host;
    @Value("${couchbase.username}")
    private String username;
    @Value("${couchbase.password}")
    private String password;
    @Value("${couchbase.bucket}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return host;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseCluster(ClusterEnvironment couchbaseClusterEnvironment) {
        try {
            log.info("Connecting to Couchbase cluster at {}", host);
            Cluster connect = Cluster.connect(getConnectionString(), getUserName(), getPassword());

            CouchbaseHealthIndicator indicator = new CouchbaseHealthIndicator(connect);
            Health health = indicator.health();
            var up = health.getStatus().equals(Status.UP);
            var down = health.getStatus().equals(Status.DOWN);

            log.info("Couchbase connected status:{}", health.getStatus());
            return connect;
        } catch (Exception e) {
            log.error("Error connecting to Couchbase cluster", e);
            throw e;
        }
    }


    @Bean
    public CacheManager cacheManager1Day() {
        Cluster cluster = Cluster.connect(getConnectionString(), getUserName(), getPassword());
        return CouchbaseCacheManager.create(new SimpleCouchbaseClientFactory(cluster, getBucketName(), null));
    }


    @Bean
    public Bucket getCouchbaseBucket(Cluster cluster) {
        try {
            if (!cluster.buckets().getAllBuckets().containsKey(getBucketName())) {
                log.error("Bucket with name {} does not exist. Creating it now", getBucketName());
                throw new BucketNotFoundException(bucketName);
            }
            return cluster.bucket(getBucketName());
        } catch (Exception e) {
            log.error("Error getting bucket", e);
            throw e;
        }
    }

}
