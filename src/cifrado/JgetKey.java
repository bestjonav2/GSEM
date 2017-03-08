import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JgetKey{
  public static String genKey(String user, String passw){
        String key = "";
        byte[] digest = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(user.getBytes("UTF-8"));
            digest = md.digest();
            user = String.format("%064x", new java.math.BigInteger(1, digest));
            System.out.println("User "+user);
            md.update(passw.getBytes("UTF-8"));
            digest = md.digest();
            passw = String.format("%064x", new java.math.BigInteger(1, digest));
            System.out.println("Passw: "+passw);
            user = user.substring(0,user.length()/6);
            passw = passw.substring(54,passw.length());
            key = user+passw;

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(JgetKey.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }
  public static String genIV(String user,String passw){
    String iv = ""
    byte[] digest = null;
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA-256");
        md.update(user.getBytes("UTF-8"));
        digest = md.digest();
        user = String.format("%064x", new java.math.BigInteger(1, digest));
        System.out.println("User "+user);
        md.update(passw.getBytes("UTF-8"));
        digest = md.digest();
        passw = String.format("%064x", new java.math.BigInteger(1, digest));
        System.out.println("Passw: "+passw);
        user = user.substring(0,user.length()/6);
        passw = passw.substring(54,passw.length());
        for (int i = 0; i < user.length(); i++) {
          iv += user.charAt(i);
          iv += passw.charAt(i);
        }

    } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
        Logger.getLogger(JgetKey.class.getName()).log(Level.SEVERE, null, ex);
    }
    return iv;
  }
}
