package com.turbolessons.paymentservice.service.meter;

import com.turbolessons.paymentservice.dto.MeterData;
import com.turbolessons.paymentservice.dto.MeterEventData;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MeterService {
//    Mono<MeterEventData> createUsageRecord(MeterEventData meterEventDto);
    Mono<List<MeterData>> listAllMeters();
    Mono<MeterData> retrieveMeter(String id);
    Mono<MeterData> createMeter(MeterData meterData);
    Mono<Void> updateMeter(String id, MeterData meterData);
    Mono<MeterData> deactivateMeter(String id);
    Mono<MeterData> reactivateMeter(String id);
    Mono<MeterEventData> createMeterEvent(MeterEventData meterEventData);

}
