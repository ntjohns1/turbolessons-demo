package com.turbolessons.paymentservice.service.product;

import com.turbolessons.paymentservice.dto.ProductData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public ProductServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    @Override
    public Mono<StripeCollection<Product>> listAllProducts() {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.products()
                .list());
    }

    @Override
    public Mono<Product> retrieveProduct(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.products()
                .retrieve(id));
    }

    @Override
    public Mono<Product> createProduct(ProductData productData) {

        ProductCreateParams params = ProductCreateParams.builder()
                .setName(productData.getName())
                .setDescription(productData.getDescription())
                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.products()
                .create(params));
    }

    @Override
    public Mono<Void> updateProduct(String id, ProductData productData) {

        ProductUpdateParams params = ProductUpdateParams.builder()
                .setName(productData.getName())
                .setDescription(productData.getDescription())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.products()
                .update(id, params));
    }

    @Override
    public Mono<Void> deleteProduct(String id) {

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.products()
                .delete(id));
    }

}
