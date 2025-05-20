package com.turbolessons.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
@Slf4j
@Component
public class MsgCreatedEventPublisher implements
        ApplicationListener<MsgCreatedEvent>,
        Consumer<FluxSink<MsgCreatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<MsgCreatedEvent> queue =
            new LinkedBlockingQueue<>();

    protected MsgCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void onApplicationEvent(MsgCreatedEvent event) {
        log.info("Received event: " + event);
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<MsgCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    MsgCreatedEvent event = queue.take();
                    log.info("Consuming event: " + event);
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}