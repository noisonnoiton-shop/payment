package com.skcc.payment.repository;

import java.util.List;

import com.skcc.payment.domain.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
  public Payment findById(long id);
  public List<Payment> findPaymentByAccountId(long accountId);
  public Payment findPaymentByIdAndPaid(long id, String paid);
  public Payment findPaymentByOrderId(long orderId);
}
