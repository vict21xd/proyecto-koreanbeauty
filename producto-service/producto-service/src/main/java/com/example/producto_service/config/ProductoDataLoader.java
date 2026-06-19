package com.example.producto_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.producto_service.model.Producto;
import com.example.producto_service.repository.ProductoRepository;

@Component
public class ProductoDataLoader implements CommandLineRunner {

    private final ProductoRepository repository;

    public ProductoDataLoader(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        if (repository.count() == 0) {

            repository.save(Producto.builder()
                    .nombre("Serum Vitamina C")
                    .descripcion("Antioxidante")
                    .precio(15000.0)
                    .stock(10)
                    .categoria("Skincare") 
                    .build());

            repository.save(Producto.builder()
                    .nombre("Protector Solar")
                    .descripcion("FPS alto")
                    .precio(18000.0)
                    .stock(20)
                    .categoria("Skincare")
                    .build());
        }
    }
}