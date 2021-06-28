package com.skcc.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderPayment {
	
	private long id;
	private long accountId;
	private long orderId;
	private String paymentMethod;
	private String paymentDetail1;
	private String paymentDetail2;
	private String paymentDetail3;
	private long price;
	private String paid;
	private String active;
}
