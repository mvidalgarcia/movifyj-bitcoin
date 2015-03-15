package com.movify.bitcoin;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

/**
 * Created by Marco Vidal García on 15/3/15.
 */

/**
 * @return  the payment id
 */
public class BitcoinPayment {
    public static String BitcoinPayment(String returnURL, String notifyURL,
                                        String notifyEmail, Double price,
                                        String customerName, Integer orderNumber,
                                        String customerEmail) throws JSONException {

        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json("{  'settled_currency': 'EUR'," +
                "  'return_url': "+returnURL+"," +
                "  'notify_url': "+notifyURL+"," +
                "  'notify_email': "+notifyEmail+",  'price': "+price+"," +
                "  'currency': 'EUR'," +
                "  'reference': {    'customer_name': "+customerName+",    'order_number': "+orderNumber+",    'customer_email': "+customerEmail+"  }}");
        Response response = client.target("https://private-anon-2b8feed1a-bitcoinpaycom.apiary-mock.com/api/v1")
                .path("/payment/btc")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Token fmzGW06lF3hQAqPZyAY8dymz")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        String strResponse = response.readEntity(String.class);
        System.out.println("body:" + strResponse);

        JSONObject res = new JSONObject(strResponse);
        String paymentUrl = res.getJSONObject("data").getString("payment_url");

        System.out.println("Payment URL: " + paymentUrl);
        return res.getJSONObject("data").getString("payment_id");
    }

    /**
     *
     * @param paymentId
     * @return payment status
     */
    public static String BitcoinPaymentStatus(String paymentId) throws JSONException {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://private-anon-2b8feed1a-bitcoinpaycom.apiary-mock.com/api/v1")
                .path("/payment/btc/"+paymentId)
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("Authorization", "Token fmzGW06lF3hQAqPZyAY8dymz")
                .get();

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        String strResponse = response.readEntity(String.class);
        System.out.println("body:" + strResponse);


        JSONObject res = new JSONObject(strResponse);
        String status = res.getJSONObject("data").getString("status");
        return status;
    }


    public static void main(String[] args) {
        /*
         * Example data
        */
        String returnURL = "http://your-e-shop.com/thank-your-for-your-order";
        String notifyURL = "https://your-e-shop.com/order-received";
        String notifyEmail = "order-received@your-e-shop.com";
        Double Price = 17.5;
        String customerName = "Customer Name";
        Integer orderNumber = 123;
        String customerEmail = "customer@example.com";

        try {
            String paymentId = BitcoinPayment(returnURL, notifyURL, notifyEmail,
                    Price, customerName, orderNumber, customerEmail);
            System.out.println("Payment ID: " + paymentId);
            System.out.println("Payment Status: " + BitcoinPaymentStatus(paymentId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
