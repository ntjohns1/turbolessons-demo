package com.turbolessons.paymentservice.service.setupintent;

import com.turbolessons.paymentservice.dto.SetupIntentData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeCollection;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.SetupIntentUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SetupIntentServiceImpl implements SetupIntentService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;


    public SetupIntentServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    //    List All SetupIntents
    @Override
    public Mono<StripeCollection<SetupIntent>> listSetupIntents() {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.setupIntents()
                .list());
    }

    //    Retrieve a SetupIntent
    @Override
    public Mono<SetupIntent> retrieveSetupIntent(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.setupIntents()
                .retrieve(id));
    }

    //    Create a SetupIntent
    @Override
    public Mono<SetupIntent> createSetupIntent(SetupIntentData setupIntentData) {
        SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                .setAutomaticPaymentMethods(SetupIntentCreateParams.AutomaticPaymentMethods.builder()
                                                    .setEnabled(true)
                                                    .build())
                .setCustomer(setupIntentData.getCustomer())
                .setPaymentMethod(setupIntentData.getPaymentMethod())
                .setDescription(setupIntentData.getDescription())
                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.setupIntents()
                .create(params));
    }

    //    Confirm a SetupIntent
    @Override
    public Mono<Void> confirmSetupIntent(String id) {

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.setupIntents()
                .confirm(id));
    }

    //    Update a SetupIntent
    @Override
    public Mono<Void> updateSetupIntent(String id, SetupIntentData setupIntentData) {
        SetupIntentUpdateParams params = SetupIntentUpdateParams.builder()
                .setCustomer(setupIntentData.getCustomer())
                .setPaymentMethod(setupIntentData.getPaymentMethod())
                .setDescription(setupIntentData.getDescription())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.setupIntents()
                .update(id,
                        params));
    }

    //    Cancel a SetupIntent
    @Override
    public Mono<Void> cancelSetupIntent(String id) {

    return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.setupIntents()
            .cancel(id));
    }
}
