package org.example.controller;

import org.example.model.Product;
import org.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService service;

    private final Counter requestCounter;
    private final Timer processingTimer;
    private final DistributionSummary responseSizeSummary;
    private final Queue<Product> processingQueue;
    private final AtomicInteger activeRequests;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService service, MeterRegistry registry) {
        this.service = service;

        // Инициализация счетчика запросов
        this.requestCounter = Counter.builder("api.requests")
                .description("Count of API requests")
                .tag("endpoint", "metrics")
                .register(registry);

        // Инициализация таймера обработки
        this.processingTimer = Timer.builder("api.processing.time")
                .description("Time taken to process API requests")
                .register(registry);

        // Инициализация распределения размеров ответов
        this.responseSizeSummary = DistributionSummary.builder("api.response.size")
                .description("Size of API responses in bytes")
                .baseUnit("bytes")
                .register(registry);

        // Инициализация очереди и gauge для мониторинга ее размера
        this.processingQueue = new ConcurrentLinkedQueue<>();
        Gauge.builder("api.queue.size", processingQueue, Queue::size)
                .description("Current size of processing queue")
                .register(registry);

        // Инициализация счетчика активных запросов
        this.activeRequests = new AtomicInteger(0);
        Gauge.builder("api.active.requests", activeRequests, AtomicInteger::get)
                .description("Number of currently active requests")
                .register(registry);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        logger.debug("Adding product {}", product);
        AtomicReference<Product> productResponse = new AtomicReference<>();;
        activeRequests.incrementAndGet();
        requestCounter.increment();
        processingQueue.add(product);
        String result = processingTimer.record(() -> {
            // Симуляция обработки
            try {
                Thread.sleep(100); // Задержка для имитации времени обработки
                productResponse.set(service.saveProduct(product));
                logger.debug("Saved product {}", product);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                productResponse.set(null);
                logger.error("Error while saving product {}", product, e);
            }
            logger.debug("Saved product {}", product);
            return "Processed";
        });
        responseSizeSummary.record(result.getBytes(StandardCharsets.UTF_8).length);
        activeRequests.decrementAndGet();
//        Product returnProduct = productResponse.get();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        activeRequests.incrementAndGet();
        requestCounter.increment();

        String result = processingTimer.record(() -> {
            // Симуляция обработки
            try {
                Thread.sleep(100);
                // Задержка для имитации времени обработки
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Processed";
        });
        responseSizeSummary.record(result.getBytes(StandardCharsets.UTF_8).length);
        activeRequests.decrementAndGet();
        logger.debug("Returning list of products {}", service.getProducts());
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getProducts());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findProductById(@PathVariable int id) {
        activeRequests.incrementAndGet();
        requestCounter.increment();

        String result = processingTimer.record(() -> {
            // Симуляция обработки
            try {
                Thread.sleep(100); // Задержка для имитации времени обработки
                logger.debug("Returning product by id {}", id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("No such product {}", id);
            }
            return "Processed";
        });
        responseSizeSummary.record(result.getBytes(StandardCharsets.UTF_8).length);
        activeRequests.decrementAndGet();
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getProductById(id));
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        AtomicReference<Product> productResponse = new AtomicReference<>();;
        activeRequests.incrementAndGet();
        requestCounter.increment();
        processingQueue.add(product);
        String result = processingTimer.record(() -> {
            // Симуляция обработки
            try {
                Thread.sleep(100); // Задержка для имитации времени обработки
                productResponse.set(service.saveProduct(product));
                logger.debug("Saved product {}", product);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error while saving product {}", product, e);
                productResponse.set(null);
            }
            return "Processed";
        });
        responseSizeSummary.record(result.getBytes(StandardCharsets.UTF_8).length);
        activeRequests.decrementAndGet();
        return ResponseEntity.status(HttpStatus.FOUND).body(service.updateProduct(product));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        AtomicReference<Product> productResponse = new AtomicReference<>();;
        activeRequests.incrementAndGet();
        requestCounter.increment();
//        processingQueue.add(product);
        String result = processingTimer.record(() -> {
            // Симуляция обработки
            try {
                Thread.sleep(100); // Задержка для имитации времени обработки
//                productResponse.set(service.saveProduct(product));
                logger.debug("Deleted successfully {}", id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error while deleting product {}", id, e);
                productResponse.set(null);
            }
            return "Processed";
        });
        responseSizeSummary.record(result.getBytes(StandardCharsets.UTF_8).length);
        activeRequests.decrementAndGet();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/queue/stats")
    public ResponseEntity<QueueStats> getQueueStats() {
        QueueStats stats = new QueueStats();
        stats.setQueueSize(processingQueue.size());
        stats.setActiveRequests(activeRequests.get());

        // Также можно записать эти данные как метрики
        responseSizeSummary.record(stats.toString().getBytes().length);

        return ResponseEntity.ok(stats);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/byname", produces = "application/json")
    public ResponseEntity<Map<String, List<Product>>> getProductsByName() {
        logger.debug("Getting products by name");
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getProductsByName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/byprice", produces = "application/json")
    public ResponseEntity<Map<Double, List<Product>>> getProductsByPrice() {
        logger.debug("Getting products by price");
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getProductByPrice());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/byquantity", produces = "application/json")
    public ResponseEntity<Map<Integer, List<Product>>> getProductsByQuantity() {
        logger.debug("Getting products by quantity");
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getProductsByQuantity());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/byid", produces = "application/json")
    public ResponseEntity<Map<Integer, List<Product>>> getProductsById() {
        logger.debug("Getting products by id");
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getProductsById());
    }


    // Вспомогательный класс для статистики
    static class QueueStats {
        private int queueSize;
        private int activeRequests;

        // Геттеры и сеттеры
        public int getQueueSize() { return queueSize; }
        public void setQueueSize(int queueSize) { this.queueSize = queueSize; }
        public int getActiveRequests() { return activeRequests; }
        public void setActiveRequests(int activeRequests) { this.activeRequests = activeRequests; }

        @Override
        public String toString() {
            return String.format("QueueSize: %d, ActiveRequests: %d", queueSize, activeRequests);
        }
    }
}
