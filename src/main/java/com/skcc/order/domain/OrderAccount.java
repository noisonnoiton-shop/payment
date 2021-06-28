package com.skcc.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderAccount {
	
	private long id;
	private String username;
	private String name;
	private String mobile;
	private String scope;
	private String address;
}
