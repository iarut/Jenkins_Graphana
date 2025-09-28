package org.example.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Product entity")
public class Product implements AutoCloseable {
    @Schema(description = "Unique identifier of the product", example = "1")
    @Positive(message = "Price must be greater than 0")
    private int id;

    @Schema(description = "Name of the product", example = "Laptop")
    @BadWordsCheck
    private String name;

    @Schema(description = "Available quantity of the product", example = "50")
    @Min(0)
    @InjectRandomInt(min=1000, max=10000)
    private int quantity;

    @Schema(description = "Price of the product", example = "999.99")
    @Min(1)
    @Positive(message = "Price must be greater than 0")
    private double price;

    @Override
    public void close() throws Exception {

    }

}