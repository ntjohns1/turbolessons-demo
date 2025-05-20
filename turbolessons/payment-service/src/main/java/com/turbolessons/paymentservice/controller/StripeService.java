package com.turbolessons.paymentservice.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.StripeObjectInterface;
import com.stripe.net.*;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

public class StripeService {

    StripeResponseGetter responseGetter = new StripeResponseGetter() {
        @Override
        public <T extends StripeObjectInterface> T request(BaseAddress baseAddress, ApiResource.RequestMethod requestMethod, String s, Map<String, Object> map, Type type, RequestOptions requestOptions, ApiMode apiMode) throws StripeException {
            return null;
        }

        @Override
        public InputStream requestStream(BaseAddress baseAddress, ApiResource.RequestMethod requestMethod, String s, Map<String, Object> map, RequestOptions requestOptions, ApiMode apiMode) throws StripeException {
            return null;
        }
    };

}
