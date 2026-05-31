package app.services;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.responseDTO.*;
import app.services.utils.GmailEmailSender;
import app.services.utils.GmailEmailSenderHTML;
import jakarta.mail.MessagingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailService {
    private GmailEmailSender gmailEmailSender;
    private GmailEmailSenderHTML gmailEmailSenderHTML;

    public MailService(GmailEmailSender gmailEmailSender, GmailEmailSenderHTML gmailEmailSenderHTML){
        this.gmailEmailSender = gmailEmailSender;
        this.gmailEmailSenderHTML = gmailEmailSenderHTML;
    }

    public void sendInquiryNotice(InquiryRequestDTO inquiryRequestDTO) throws MessagingException {

    }


    public void sendQuoteNotice(InquiryResponseDTO inquiryResponseDTO) throws MessagingException {
        //Get relevant information to send email
        CustomerResponseDTO customer = inquiryResponseDTO.getCustomerResponseDTO();
        String receiver = customer.getEmail();
        String subject = "Tilbud modtaget med tilbudsnummer: " + inquiryResponseDTO.getInquiryId() + " på en carport med egne mål";
        String body = "Goddag " + customer.getFirstName() + ", \n" +
                "Tak for en god snak, vi har nu sendt dig et tilbud på en carport.\nDer vil selvfølgelig være" +
                " de nye tilføjelser med, hvis det blev aftalt, ellers er den som du selv har lavet.\n\n" +
                "For at se og betale for ordren, skal du logge ind på vores hjemmeside, logge ind med samme mail som\n" +
                " du har modtaget denne mail på. Derefter skal du åbne fanen 'Min Side' og til sidst under 'Tilbud' fanen " +
                "åbne tilbuddet med nummeret: \n\n" + inquiryResponseDTO.getInquiryId() +
                "\n\n Vi håber alt er som det skal være, " +
                "ellers er du altid velkommen til at kontakte os på: " + System.getenv("MAIL_USERNAME") + " eller " +
                "ringe til os på: 12345678";


        gmailEmailSender.sendPlainTextEmail(receiver, subject, body);

    }

    public void sendOrderConfirmation(OrderResponseDTO orderResponseDTO, CustomerResponseDTO customer) throws MessagingException {
        // Get relevant information to send email
        String receiver = customer.getEmail();
        String subject = "Ordrebekræftelse: Betaling modtaget for ordre nummer: " + orderResponseDTO.getOrderId();
        String body = "Goddag " + customer.getFirstName() + ", \n\n" +
                "Mange tak for din betaling! Vi har nu registreret din transaktion, og din ordre er officielt bekræftet.\n" +
                "Vi går i gang med at behandle din ordre med det samme, så du kan få opført din nye carport.\n\n" +
                "For at se detaljerne samt styklisten over de materialer, der skal bruges til dit byggeri, kan du\n" +
                "logge ind på vores hjemmeside med denne mailadresse. Under fanen 'Min Side' og derefter under\n" +
                "'Ordrer' fanen, kan du finde din aktive ordre med nummeret: \n\n" + orderResponseDTO.getOrderId() +
                "\n\nNår materialerne bliver afsendt fra vores lager, vil du modtage en opdatering omkring levering.\n\n" +
                "Hvis du har spørgsmål til processen eller ønsker at ændre noget, er du altid velkommen til at kontakte os på: " +
                System.getenv("MAIL_USERNAME") + " eller ringe til os på: 12345678\n\n" +
                "Med venlig hilsen,\n" +
                "Johannes Fog Carporte";

        gmailEmailSender.sendPlainTextEmail(receiver, subject, body);
    }

    public void sendPartsList(String toEmail, int orderId, List<ProductsPartsListEntryResponseDTO> partsListWood, List<ProductsPartsListEntryResponseDTO> partsListHardware) throws MessagingException {
        String subject = "Din stykliste til din nye carport vedrørende ordrenummer: " + orderId;
        Map<String, Object> variables = new HashMap<>();
        variables.put("parts_list_wood", partsListWood);
        variables.put("parts_list_hardware", partsListHardware);
        String htmlBody = gmailEmailSenderHTML.renderTemplate("email", variables);

        gmailEmailSenderHTML.sendHtmlEmail(toEmail, subject , htmlBody);
    }
}
