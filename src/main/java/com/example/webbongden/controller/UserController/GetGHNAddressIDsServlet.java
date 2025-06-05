package com.example.webbongden.controller.UserController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

@WebServlet(name = "GetGHNAddressIDsServlet", value = "/get-ghn-address-ids")
public class GetGHNAddressIDsServlet extends HttpServlet {

    private static final String GHN_TOKEN_FOR_MASTER_DATA = "84cec4b1-3712-11f0-8990-3a03389d049e";
    private static final String GHN_API_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String provinceNameInput = request.getParameter("province");
        String districtNameInput = request.getParameter("district");
        String wardNameInput = request.getParameter("ward");

        JSONObject jsonResponse = new JSONObject();

        if (provinceNameInput == null || districtNameInput == null || wardNameInput == null ||
                provinceNameInput.trim().isEmpty() || districtNameInput.trim().isEmpty() || wardNameInput.trim().isEmpty()) {
            jsonResponse.put("error", "Vui lòng cung cấp đủ tên Tỉnh/Thành, Quận/Huyện, Phường/Xã.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        System.out.println("[SERVLET DEBUG] Received: province=" + provinceNameInput + ", district=" + districtNameInput + ", ward=" + wardNameInput);


        try {
            int ghnProvinceID = -1;
            String foundGhnProvinceName = null;
            JSONArray provinces = callGHNMasterDataAPI("/province", null);

            if (provinces != null) {
                System.out.println("[SERVLET DEBUG] Danh sách tỉnh từ GHN API:");
                for (int i = 0; i < provinces.length(); i++) {
                    JSONObject p = provinces.getJSONObject(i);
                    String currentGhnProvinceName = p.getString("ProvinceName");
                    System.out.println("  - ID: " + p.getInt("ProvinceID") + ", Tên GHN: '" + currentGhnProvinceName + "'");
                }
                System.out.println("-----------------------------------------");


                for (int i = 0; i < provinces.length(); i++) {
                    JSONObject p = provinces.getJSONObject(i);
                    String currentGhnProvinceName = p.getString("ProvinceName");

                    System.out.println("[SERVLET DEBUG] Comparing (Province): GHN API Name = '" + currentGhnProvinceName + "', Input Name = '" + provinceNameInput + "'");
                    boolean isMatch = flexibleNameMatch(currentGhnProvinceName, provinceNameInput, "province");
                    System.out.println("[SERVLET DEBUG] Match result: " + isMatch);

                    if (isMatch) {
                        ghnProvinceID = p.getInt("ProvinceID");
                        foundGhnProvinceName = currentGhnProvinceName;
                        System.out.println("[SERVLET DEBUG] Province MATCHED! GHN Name: '" + foundGhnProvinceName + "', Input Name: '" + provinceNameInput + "', GHN ProvinceID: " + ghnProvinceID);
                        break;
                    }
                }
            }

            if (ghnProvinceID == -1) {
                jsonResponse.put("error", "Không tìm thấy Tỉnh/Thành phố GHN khớp với: '" + provinceNameInput + "'. Vui lòng kiểm tra lại tên hoặc cách GHN định dạng.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            int ghnDistrictID = -1;
            String foundGhnDistrictName = null;
            JSONArray districts = callGHNMasterDataAPI("/district", "province_id=" + ghnProvinceID);
            if (districts != null) {
                for (int i = 0; i < districts.length(); i++) {
                    JSONObject d = districts.getJSONObject(i);
                    String currentGhnDistrictName = d.getString("DistrictName");
                    JSONArray nameExtensions = d.optJSONArray("NameExtension");

                    System.out.println("[SERVLET DEBUG] Comparing (District): GHN API Name = '" + currentGhnDistrictName + "', Input Name = '" + districtNameInput + "'");
                    boolean isMatch = flexibleNameMatch(currentGhnDistrictName, districtNameInput, "district");

                    if (!isMatch && nameExtensions != null) {
                        for (int j = 0; j < nameExtensions.length(); j++) {
                            String extName = nameExtensions.getString(j);
                            System.out.println("[SERVLET DEBUG] Comparing (District Extension): GHN Ext. Name = '" + extName + "', Input Name = '" + districtNameInput + "'");
                            if (flexibleNameMatch(extName, districtNameInput, "district_extension")) {
                                isMatch = true;
                                currentGhnDistrictName = extName;
                                break;
                            }
                        }
                    }
                    System.out.println("[SERVLET DEBUG] District match result: " + isMatch);

                    if (isMatch) {
                        ghnDistrictID = d.getInt("DistrictID");
                        foundGhnDistrictName = currentGhnDistrictName;
                        System.out.println("[SERVLET DEBUG] District MATCHED! GHN Name: '" + foundGhnDistrictName + "', Input Name: '" + districtNameInput + "', GHN DistrictID: " + ghnDistrictID);
                        break;
                    }
                }
            }

            if (ghnDistrictID == -1) {
                jsonResponse.put("error", "Không tìm thấy Quận/Huyện GHN khớp với: '" + districtNameInput + "' trong tỉnh '" + (foundGhnProvinceName != null ? foundGhnProvinceName : provinceNameInput) + "'.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            String ghnWardCode = null;
            String foundGhnWardName = null;
            JSONArray wards = callGHNMasterDataAPI("/ward", "district_id=" + ghnDistrictID);
            if (wards != null) {
                for (int i = 0; i < wards.length(); i++) {
                    JSONObject w = wards.getJSONObject(i);
                    String currentGhnWardName = w.getString("WardName");
                    JSONArray nameExtensions = w.optJSONArray("NameExtension");

                    System.out.println("[SERVLET DEBUG] Comparing (Ward): GHN API Name = '" + currentGhnWardName + "', Input Name = '" + wardNameInput + "'");
                    boolean isMatch = flexibleNameMatch(currentGhnWardName, wardNameInput, "ward");

                    if (!isMatch && nameExtensions != null) {
                        for (int j = 0; j < nameExtensions.length(); j++) {
                            String extName = nameExtensions.getString(j);
                            System.out.println("[SERVLET DEBUG] Comparing (Ward Extension): GHN Ext. Name = '" + extName + "', Input Name = '" + wardNameInput + "'");
                            if (flexibleNameMatch(extName, wardNameInput, "ward_extension")) {
                                isMatch = true;
                                currentGhnWardName = extName;
                                break;
                            }
                        }
                    }
                    System.out.println("[SERVLET DEBUG] Ward match result: " + isMatch);

                    if (isMatch) {
                        ghnWardCode = w.getString("WardCode");
                        foundGhnWardName = currentGhnWardName;
                        System.out.println("[SERVLET DEBUG] Ward MATCHED! GHN Name: '" + foundGhnWardName + "', Input Name: '" + wardNameInput + "', GHN WardCode: " + ghnWardCode);
                        break;
                    }
                }
            }

            if (ghnWardCode == null) {
                jsonResponse.put("error", "Không tìm thấy Phường/Xã GHN khớp với: '" + wardNameInput + "' trong quận/huyện '" + (foundGhnDistrictName != null ? foundGhnDistrictName : districtNameInput) + "'.");
            } else {
                jsonResponse.put("ghnDistrictId", ghnDistrictID);
                jsonResponse.put("ghnWardCode", ghnWardCode);
                System.out.println("[SERVLET SUCCESS] Trả về: DistrictID=" + ghnDistrictID + ", WardCode=" + ghnWardCode);
            }

        } catch (Exception e) {
            System.err.println("[SERVLET EXCEPTION] Lỗi nghiêm trọng: " + e.getMessage());
            e.printStackTrace();
            jsonResponse.put("error", "Lỗi hệ thống khi lấy mã địa chỉ GHN. Chi tiết: " + e.getMessage());
        }
        response.getWriter().write(jsonResponse.toString());
    }

    private JSONArray callGHNMasterDataAPI(String endpointPath, String queryParams) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(GHN_API_BASE_URL + endpointPath);
        if (queryParams != null && !queryParams.isEmpty()) {
            urlBuilder.append("?").append(queryParams);
        }
        System.out.println("[SERVLET DEBUG] Gọi API GHN: " + urlBuilder.toString());

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        if (GHN_TOKEN_FOR_MASTER_DATA != null && !GHN_TOKEN_FOR_MASTER_DATA.isEmpty() && !"YOUR_GHN_API_TOKEN_FOR_MASTER_DATA_IF_NEEDED".equals(GHN_TOKEN_FOR_MASTER_DATA)) {
            conn.setRequestProperty("Token", GHN_TOKEN_FOR_MASTER_DATA);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }
            in.close();
            JSONObject jsonResponse = new JSONObject(responseContent.toString());
            if (jsonResponse.getInt("code") == 200) {
                return jsonResponse.getJSONArray("data");
            } else {
                System.err.println("Lỗi logic từ API GHN ("+ endpointPath +"): " + jsonResponse.optString("message", jsonResponse.toString()));
                return null;
            }
        } else {
            System.err.println("Lỗi HTTP khi gọi API GHN ("+ endpointPath +"): " + responseCode);
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) { // Đảm bảo đọc lỗi bằng UTF-8
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                System.err.println("GHN Error Response Body: " + errorResponse.toString());
            } catch (IOException ex) {
                System.err.println("Không thể đọc error stream từ GHN: " + ex.getMessage());
            }
            return null;
        }
    }

    private boolean flexibleNameMatch(String ghnName, String inputName, String type) {
        if (ghnName == null || inputName == null) return false;

        String normalizedGhnName = normalizeStringForMatching(ghnName, type);
        String normalizedInputName = normalizeStringForMatching(inputName, type);


        if (normalizedGhnName.equalsIgnoreCase(normalizedInputName)) {
            return true;
        }

        if (normalizedGhnName.contains(normalizedInputName) || normalizedInputName.contains(normalizedGhnName)) {
            int lenDiff = Math.abs(normalizedGhnName.length() - normalizedInputName.length());
            int minLen = Math.min(normalizedGhnName.length(), normalizedInputName.length());
            int maxLen = Math.max(normalizedGhnName.length(), normalizedInputName.length());

            if ( (normalizedGhnName.startsWith(normalizedInputName) && minLen > 2) ||
                    (normalizedInputName.startsWith(normalizedGhnName) && minLen > 2) ||
                    (lenDiff <= maxLen / 2 && minLen > 2)
            ) {
                return true;
            }
        }
        return false;
    }

    private String normalizeStringForMatching(String str, String type) {
        if (str == null) return "";

        String temp = str;
        temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");

        temp = temp.toLowerCase();

        if (!type.contains("extension")) {
            if (type.equals("province")) {
                temp = temp.replaceFirst("^(thanh pho|tp|tinh)\\s+", "");
            } else if (type.equals("district")) {
                temp = temp.replaceFirst("^(quan|huyen|thi xa|tx)\\s+", "");
            } else if (type.equals("ward")) {
                temp = temp.replaceFirst("^(phuong|xa|thi tran|tt)\\s+", "");
            }
        }

        temp = temp.trim().replaceAll("\\s+", " ").replaceAll("[-–—]", " ");
        temp = temp.replaceAll("\\s+", " ");

        return temp;
    }

}