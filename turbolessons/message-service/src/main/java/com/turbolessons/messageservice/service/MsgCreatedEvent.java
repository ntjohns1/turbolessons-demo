package com.turbolessons.messageservice.service;

import com.turbolessons.messageservice.model.Msg;
import org.springframework.context.ApplicationEvent;

public class MsgCreatedEvent extends ApplicationEvent {
    public MsgCreatedEvent(Msg source) {
        super(source);
    }

    @Override
    public Msg getSource() {
        return (Msg)super.getSource();
    }

}