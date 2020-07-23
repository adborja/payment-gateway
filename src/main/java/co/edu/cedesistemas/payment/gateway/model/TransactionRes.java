package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRes {
    private String transactionId;
    private String orderId;
    private State state;
    private String paymentNetworkResponseCode;
    private String paymentNetworkResponseErrorMessage;
    private String traceabilityCode;
    private String authorizationCode;
    private String pendingReason;
    private String responseCode;
    private String errorCode;
    private String responseMessage;
    private LocalDateTime transactionDate;

    public enum  State {
        APPROVED,
        DECLINED,
        PENDING
    }
}
