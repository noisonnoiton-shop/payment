package com.skcc.order.event.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderEvent {
	
	private long id;
	private String domain;
	private long orderId;
	private OrderEventType eventType;
	private OrderPayload payload;
	private String txId;
	private String createdAt;
}
