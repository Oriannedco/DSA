import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.time.*;
import java.nio.file.Files;


public class Signature {

    private int N;
    private BigInteger p;
    private BigInteger l; // correspond à q dans la doc du NIST
    private BigInteger g;
    private BigInteger x;
    private BigInteger y;
    private Generation gen;

    public Signature(){

        N = 160;

        l = BigInteger.TWO.pow(160).add(BigInteger.valueOf(7));
        p = l.multiply(BigInteger.TWO.pow(864).add(BigInteger.valueOf(218))).add(BigInteger.valueOf(1));
        g = BigInteger.TWO.modPow(p.subtract(BigInteger.valueOf(1)).divide(l), p) ;

        // génération de la paire de clés (x,y)
        gen = new Generation(p, l, g);
        gen.genKeyPair();
        x = gen.getPrivateKey();
        y = gen.getPublicKey();
    }

    public BigInteger[] sign(String message){ 
 
        // génération de k et de son inverse
        gen.genPerMessageNumber();
        BigInteger k = gen.getPerMessageNumber();
        BigInteger kInv = gen.getInvPerMessageNumber(); 

        BigInteger r = g.modPow(k, p).mod(l);

         // choix de la fonction de hashage utilisée
        String hashAlgorithm = "SHA-256"; 

        try {

            // passage du message par la fonction de hashage
            MessageDigest hash = MessageDigest.getInstance(hashAlgorithm);
            byte[] byteMessage = message.getBytes();
            hash.update(byteMessage);
            byte[] hashMessage = hash.digest();
            BigInteger z = new BigInteger(Arrays.copyOf(hashMessage, Math.min(N, hashMessage.length))); 

            BigInteger s = kInv.multiply(z.add(x.multiply(r))).mod(l);
            BigInteger[] signature = {r, s};

            // génération d'une signature avec un nouveau k si r ou s est égal à zéro 
            if (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO)){

                sign(message);
            }

            return signature;
        }
        catch( NoSuchAlgorithmException e){

            System.err.println(hashAlgorithm + "n'est pas un algorithme valide.");
            return null;
        }
    }

    public BigInteger[] sign(){

        try {
            
            String alice = Files.readString(Path.of("alice.txt"));
            return this.sign(alice);

        } catch (IOException e){

            System.out.println("Le fichier alice.txt ne peut être ouvert.");
            return null;
        }
    }

    public boolean verify(String message, BigInteger r, BigInteger s, BigInteger PublicKey){

        if (r.compareTo(l) >= 0 || r.equals(BigInteger.ZERO) || s.compareTo(l) >=0 || s.equals(BigInteger.ZERO)){

            //System.out.println("La signature est invalide.");
            return false;
        }

        BigInteger w = s.modInverse(l);

        String hashAlgorithm = "SHA-256";
        try {
            
            // passage du message par la fonction de hashage
            MessageDigest hash = MessageDigest.getInstance(hashAlgorithm);
            byte[] byteMessage = message.getBytes();
            hash.update(byteMessage);
            byte[] hashMessage = hash.digest();
            BigInteger z = new BigInteger(Arrays.copyOf(hashMessage, Math.min(N, hashMessage.length))); 

            BigInteger u1 = z.multiply(w).mod(l);
            BigInteger u2 = r.multiply(w).mod(l);
            BigInteger v = g.modPow(u1, p).multiply(PublicKey.modPow(u2, p)).mod(p).mod(l);

            if (v.equals(r)){
                
                //System.out.println("La signature est vérifiée.");
                return true;
            } else {

                //System.out.println("La signature est invalide.");
                return false;
            }
        }
        catch( NoSuchAlgorithmException e){

            System.err.println(hashAlgorithm + "n'est pas un algorithme valide.");
        }

        //System.out.println("La signature est invalide.");
        return false;
    }

    public boolean verify(BigInteger r, BigInteger s, BigInteger PublicKey){

        try {
            
            String alice = Files.readString(Path.of("alice.txt"));
            return this.verify(alice, r, s, PublicKey);

        } catch (IOException e){

            System.out.println("Le fichier alice.txt ne peut être ouvert.");
            return false;
        }
    }

    public void timeSign(String message, int nombreTest){

        Instant start = Instant.now();
        
        for (int i = 0; i < nombreTest; i++){

            sign(message);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Temps pour effectuer " + nombreTest + " signatures : " + duration);
    } 

    // test par défaut du temps de signature avec le texte alice.txt et 10 000 itérations
    public void timeSign(){

        try {
            
            String alice = Files.readString(Path.of("alice.txt"));
            this.timeSign(alice, 10000);

        } catch (IOException e){

            System.out.println("Le fichier alice.txt ne peut être ouvert.");
        };
    }

    public void timeVerif(String message, int nombreTest){

        BigInteger[] signature = sign(message);

        Instant start = Instant.now();
        
        for (int i = 0; i < nombreTest; i++){

            verify(message, signature[0], signature[1], y);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Temps pour effectuer " + nombreTest + " vérifications : " + duration);
    } 

    // test par défaut du temps de vérification avec le texte alice.txt et 10 000 itérations
    public void timeVerif(){

        try {
            
            String alice = Files.readString(Path.of("alice.txt"));
            this.timeVerif(alice, 10000);

        } catch (IOException e){

            System.out.println("Le fichier alice.txt ne peut être ouvert.");
        };
    }

    public BigInteger getPublicKey(){

        return y;
    }
}
