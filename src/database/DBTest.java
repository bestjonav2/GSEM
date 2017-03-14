/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

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
        db.validateCred("Jona", "123");
    }
    
}
