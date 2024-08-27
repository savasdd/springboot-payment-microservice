package com.payment.user.common.config;

import com.couchbase.client.core.diagnostics.DiagnosticsResult;
import com.couchbase.client.java.Cluster;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;


public class CouchbaseHealthIndicator extends AbstractHealthIndicator {

    private final Cluster cluster;

    public CouchbaseHealthIndicator(Cluster cluster) {
        super("Couchbase health check failed");
        this.cluster = cluster;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        DiagnosticsResult diagnostics = this.cluster.diagnostics();
        new CouchbaseHealth(diagnostics).applyTo(builder);
    }
}
