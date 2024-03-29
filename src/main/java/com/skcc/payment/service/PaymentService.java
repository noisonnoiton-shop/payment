package com.skcc.payment.service;

import java.time.LocalDateTime;
import java.util.List;

// import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.skcc.order.event.message.OrderEvent;
import com.skcc.payment.domain.Payment;
import com.skcc.payment.event.message.PaymentEvent;
import com.skcc.payment.event.message.PaymentEventType;
import com.skcc.payment.event.message.PaymentPayload;
import com.skcc.payment.producer.PaymentProducer;
import com.skcc.payment.publish.PaymentPublish;
import com.skcc.payment.repository.PaymentEventRepository;
import com.skcc.payment.repository.PaymentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
// @XRayEnabled
@Slf4j
public class PaymentService {

	// @Autowired
	// private PaymentMapper paymentMapper;

	private PaymentRepository paymentRepository;
	private PaymentEventRepository paymentEventRepository;
	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentProducer paymentProducer;
	
	
	@Value("${domain.name}")
	String domain;
	
	@Autowired
	public PaymentService(PaymentRepository paymentRepository, PaymentEventRepository paymentEventRepository) {
		this.paymentRepository = paymentRepository;
		this.paymentEventRepository = paymentEventRepository;
	}
	
	public boolean createPaymentAndCreatePublishEvent(OrderEvent orderEvent) {
        boolean result = false;
        Payment payment = this.convertOrderEventToPayment(orderEvent);
        payment.setPaid("unpaid");
        try {
            this.paymentService.createPaymentAndCreatePublishPaymentCreatedEvent(orderEvent.getTxId(), payment);
            result = true;
        } catch(Exception e) {
            try {
                result = false;
                e.printStackTrace();
                this.paymentService.createPublishPaymentCreateFailedEvent(orderEvent.getTxId(), payment);
            }catch(Exception e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
	
    @Transactional
    public void createPaymentAndCreatePublishPaymentCreatedEvent(String txId, Payment payment) throws Exception {
        this.createPaymentValidationCheck(payment);
        Payment resultPayment = this.createPayment(payment);
        this.createPublishPaymentEvent(txId, resultPayment, PaymentEventType.PaymentCreated);
    }
	
    @Transactional
    public void createPublishPaymentCreateFailedEvent(String txId, Payment payment) throws Exception{
        this.createPublishPaymentEvent(txId, payment, PaymentEventType.PaymentCreateFailed);
    }

    public boolean cancelPaymentAndCreatePublishEvent(OrderEvent orderEvent) {
        boolean result = false;
        String txId = orderEvent.getTxId();
        Payment payment = this.findPaymentByOrderId(orderEvent.getOrderId());
        if(payment == null)
            return result;
        try {
            this.paymentService.cancelPaymentAndCreatePublishPaymentCanceledEvent(txId, payment);
            result = true;
        } catch(Exception e) {
            try {
                result = false;
                e.printStackTrace();
                this.paymentService.createPublishPaymentCancelFailedEvent(txId, payment);
            }catch(Exception e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
	
    @Transactional
    public void cancelPaymentAndCreatePublishPaymentCanceledEvent(String txId, Payment payment) throws Exception{
        this.cancelPaymentValidationCheck(payment);
        this.cancelPayment(payment);
        this.createPublishPaymentEvent(txId, payment, PaymentEventType.PaymentCanceled);
    }
	
    @Transactional
    public void createPublishPaymentCancelFailedEvent(String txId, Payment payment) throws Exception{
        this.createPublishPaymentEvent(txId, payment, PaymentEventType.PaymentCancelFailed);
    }
	
	public void cancelPayment(Payment payment) {
		// this.paymentMapper.cancelPayment(payment);
		payment.setActive("inactive");
		this.paymentRepository.save(payment);
	}
	
	public void cancelPaymentValidationCheck(Payment payment) throws Exception {
		if(payment == null)
			throw new Exception();
		if("paid".equals(payment.getPaid()))
			throw new Exception();
	}

	public Payment createPayment(Payment payment) {
		// this.paymentMapper.createPayment(payment);
		payment = this.paymentRepository.save(payment);
		return payment;
	}
	
	public void createPublishPaymentEvent(String txId, Payment payment, PaymentEventType paymentEventType) {
		PaymentEvent paymentEvent = this.paymentService.convertPaymentToPaymentEvent(txId, payment, paymentEventType);
		this.createPaymentEvent(paymentEvent);
		this.publishPaymentEvent(paymentEvent);
	}
	
	public void createPaymentEvent(PaymentEvent paymentEvent) {
		// this.paymentMapper.createPaymentEvent(paymentEvent);
		this.paymentEventRepository.save(paymentEvent);
	}
	
	public void publishPaymentEvent(PaymentEvent paymentEvent) {
		// this.paymentPublish.send(paymentEvent);
		this.paymentProducer.send(paymentEvent);
	}
	
	public Payment findById(long id) {
		// return this.paymentMapper.findById(id);
		return this.paymentRepository.findById(id);
	}
	
	public List<Payment> findPaymentByAccountId(long accountId) {
		// return this.paymentMapper.findPaymentByAccountId(accountId);
		return this.paymentRepository.findPaymentByAccountId(accountId);
	}
	
	public Payment convertOrderEventToPayment(OrderEvent orderEvent) {
		Payment payment = new Payment();
		
		payment.setId(orderEvent.getPayload().getPaymentId());
		payment.setAccountId(orderEvent.getPayload().getPaymentInfo().getAccountId());
		payment.setOrderId(orderEvent.getPayload().getPaymentInfo().getOrderId());
		payment.setPaymentMethod(orderEvent.getPayload().getPaymentInfo().getPaymentMethod());
		payment.setPaymentDetail1(orderEvent.getPayload().getPaymentInfo().getPaymentDetail1());
		payment.setPaymentDetail2(orderEvent.getPayload().getPaymentInfo().getPaymentDetail2());
		payment.setPaymentDetail3(orderEvent.getPayload().getPaymentInfo().getPaymentDetail3());
		payment.setPrice(orderEvent.getPayload().getPaymentInfo().getPrice());
		payment.setPaid(orderEvent.getPayload().getPaymentInfo().getPaid());
		payment.setActive(orderEvent.getPayload().getPaymentInfo().getActive());
		
		return payment;
	}
	
	public Payment convertPaymentEventToPayment(PaymentEvent paymentEvent) {
		Payment payment = new Payment();
		
		payment.setId(paymentEvent.getPayload().getId());
		payment.setAccountId(paymentEvent.getPayload().getAccountId());
		payment.setOrderId(paymentEvent.getPayload().getOrderId());
		payment.setPaymentMethod(paymentEvent.getPayload().getPaymentMethod());
		payment.setPaymentDetail1(paymentEvent.getPayload().getPaymentDetail1());
		payment.setPaymentDetail2(paymentEvent.getPayload().getPaymentDetail2());
		payment.setPaymentDetail3(paymentEvent.getPayload().getPaymentDetail3());
		payment.setPrice(paymentEvent.getPayload().getPrice());
		payment.setPaid(paymentEvent.getPayload().getPaid());
		payment.setActive(paymentEvent.getPayload().getActive());
		
		return payment;
	}
	
	public PaymentEvent convertPaymentToPaymentEvent(String txId, Payment payment, PaymentEventType paymentEventType) {
		log.info("in service txId : {}", txId);
		
		long id = payment.getId();
		
		if(id != 0)
			payment = this.findById(id);
		
		PaymentEvent paymentEvent = new PaymentEvent();
		// paymentEvent.setId(this.getPaymentEventId());
		paymentEvent.setPaymentId(id);
		paymentEvent.setDomain(domain);
		paymentEvent.setEventType(paymentEventType);
		paymentEvent.setPayload(new PaymentPayload(payment.getId(), payment.getAccountId(), payment.getOrderId(), payment.getPaymentMethod(), payment.getPaymentDetail1(), payment.getPaymentDetail2(), payment.getPaymentDetail3(), payment.getPrice(), payment.getPaid(), payment.getActive()));
		paymentEvent.setTxId(txId);
		paymentEvent.setCreatedAt(LocalDateTime.now());
		
		log.info("in service paymentEvent : {}", paymentEvent.toString());
		
		return paymentEvent;
	}
	
	public void createPaymentValidationCheck(Payment payment) throws Exception {
		if(payment.getPrice() == 0)
			throw new Exception();
		if(payment.getPaymentMethod().isEmpty())
			throw new Exception();
		if(payment.getAccountId() == 0)
			throw new Exception();
	}
	
	public Payment findUnpaidPaymentById(long id) {
		// return this.paymentMapper.findunPaidPaymentById(id);
		return this.paymentRepository.findPaymentByIdAndPaid(id, "unpaid");
	}
	
	public Payment findPaymentByOrderId(long orderId) {
		// return this.paymentMapper.findPaymentByOrderId(orderId);
		return this.paymentRepository.findPaymentByOrderId(orderId);
	}
	
	// public PaymentEvent findPreviousPaymentEvent(String txId, long paymentId) {
	// 	return this.paymentMapper.findPreviousPaymentEvent(txId, paymentId);
	// }
	
	public List<PaymentEvent> getPaymentEvent() {
		// return this.paymentMapper.getPaymentEvent();
		return this.paymentEventRepository.findAll();
	}
	
}
