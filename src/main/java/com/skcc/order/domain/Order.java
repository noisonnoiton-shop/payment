package com.skcc.order.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
	
	private long id;
	private long accountId;
	private long paymentId;
	private OrderAccount accountInfo;
	private List<OrderProduct> productsInfo;
	private OrderPayment paymentInfo;
	private String paid;
	private String status;
	private String createdAt;
}
