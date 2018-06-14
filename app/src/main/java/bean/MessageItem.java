package bean;

/**
 * Created by hp on 6/22/2017.
 */

public class MessageItem
{
    String msg_date;
    String msg_title;
    String msg_description;
    String message_status;
    String notification_id;
    String offers_type;
    String messageimg_url;
    String offers_title;
    String offers_description;
    String nombre;
    String expire_date;
    String expiretime;
    String offer_image_detail;
    String offer_code;
    String offer_qr_code;


    public MessageItem(String msg_date, String msg_title, String msg_description, String message_status, String notification_id, String offers_type, String messageimg_url, String offers_title, String offers_description, String nombre, String expire_date, String expiretime,
                       String offer_image_detail,String offer_code,String offer_qr_code) {
        this.msg_date = msg_date;
        this.msg_title = msg_title;
        this.msg_description = msg_description;
        this.message_status = message_status;
        this.notification_id = notification_id;
        this.offers_type = offers_type;
        this.messageimg_url = messageimg_url;
        this.offers_title = offers_title;
        this.offers_description = offers_description;
        this.nombre = nombre;
        this.expire_date = expire_date;
        this.expiretime = expiretime;
        this.offer_image_detail=offer_image_detail;
        this.offer_code=offer_code;
        this.offer_qr_code=offer_qr_code;
    }

    public String getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(String msg_date) {
        this.msg_date = msg_date;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getMsg_description() {
        return msg_description;
    }

    public void setMsg_description(String msg_description) {
        this.msg_description = msg_description;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getOffers_type() {
        return offers_type;
    }

    public void setOffers_type(String offers_type) {
        this.offers_type = offers_type;
    }

    public String getMessageimg_url() {
        return messageimg_url;
    }

    public void setMessageimg_url(String messageimg_url) {
        this.messageimg_url = messageimg_url;
    }

    public String getOffers_title() {
        return offers_title;
    }

    public void setOffers_title(String offers_title) {
        this.offers_title = offers_title;
    }

    public String getOffers_description() {
        return offers_description;
    }

    public void setOffers_description(String offers_description) {
        this.offers_description = offers_description;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getOffer_image_detail() {
        return offer_image_detail;
    }

    public void setOffer_image_detail(String offer_image_detail) {
        this.offer_image_detail = offer_image_detail;
    }

    public String getOffer_code() {
        return offer_code;
    }

    public void setOffer_code(String offer_code) {
        this.offer_code = offer_code;
    }

    public String getOffer_qr_code() {
        return offer_qr_code;
    }

    public void setOffer_qr_code(String offer_qr_code) {
        this.offer_qr_code = offer_qr_code;
    }
}
