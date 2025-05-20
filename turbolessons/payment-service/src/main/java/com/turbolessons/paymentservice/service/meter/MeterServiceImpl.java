package com.turbolessons.paymentservice.service.meter;

import com.stripe.StripeClient;
import com.stripe.model.billing.Meter;
import com.stripe.model.billing.MeterEvent;
import com.stripe.param.billing.MeterCreateParams;
import com.stripe.param.billing.MeterEventCreateParams;
import com.stripe.param.billing.MeterUpdateParams;
import com.turbolessons.paymentservice.dto.MeterData;
import com.turbolessons.paymentservice.dto.MeterEventData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class MeterServiceImpl implements MeterService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;

    public MeterServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private MeterEventData mapMeterEventToDto(MeterEvent meterEvent) {
        return new MeterEventData(meterEvent.getIdentifier(),
                                  meterEvent.getEventName(),
                                  meterEvent.getPayload()
                                          .get("stripe_customer_id"),
                                  meterEvent.getPayload()
                                          .get("value"));
    }

    private MeterData mapMeterToDto(Meter meter) {
        return new MeterData(meter.getId(),
                             meter.getDisplayName(),
                             meter.getEventName());
    }

    @Override
    public Mono<List<MeterData>> listAllMeters() {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .list())
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapMeterToDto)
                        .toList());
    }

    @Override
    public Mono<MeterData> retrieveMeter(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .retrieve(id))
                .map(this::mapMeterToDto);
    }

    @Override
    public Mono<MeterData> createMeter(MeterData meterData) {
        MeterCreateParams params = MeterCreateParams.builder()
                .setDisplayName(meterData.display_name())
                .setEventName(meterData.event_name())
                .setDefaultAggregation(MeterCreateParams.DefaultAggregation.builder()
                                               .setFormula(MeterCreateParams.DefaultAggregation.Formula.COUNT)
                                               .build())
                .setValueSettings(MeterCreateParams.ValueSettings.builder()
                                          .setEventPayloadKey("value")
                                          .build())
                .setCustomerMapping(MeterCreateParams.CustomerMapping.builder()
                                            .setType(MeterCreateParams.CustomerMapping.Type.BY_ID)
                                            .setEventPayloadKey("stripe_customer_id")
                                            .build())
                .build();
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .create(params))
                .map(this::mapMeterToDto);
    }

    @Override
    public Mono<Void> updateMeter(String id, MeterData meterData) {
        MeterUpdateParams params = MeterUpdateParams.builder()
                .setDisplayName(meterData.display_name())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.billing()
                .meters()
                .update(id,
                        params));
    }

    @Override
    public Mono<MeterData> deactivateMeter(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .reactivate(id))
                .map(this::mapMeterToDto);
    }

    @Override
    public Mono<MeterData> reactivateMeter(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meters()
                        .reactivate(id))
                .map(this::mapMeterToDto);
    }

    //    *** Create Meter Event ***
    @Override
    public Mono<MeterEventData> createMeterEvent(MeterEventData meterEventData) {
        MeterEventCreateParams params = MeterEventCreateParams.builder()
                .setEventName(meterEventData.eventName())
                .putPayload("value",
                            meterEventData.value())
                .putPayload("stripe_customer_id",
                            meterEventData.stripeCustomerId())
                .setIdentifier(meterEventData.identifier())
                .build();
        System.out.println("Meter service params:" + params);
        return stripeClientHelper.executeStripeCall(() -> stripeClient.billing()
                        .meterEvents()
                        .create(params))
                .map(this::mapMeterEventToDto);
    }
}
