package com.turbolessons.paymentservice.controller.product;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.ProductData;
import com.turbolessons.paymentservice.service.product.ProductService;
import com.stripe.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductHandlerImpl extends BaseHandler implements ProductHandler {

    private final ProductService productService;

    public ProductHandlerImpl(ProductService productService) {
        this.productService = productService;
    }

    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                          request -> this.productService.listAllProducts(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> this.productService.retrieveProduct(id(request)),
                              Product.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(productService::createProduct),
                            ProductData.class,
                            Product.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> this.productService.updateProduct(idParam,
                                                                                                                   dto)),
                            id,
                            ProductData.class);
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest r) {
        return handleDelete(r,
                            request -> this.productService.deleteProduct(id(request)));
    }
}
