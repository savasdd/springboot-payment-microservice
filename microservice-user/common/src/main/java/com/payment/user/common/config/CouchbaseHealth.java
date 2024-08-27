package com.payment.user.common.config;

import com.couchbase.client.core.diagnostics.ClusterState;
import com.couchbase.client.core.diagnostics.DiagnosticsResult;
import com.couchbase.client.core.diagnostics.EndpointDiagnostics;
import org.springframework.boot.actuate.health.Health.Builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CouchbaseHealth {

    private final DiagnosticsResult diagnostics;

    CouchbaseHealth(DiagnosticsResult diagnostics) {
        this.diagnostics = diagnostics;
    }

    void applyTo(Builder builder) {
        builder = isCouchbaseUp(this.diagnostics) ? builder.up() : builder.down();
        builder.withDetail("sdk", this.diagnostics.sdk());
        builder.withDetail("endpoints",
                this.diagnostics.endpoints()
                        .values()
                        .stream()
                        .flatMap(Collection::stream)
                        .map(this::describe).collect(Collectors.toList()));
    }

    private boolean isCouchbaseUp(DiagnosticsResult diagnostics) {
        return diagnostics.state() == ClusterState.ONLINE;
    }

    private Map<String, Object> describe(EndpointDiagnostics endpointHealth) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", endpointHealth.id());
        map.put("lastActivity", endpointHealth.lastActivity());
        map.put("local", endpointHealth.local());
        map.put("remote", endpointHealth.remote());
        map.put("state", endpointHealth.state());
        map.put("type", endpointHealth.type());
        return map;
    }
}
