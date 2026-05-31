package app.services;

import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.services.utils.GmailEmailSender;
import jakarta.mail.MessagingException;

public class MailService {
    private GmailEmailSender gmailEmailSender;


    public MailService(GmailEmailSender gmailEmailSender){
        this.gmailEmailSender = gmailEmailSender;
    }


    public void sendQuoteNotice(InquiryResponseDTO inquiryResponseDTO) throws MessagingException {
        //Get relevant information to send email
        CustomerResponseDTO customer = inquiryResponseDTO.getCustomerResponseDTO();
        String reciever = customer.getEmail();
        String subject = "Tilbud modtaget med tilbudsnummer: " + inquiryResponseDTO.getInquiryId() + " på en carport med egne mål";
        String body = "Goddag " + customer.getFirstName() + ", \n" +
                "Tak for en god snak, vi har nu sendt dig et tilbud på en carport. Der vil selvfølgelig være" +
                " de nye tilføjelser med, hvis det blev aftalt, ellers er den som du selv har lavet.\n" +
                "For at se og betale for ordren, skal du logge ind på vores hjemmeside, logge ind med samme mail som\n" +
                " du har modtaget denne mail på. Derefter skal du åbne fanen 'Min Side' og til sidst under 'Tilbud' fanen\n " +
                " åbne tilbuddet med nummeret: " + inquiryResponseDTO.getInquiryId() +
                "\n Vi håber alt er som det skal være, " +
                "ellers er du altid velkommen til at kontakte os på: " + System.getenv("MAIL_USERNAME") + " eller " +
                "ringe til os på: 12345678";


        gmailEmailSender.sendPlainTextEmail(reciever, subject, body);

    }
}
