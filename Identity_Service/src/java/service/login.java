/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import connection.DB;
import java.sql.Connection;
import org.json.*;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Fitra Rahmamuliani
 */

@WebServlet(name="login", urlPatterns = "/login")
public class login extends HttpServlet {
    private static final long serialVersionUID = 1;

    public login(){
        super();
        /* to do autogenerated contructor stub*/
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        /*KAMUS*/
        String email, password, sql, token, delquery, insquery, userAgent, ip;
        ResultSet result;
        int uid;
        Calendar calendar;
        Timestamp token_expired;
        /* Make JSON object */
        JSONObject jo = new JSONObject();
        /*ALGORITMA*/
        /* Connect to database */
        Connection conn = DB.connect();
        response.setContentType("application/json");
        try(PrintWriter out = response.getWriter()){
            email= request.getParameter("email");
            password = (request.getParameter("password"));
            if (email != null & password != null){
                sql = "SELECT * FROM user WHERE email = ? AND password = ?";
                conn.setAutoCommit(false);
                
                try(PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    result= stmt.executeQuery();
                    if (result.next())
                    {
                       uid = result.getInt("user_id");
                       calendar = Calendar.getInstance();
                       calendar.setTime(new Date());
                       calendar.add(Calendar.DATE, 1);
                       token_expired = new Timestamp(calendar.getTime().getTime());
                       token = UUID.randomUUID().toString().replaceAll("-","");
                       userAgent = request.getHeader("User-Agent");
                       token = token + "#" + userAgent;
                       ip = request.getRemoteAddr();
                       if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1"))
                       {
                           InetAddress inetAddress = InetAddress.getLocalHost();
                           String ipAddress = inetAddress.getHostAddress();
                           ip = ipAddress;
                       }
                       token += "#" + ip;
                       delquery = "DELETE FROM token WHERE user_id=?;";
                       insquery = "INSERT INTO token (token_id,user_id,token_expired) VALUES (?,?,?);";
                       try
                       {
                           PreparedStatement delstmt = conn.prepareStatement(delquery);
                           PreparedStatement insstmt = conn.prepareStatement(insquery);
                           delstmt.setInt(1,uid);
                           insstmt.setString(1, token);
                           insstmt.setInt(2, uid);
                           insstmt.setTimestamp(3, token_expired);
                           delstmt.execute();
                           insstmt.execute();
                           jo.put("token",token);
                           jo.put("token_expired",token_expired.getTime());
                           conn.commit();
                       }
                       finally{
                           conn.setAutoCommit(true);
                       }
                    }
                    else
                    {
                         jo.put("ERROR","Wrong username and password");   
                    }
                }
            }
            out.println(jo.toString());
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
       processRequest(request, response);
   }
   
   public static String md5(String input) {
        String md5 = null;
        if(null == input) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}