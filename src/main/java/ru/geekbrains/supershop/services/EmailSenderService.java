package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.geekbrains.supershop.persistence.entities.Purchase;


@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String sendTo, Purchase purchase) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(sendTo);

        mailMessage.setSubject("Purchase at Supershop");
        mailMessage.setText("Your purchase is successful!\n" + "You have bought " + purchase.getProducts().size() + " items and it costs " + purchase.getPrice() + "\nThank You!");

        javaMailSender.send(mailMessage);
    }
}
