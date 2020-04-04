package ru.geekbrains.supershop.services.soap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.paymentservice.*;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    public List<Payment> getPayment(String country) {
        PaymentPort paymentPort = new PaymentPortService().getPaymentPortSoap11();

        GetPaymentRequest request = new GetPaymentRequest();
        request.setCountry(country);
        GetPaymentResponse response;

        try {
            response = paymentPort.getPayment(request);
        } catch (Exception e) {
            response = null;
        }

        return response == null ? Collections.emptyList() : response.getPayments();
    }
}
