package entities;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.math.BigDecimal;
public class SmsSender {
    public static final String ACCOUNT_SID = "ACd6178ca26eb4a78ebcf30b381f0ebeab";
    public static final String AUTH_TOKEN = "9c80cfd68e4cd0692781ba469c2e819b";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+21653378560"),//receiver
                new com.twilio.type.PhoneNumber("+15415267049"),//sender
                "Bienvenue dans notre platforme Sportify!") .create();

        System.out.println(message.getSid());
    }
}
