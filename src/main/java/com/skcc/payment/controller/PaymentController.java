package com.skcc.payment.controller;

import java.util.List;

import com.skcc.order.event.message.OrderEvent;
import com.skcc.payment.domain.Payment;
import com.skcc.payment.event.message.PaymentEvent;
import com.skcc.payment.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @XRayEnabled
@RequestMapping("/v1")
public class PaymentController {
	
	private PaymentService paymentService;
	
	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@GetMapping(value="/payments/{accountId}")
	public List<Payment> findPaymentByAccountID(@PathVariable long accountId) {
		return paymentService.findPaymentByAccountId(accountId);
	}
	
	@GetMapping(value="/payments/events")
	public List<PaymentEvent> getPaymentEvent(){
		return this.paymentService.getPaymentEvent();
	}

	@PostMapping(value="/payments")
	public boolean receiveOrderCreatedEvent(@RequestBody OrderEvent orderEvent) {
		return this.paymentService.createPaymentAndCreatePublishEvent(orderEvent);
	}

	@PostMapping(value="/payments/cancel")
	public boolean receiveOrderCanceledEvent(@RequestBody OrderEvent orderEvent) {
		return this.paymentService.cancelPaymentAndCreatePublishEvent(orderEvent);
	}

}
