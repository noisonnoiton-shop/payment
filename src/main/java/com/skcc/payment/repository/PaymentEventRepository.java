package com.skcc.payment.repository;

import com.skcc.payment.event.message.PaymentEvent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long>{
}
