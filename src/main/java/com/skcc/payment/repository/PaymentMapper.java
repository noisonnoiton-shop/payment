package com.skcc.payment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.skcc.payment.domain.Payment;
import com.skcc.payment.event.message.PaymentEvent;

@Mapper
public interface PaymentMapper {

//	@Select("select * from payments where accountId = #{accountId}")
	public List<Payment> findPaymentByAccountId(long accountId);
	
//	@Select("select * from payments where accountId = #{orderId}")
	public Payment findPaymentByOrderId(long orderId);
	
//	@Select("select * from payments where id = #{id}")
	public Payment findById(long id);
	
//	@Insert("insert into payments(accountId, paymentMethod, paymentDetail1, paymentDetail2, paymentDetail3, price, paid, createdAt) values(#{accountId}, #{paymentMethod}, #{paymentDetail1}, #{paymentDetail2}, #{paymentDetail3}, #{price}, #{paid}, NOW() )")
	public void createPayment(Payment payment);
	
//	@Update("update payments set paid='paid' where id = #{id}")
	public void setPaymentPaid(@Param("paid") String paid, @Param("id") long id);
	
//	@select("select * from payments where id=#{id} and paid='unpaid'")
	public Payment findunPaidPaymentById(long id);
	
//	@update("update payments set active = #{active} where orderId = #{orderId}")
	public void cancelPayment(Payment payment);
	
//	@select("select * from payment_events where paymentId = #{paymentId} and id = (select max(id) from payment_events where paymentId = #{paymentId} and id < (select min(id) from payment_events where paymentId = #{paymentId} and txId = #{txId}))")
	public PaymentEvent findPreviousPaymentEvent(@Param("txId") String txId, @Param("paymentId") long paymentId);
	
//	@select("select * from payment_events")
	public List<PaymentEvent> getPaymentEvent();
	
	public long getPaymentEventId();
	
	public void createPaymentEvent(PaymentEvent paymentEvent);

}
