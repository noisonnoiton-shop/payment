package com.skcc.payment.producer;

import com.skcc.payment.event.message.PaymentEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentProducer {

  private final StreamBridge streamBridge;

  @Value("${domain.name}")
  private String domain;

  public boolean send(PaymentEvent paymentEvent) {
    log.info("routeTo" + domain + "." + paymentEvent.getEventType());

    return this.streamBridge.send("paymentOutput", MessageBuilder.withPayload(paymentEvent)
    .setHeader("routeTo", domain + "." + paymentEvent.getEventType()).build());
  }
}