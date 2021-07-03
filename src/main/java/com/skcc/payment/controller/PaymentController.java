package com.skcc.payment.controller;

import java.util.List;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.skcc.payment.domain.Payment;
import com.skcc.payment.event.message.PaymentEvent;
import com.skcc.payment.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@XRayEnabled
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
}
