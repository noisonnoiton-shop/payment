package com.skcc.payment.subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.skcc.order.event.message.OrderEvent;
import com.skcc.payment.event.channel.PaymentInputChannel;
import com.skcc.payment.service.PaymentService;

@Component
@EnableBinding(PaymentInputChannel.class)
public class PaymentSubscribe {

	private PaymentService paymentService;
	
	@Autowired
	public PaymentSubscribe(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@StreamListener(PaymentInputChannel.orderCreated)
	public void receiveOrderCreatedEvent(OrderEvent orderEvent) {
	    this.paymentService.createPaymentAndCreatePublishEvent(orderEvent);
	}
	
	@StreamListener(PaymentInputChannel.orderCanceled)
	public void receiveOrderCanceledEvent(OrderEvent orderEvent) {
	    this.paymentService.cancelPaymentAndCreatePublishEvent(orderEvent);
	}
	
}
