package bean;

/**
 * Created by hp on 12/13/2017.
 */

public class TransferBalanceBean {

    private String transaction_id,transaction_time,transaction_amount;

    public TransferBalanceBean(String transaction_id, String transaction_time, String transaction_amount) {
        this.transaction_id = transaction_id;
        this.transaction_time = transaction_time;
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }
}
