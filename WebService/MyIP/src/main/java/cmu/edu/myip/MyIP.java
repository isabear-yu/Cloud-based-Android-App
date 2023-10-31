package cmu.edu.myip;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
@WebServlet(name = "myIP", urlPatterns = {"/result","/dashboard"})
public class MyIP extends HttpServlet {
    IPmodel model = null;  // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        model = new IPmodel();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String nextView;

        URL myIP;

        /**
         * if the urlPath contains /dashboard then get call getFromdb method to get data from mongo
         * and display to dashboard
         */
        if (request.getServletPath().contains("/dashboard")){

                String data = model.getFromdb();
                System.out.println(data);
                request.setAttribute("time", data.split("@")[0]);
                request.setAttribute("timezone",data.split("@")[1]);
                request.setAttribute("IP", data.split("@")[2]);
                request.setAttribute("count", data.split("@")[3]);
                request.setAttribute("parsedData", data.split("@")[4]);

    }

        /**
         * if searchTerm is not null
         * fetch the data from 3rd party API
         * save the data to MongoDb
         * and display the json to https://protected-scrubland-77046.herokuapp.com/result?searchTerm=
         */
        if (request.getParameter("searchTerm")!=null){
        try {
            String searchTerm = request.getParameter("searchTerm");
            myIP = new URL("https://ipinfo.io/" + searchTerm + "/geo");
            HttpURLConnection conn = (HttpURLConnection) myIP.openConnection();
            conn.setRequestMethod("GET");


            InputStream stream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder responce = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responce.append(line);
                responce.append("\r");
            }
            reader.close();

            String ipAdress = responce.toString();

            out.print(responce);
            model.saveDatatoDb(ipAdress);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


        if(request.getServletPath().contains("/dashboard")){
            System.out.println("dashboard!");
            nextView = "result.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        }


    }



}

