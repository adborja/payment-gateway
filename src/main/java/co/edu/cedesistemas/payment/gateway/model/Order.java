package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

import java.util.Map;

@Data
public class Order {
    private String id;
    private String accountId;
    private String referenceCode;
    private String description;
    private String signature;
    private String notifyUrl;
    private Map<String, Map<String, ?>> additionalValues;
    private Person buyer;
    private Address shippingAddress;
}
