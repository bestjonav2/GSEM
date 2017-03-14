/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jona622
 */
public class DBConnect {

    private String host = "jdbc:mysql://localhost/gsmDB";
    private String user = "root";
    private String passw = "181297jm";
    private Connection dbcon;

    private Connection connect() {
        try {
            this.dbcon = DriverManager.getConnection(host, user, passw);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbcon;
    }

    public void disconnect() {
        if (dbcon != null) {
            try {
                dbcon.close();
                dbcon = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public String getUserID(String user) {
        String result = "";
        try {
            connect();
            Statement stat = dbcon.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `user`");
            rs.next();
            while (!user.equals(rs.getString("username"))) {
                rs.next();
            }
            result = rs.getString("iduser");
        } catch (SQLException e) {
            result = "ERROR_CODE: 18 (USER NOT FOUND)";
        } finally {
            disconnect();
        }
        return result;
    }

    public boolean validateCred(String user, String passw) {
        boolean acces = false;
        try {
            connect();
            Statement stat = dbcon.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `user` WHERE iduser = '"+getUserID(user)+"'");
            rs.next();
            try {
                acces = new PasswordHash().validatePassword(passw, rs.getString("hash"), rs.getString("salt"));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            }

            return acces;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return acces;
        } finally {
            disconnect();
        }
    }
}
