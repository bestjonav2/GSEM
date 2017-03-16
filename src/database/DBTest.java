/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jona622
 */
public class DBTest {
    public static void main(String[] args) {
        DBConnect db = new DBConnect();
        System.out.println(db.getUserID("Jona"));
        System.out.println(db.getUserID("juan"));
        System.out.println(db.getUserID("julian"));
        PasswordHash aux = new PasswordHash();
        try {
            aux.createHash("123");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(DBTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Salt="+aux.getSalt());
        System.out.println("Hash="+aux.getHash());
        System.out.println(db.validateCred("Jona", "1233333"));
    }
    
}
