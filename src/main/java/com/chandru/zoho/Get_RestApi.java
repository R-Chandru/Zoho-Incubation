package com.chandru.zoho;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

@WebServlet(name = "Get_RestApi", value = "/Get_RestApi")
public class Get_RestApi extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String JSON = getRESt();
        JSONArray list = new JSONArray(JSON);

        writer.write("Id\tFirstName\tLastName\tEmail\n\n");
        for (int itr=0 ; itr<list.length() ; itr++) {
            JSONObject object = list.getJSONObject(itr);
            writer.write(object.getInt("Id") + "\t" + object.getString("firstName") + "\t\t" +
                    object.getString("lastName") + "\t\t" + object.getString("email") + "\n");
        }

        writer.close();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected String getRESt() {
        StringBuffer jsonResponse = new StringBuffer(); //Raw String buffer where it gets the response from server
        try {
            String url = "http://localhost:8080/Zoho_war_exploded/Rest_Servlet?";
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                jsonResponse.append(inputLine);
            }
            reader.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse.toString();
    }
}
