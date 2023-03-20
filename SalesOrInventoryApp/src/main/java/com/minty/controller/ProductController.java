package com.minty.controller;

import com.minty.entity.Product;
import com.minty.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    ProductService productService ;
    @Value("${api.version}")
    private String apiVersion ;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(value = "pageNumber",required = false,defaultValue = "10") int pageNumber,
                                                        @RequestParam(value = "pageSize",required = false,defaultValue = "0") int pageSize){
        return ResponseEntity.ok(productService.getAllProduct(pageNumber,pageSize)) ;
    }
    @GetMapping("/getproducts")  // second version to 1 above
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable){
      return ResponseEntity.ok(productService.getAllProduct(pageable)) ;
    }
    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Creating new product with name: {}, quantity: {}", product.getProductname(), product.getNoOfItemInStock());
        // Create the new product
        Product newProduct = productService.save(product);
        try {
            // Build a created response
            return ResponseEntity
                    .created(new URI("/product/" + newProduct.getId()))
                    .eTag(apiVersion)
                    .body(newProduct);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        return productService.findById(id)
                .map(product -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(apiVersion)
                                .location(new URI("/product/" + product.getId()))
                                .body(product);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product,
                                           @PathVariable Integer id) {
        log.info("Updating product with id: {}, name: {}, quantity: {}",
                id, product.getProductname(), product.getNoOfItemInStock());
        // Get the existing product
        Optional<Product> existingProduct = productService.findById(id);
        return existingProduct.map(p -> {
            // Compare the etags
            log.info("Product with ID: " + id + " has a version of " + apiVersion);
            // Update the product
            p.setProductname(product.getProductname());
            p.setNoOfItemInStock(product.getNoOfItemInStock());
            p.setProductprice(product.getProductprice());  // price update  or change here
            p.setProductdescription(product.getProductdescription());
            log.info("Updating product with ID: " + p.getId()
                    + " -> name=" + p.getProductname()
                    + ", quantity=" + p.getNoOfItemInStock()) ;
            try {
                // Update the product and return an ok response
                if (productService.update(p)) {
                    return ResponseEntity.ok()
                            .location(new URI("/product/" + p.getId()))
                            .eTag(apiVersion)
                            .body(p);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (URISyntaxException e) {
                // An error occurred trying to create the location URI, return an error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        log.info("Deleting product with ID {}", id);
        // Get the existing product
        Optional<Product> existingProduct = productService.findById(id);
        return existingProduct.map(p -> {
            if (productService.delete(p.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}




















































































































