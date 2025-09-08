package org.example;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomMetrics {
    private final Counter customCounter;
    private final AtomicInteger customGauge;

    public CustomMetrics(MeterRegistry registry) {
        // Создание счетчика
        customCounter = Counter.builder("custom_counter")
                .description("A custom counter metric")
                .register(registry);

        // Создание gauge
        customGauge = registry.gauge("custom_gauge", new AtomicInteger(0));
    }

    public void incrementCounter() {
        customCounter.increment();
    }

    public void setGaugeValue(int value) {
        customGauge.set(value);
    }
}
