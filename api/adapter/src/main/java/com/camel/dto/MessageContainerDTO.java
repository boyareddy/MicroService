package com.camel.dto;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;

public class MessageContainerDTO {
	
	HL7HeaderSegmentDTO headerSegment;
	Exchange exchange;
	AsyncCallback callback;
	public HL7HeaderSegmentDTO getHeaderSegment() {
		return headerSegment;
	}
	public void setHeaderSegment(HL7HeaderSegmentDTO headerSegment) {
		this.headerSegment = headerSegment;
	}
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
