package common;

import com.SafeWebServices.PaymentGateway.PGCard;

/**
 * Created by hp on 2/17/2018.
 */

public class PgPaymentCard extends PGCard {

    private String cardName;
    private String cardNumber;
    private String expirationDate;
    private String cardStartDate;
    private String cardIssueNumber;

    public PgPaymentCard(String ccname,String ccnumber, String exp, String cvv) {
        this.cardName = ccname;
        this.cardNumber = ccnumber;
        this.expirationDate = exp;
        this.setCVV(cvv);
        this.cardStartDate = null;
        this.cardIssueNumber = null;
    }

    public PgPaymentCard(String ccnumber, String exp, String cvv, String startDate, String issueNum) {
        this.cardNumber = ccnumber;
        this.expirationDate = exp;
        this.setCVV(cvv);
        this.cardStartDate = startDate;
        this.cardIssueNumber = issueNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    public void setCardStartDate(String value) {
        this.cardStartDate = value;
    }

    public void setCardIssueNumber(String value) {
        this.cardIssueNumber = value;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public String getCardStartDate() {
        return this.cardStartDate;
    }

    public String getCardIssueNumber() {
        return this.cardIssueNumber;
    }

    public String getDirectPostString(boolean includeCVV) {
        String result = "ccnumber=" + this.cardNumber;
        result = result + "&Customer=" + this.cardName;
        result = result + "&ccexp=" + this.expirationDate;
        if(this.getCVV() != null && includeCVV) {
            result = result + "&cvv=" + this.getCVV();
        }

        if(this.cardStartDate != null) {
            result = result + "&cc_start_date=" + this.cardStartDate;
        }

        if(this.cardIssueNumber != null) {
            result = result + "&cc_issue_number=" + this.cardIssueNumber;
        }

        return result;
    }
}
