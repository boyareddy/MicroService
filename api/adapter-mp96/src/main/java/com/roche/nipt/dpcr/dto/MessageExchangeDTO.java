package com.roche.nipt.dpcr.dto;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;

public class MessageExchangeDTO {
		
	Exchange exchange;
	
	AsyncCallback callback;

	public Exchange getExchange() {
		return exchange;
	}
	
	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
	
	public AsyncCallback getCallback() {
		return callback;
	}
	
	public void setCallback(AsyncCallback callback) {
		this.callback = callback;
	}

}
