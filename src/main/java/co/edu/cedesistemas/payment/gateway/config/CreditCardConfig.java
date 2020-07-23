package co.edu.cedesistemas.payment.gateway.config;

import lombok.Data;

@Data
public class CreditCardConfig {
    private String number;
    private Integer cvc;
    private Integer expMonth;
    private Integer expYear;
    private Status status;
    private Integer limit;
    private Integer credit;

    public enum Status {
        VALID,
        INVALID
    }

    @Override
    public String toString() {
        return "credit-card {number: " + number + ", month: " + expMonth + ", year: " + expYear + "," +
                " status: " + status + ", credit: " + credit + "}";
    }
}
