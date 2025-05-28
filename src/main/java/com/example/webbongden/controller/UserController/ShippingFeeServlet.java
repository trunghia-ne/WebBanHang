package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Cart; // Gi? s? b?n có Cart trong session
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject; // Th? vi?n JSON, ví d? org.json

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ShippingFeeServlet", value = "/calculate-shipping-fee")
public class ShippingFeeServlet extends HttpServlet {

    private static final String GHN_TOKEN = "84cec4b1-3712-11f0-8990-3a03389d049e";
    private static final int YOUR_SHOP_ID = 4776733;
    private static final int FROM_DISTRICT_ID = 1442;
    private static final String FROM_WARD_CODE = "20308";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject jsonRequest = new JSONObject(sb.toString());

        int toDistrictId = jsonRequest.getInt("to_district_id");
        String toWardCode = jsonRequest.getString("to_ward_code");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.getWriter().write(new JSONObject().put("error", "Gi? hàng tr?ng").toString());
            return;
        }

        int weight = cart.getTotalWeight();
        int length = cart.getEstimatedLength(); // cm
        int width = cart.getEstimatedWidth();   // cm
        int height = cart.getEstimatedHeight();  // cm
        int insuranceValue = (int) cart.getTotalPriceNumber();
        int serviceTypeId = 2;


        try {
            URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee"); // Production URL

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Token", GHN_TOKEN);

            conn.setDoOutput(true);

            JSONObject payload = new JSONObject();
            payload.put("service_type_id", serviceTypeId);
            payload.put("insurance_value", insuranceValue);
            payload.put("from_district_id", FROM_DISTRICT_ID);
            payload.put("from_ward_code", FROM_WARD_CODE);
            payload.put("to_district_id", toDistrictId);
            payload.put("to_ward_code", toWardCode);
            payload.put("weight", weight);
            payload.put("length", length);
            payload.put("width", width);
            payload.put("height", height);

            String jsonInputString = payload.toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder ghnResponse = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    ghnResponse.append(inputLine);
                }
                in.close();

                JSONObject ghnJson = new JSONObject(ghnResponse.toString());
                if (ghnJson.getInt("code") == 200) {
                    long shippingFee = ghnJson.getJSONObject("data").getLong("total");
                    response.getWriter().write(new JSONObject().put("shipping_fee", shippingFee).toString());
                } else {
                    String message = ghnJson.optString("message", "L?i t? API GHN");
                    if (ghnJson.has("code_message_value")) {
                        message = ghnJson.getString("code_message_value");
                    }
                    response.getWriter().write(new JSONObject().put("error", message).toString());
                }
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();
                System.err.println("GHN Error Response: " + errorResponse.toString());
                response.getWriter().write(new JSONObject().put("error", "L?i k?t n?i ??n GHN: " + responseCode + " - " + errorResponse.toString()).toString());
            }
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(new JSONObject().put("error", "L?i h? th?ng: " + e.getMessage()).toString());
        }
    }
}