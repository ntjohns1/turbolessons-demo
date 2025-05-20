package com.turbolessons.paymentservice.service.product;

import com.turbolessons.paymentservice.dto.ProductData;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<StripeCollection<Product>> listAllProducts();

    Mono<Product> retrieveProduct(String id);

    Mono<Product> createProduct(ProductData productData);

    Mono<Void> updateProduct(String id, ProductData productData);

    Mono<Void> deleteProduct(String id);
}
