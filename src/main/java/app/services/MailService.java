package app.services;

import app.dto.responseDTO.*;
import app.services.utils.GmailEmailSender;
import app.services.utils.GmailEmailSenderHTML;
import jakarta.mail.MessagingException;
import org.thymeleaf.TemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailService {
    private GmailEmailSender gmailEmailSender;
    private GmailEmailSenderHTML gmailEmailSenderHTML;

    public MailService(TemplateEngine templateEngine) {
        this.gmailEmailSender = new GmailEmailSender();
        this.gmailEmailSenderHTML = new GmailEmailSenderHTML(templateEngine);
    }

    public void sendInquiryNotice(InquiryResponseDTO inquiryResponseDTO) throws MessagingException {
        CustomerResponseDTO customer = inquiryResponseDTO.getCustomerResponseDTO();
        String receiver = customer.getEmail();
        String subject = "Tak for din forespørgsel";
        String body = "Goddag " + customer.getFirstName() + ", \n\n" +
                "Mange tak for din forespørgsel om en carport med egne mål. \n\n Du vil snarest modtage et opkald, fra en af vores dygtige sælgere. \n" +
                "Der vil vi sammen gennemgå dine valg, måske har vi nogle forslag eller måske har du selv. Det finder vi ud af sammen.\n\n"+
                "Indtil da vi arbejde hårdt, på at kunne gøre det til, lige præcist din carport.\n\n"+
                "Du er altid velkommen til at kontakte os på: 2semesterprojekt@cph.dk eller " +
                "ringe til os på: 12345678 \n";

        gmailEmailSender.sendPlainTextEmail(receiver, subject, body);
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
                "ellers er du altid velkommen til at kontakte os på: 2semesterprojekt@cph.dk eller " +
                "ringe til os på: 12345678 \n";

        gmailEmailSender.sendPlainTextEmail(receiver, subject, body);
    }

    public void sendOrderConfirmation(OrderResponseDTO orderResponseDTO) throws MessagingException {
        // Get relevant information to send email
        CustomerResponseDTO customer = orderResponseDTO.getCustomerResponseDTO();
        String receiver = customer.getEmail();
        String subject = "Ordrebekræftelse: Betaling modtaget for ordre nummer: " + orderResponseDTO.getOrderId();
        String body = "Goddag " + customer.getFirstName() + ", \n\n" +
                "Mange tak for din betaling! Vi har nu registreret din transaktion, og din ordre er officielt bekræftet.\n" +
                "Vi går i gang med at behandle din ordre med det samme, så du kan få opført din nye carport hurtigst muligt.\n\n" +
                "For at se detaljerne samt styklisten over de materialer, der skal bruges til dit byggeri, kan du\n" +
                "logge ind på vores hjemmeside med denne mailadresse. Under fanen 'Min Side' og derefter under\n" +
                "'Ordrer' fanen, kan du finde din aktive ordre med nummeret: \n\n" + orderResponseDTO.getOrderId() +
                "\n\nNår materialerne bliver afsendt fra vores lager, vil du modtage en opdatering omkring levering.\n\n" +
                "Hvis du har spørgsmål til processen eller ønsker at ændre noget, er du altid velkommen til at kontakte os på: " +
                " 2semesterprojekt@cph.dk eller ringe til os på: 12345678";

        gmailEmailSender.sendPlainTextEmail(receiver, subject, body);
    }

    public void sendPartsList(String toEmail, int orderId, List<ProductsPartsListEntryResponseDTO> partsListWood, List<ProductsPartsListEntryResponseDTO> partsListHardware, String svg) throws MessagingException {
        String subject = "Din stykliste til din nye carport vedrørende ordrenummer: " + orderId;
        Map<String, Object> variables = new HashMap<>();
        variables.put("parts_list_wood", partsListWood);
        variables.put("parts_list_hardware", partsListHardware);
        variables.put("svg_carport_details", svg);
        String htmlBody = gmailEmailSenderHTML.renderTemplate("email", variables);

        gmailEmailSenderHTML.sendHtmlEmail(toEmail, subject , htmlBody);
    }
}
