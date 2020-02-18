package com.ruda.coco.rest.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class RecuperarRequest implements Processor{
	 public void process(Exchange exchange) throws Exception {
		 String body = exchange.getIn().getBody(String.class);
		    exchange.getOut().setBody("Resultado" + body);
	
	 }
}
 