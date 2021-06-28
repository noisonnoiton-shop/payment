package com.skcc.payment.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long accountId;

	@Column
	private Long orderId;

	@Column(length = 255)
	private String paymentMethod;

	@Column(length = 255)
	private String paymentDetail1;

	@Column(length = 255)
	private String paymentDetail2;

	@Column(length = 255)
	private String paymentDetail3;

	@Column
	private Long price;

	@Column(length = 255)
	private String paid;

	@Column(length = 255)
	private String active;
	
	@Column
	@CreationTimestamp
	private LocalDateTime createdAt;
}
