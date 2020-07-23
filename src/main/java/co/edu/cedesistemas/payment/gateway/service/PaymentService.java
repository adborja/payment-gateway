package co.edu.cedesistemas.payment.gateway.service;

import co.edu.cedesistemas.payment.gateway.config.CreditCardConfig;
import co.edu.cedesistemas.payment.gateway.model.CreditCard;
import co.edu.cedesistemas.payment.gateway.model.Order;
import co.edu.cedesistemas.payment.gateway.model.TransactionReq;
import co.edu.cedesistemas.payment.gateway.model.TransactionRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private final Map<String, CreditCardConfig> cards;

    public TransactionRes pay(TransactionReq tx) {
        CreditCard card = tx.getCreditCard();
        Order order = tx.getOrder();

        Integer value = (Integer) order.getAdditionalValues().get("TX_VALUE").get("value");

        TransactionRes res = new TransactionRes();
        res.setTransactionId(UUID.randomUUID().toString());
        res.setOrderId(tx.getOrder().getId());
        res.setTransactionDate(LocalDateTime.now());

        try {
            pay(card, value);
            res.setAuthorizationCode(RandomStringUtils.randomNumeric(5));
            res.setResponseMessage("success");
            res.setResponseCode("0");
            res.setState(TransactionRes.State.APPROVED);
            res.setTraceabilityCode(RandomStringUtils.randomAlphabetic(8));

        } catch (Exception ex) {
            res.setResponseMessage(ex.getMessage());
            res.setResponseCode("1");
            res.setState(TransactionRes.State.DECLINED);
            res.setErrorCode("" + ex.getMessage().hashCode());
        }
        return res;

    }

    private void pay(CreditCard card, Integer value) throws Exception {
        validateCard(card);
        validateFunds(card, value);
        CreditCardConfig cardConfig = cards.get(card.getNumber());
        log.info("current credit for card {}: {}", cardConfig.getNumber(), cardConfig.getCredit());
        cardConfig.setCredit(cardConfig.getCredit() - value);
        log.info("new credit for card {}: {}", cardConfig.getNumber(), cardConfig.getCredit());
    }

    private void validateCard(CreditCard card) throws Exception {
        String number = card.getNumber();
        CreditCardConfig cardConfig = cards.get(number);
        if (cardConfig == null) {
            throw new Exception("card not found");
        }

        if (cardConfig.getStatus().equals(CreditCardConfig.Status.INVALID)) {
            throw new Exception("invalid card");
        }

        Integer cvc = card.getSecurityCode();
        if (!cvc.equals(cardConfig.getCvc())) {
            throw new Exception("invalid security code");
        }

        Integer expMonth = card.getExpirationMonth();
        if (!expMonth.equals(cardConfig.getExpMonth())) {
            throw new Exception("invalid expiration date");
        }

        Integer expYear = card.getExpirationYear();
        if (!expYear.equals(cardConfig.getExpYear())) {
            throw new Exception("invalid expiration date");
        }
    }

    private void validateFunds(CreditCard card, Integer value) throws Exception {
        CreditCardConfig cardConfig = cards.get(card.getNumber());

        Integer credit = cardConfig.getCredit();
        if (credit - value < 0) {
            throw new Exception("no funds");
        }
    }
}