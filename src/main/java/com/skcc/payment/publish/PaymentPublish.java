package com.skcc.payment.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.skcc.payment.event.channel.PaymentOutputChannel;
import com.skcc.payment.event.message.PaymentEvent;

@Component
@EnableBinding(PaymentOutputChannel.class)
public class PaymentPublish {
	
	private PaymentOutputChannel paymentOutputChannel;
	
	@Value("${domain.payment.name}")
	private String domain;
	
	@Autowired
	public PaymentPublish(PaymentOutputChannel paymentOutputChannel) {
		this.paymentOutputChannel = paymentOutputChannel;
	}
	
	public void send(PaymentEvent paymentEvent) {
		this.paymentOutputChannel.getMessageChannel().send(
		 MessageBuilder.withPayload(paymentEvent).setHeader(
		 "routeTo", domain + "." + paymentEvent.getEventType()).build());
	}
	
}
