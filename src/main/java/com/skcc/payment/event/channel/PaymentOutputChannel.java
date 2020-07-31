package com.skcc.payment.event.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PaymentOutputChannel {
	String paymentOutput = "paymentOutput";
			
	@Output(PaymentOutputChannel.paymentOutput)
	MessageChannel getMessageChannel();

}
