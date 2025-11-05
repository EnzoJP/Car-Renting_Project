package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.exceptions.ErrorServiceException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Service
public class PagoService {

    @Value("${mp.access.token}")
    private String mpAccessToken;

    public String Pago() throws ErrorServiceException {


        MercadoPagoConfig.setAccessToken(mpAccessToken);

        PreferenceClient client = new PreferenceClient();

        Double precio1 = 10.00;

        PreferenceRequest request = PreferenceRequest.builder()
                .items(Arrays.asList(
                        PreferenceItemRequest.builder()
                                .title("Pago de Auto")
                                .description("Pago de el alquiler de auto")
                                .quantity(1)
                                .unitPrice(BigDecimal.valueOf(precio1))
                                .build()
                ))
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                //url para cuando paga bien
                                .success("https://sprintproject.tech/inicio")
                                //url para cuando falla
                                .failure("https://sprintproject.tech/inicio")
                                //url para cuando queda pendiente
                                .pending("https://sprintproject.tech/inicio")
                                .build()
                )


                // esta es la URL donde Mercado Pago manda la info del pago con un POST
                .notificationUrl("https://sprintproject.tech/inicio")

                // esto es un identificador que tiene que ser unico
                .externalReference("orden_" + UUID.randomUUID().toString())

                .autoReturn("approved")

                .build();

        try {
            Preference preference = client.create(request);
            return preference.getInitPoint();
        } catch (com.mercadopago.exceptions.MPApiException e) {
            System.out.println("Error status: " + e.getStatusCode());
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Error cause: " + e.getCause());

            if (e.getApiResponse() != null) {
                System.out.println("API Response body: " + e.getApiResponse().getContent());
            }
            throw new ErrorServiceException("error");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("error");
        }
    }
}