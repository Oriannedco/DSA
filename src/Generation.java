import java.math.BigInteger;
import java.security.SecureRandom;

public class Generation {

    private int N = 160;
    private BigInteger p;
    private BigInteger l;
    private BigInteger g;
    private BigInteger x;
    private BigInteger y;
    private BigInteger k;
    private BigInteger kInv;

    public Generation(BigInteger p, BigInteger l, BigInteger g){

        this.p = p;
        this.l = l;
        this.g = g;
        x = BigInteger.ZERO;
        y = BigInteger.ZERO;
        k = BigInteger.ZERO;
        kInv = BigInteger.ZERO;
    }

    //génère une paire de clés associée aux paramètres de domaine p, l et q
    public void genKeyPair(){

        //génération de la paire de clés (x,y)
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[N+64]; 
        random.nextBytes(bytes);
        BigInteger c = new BigInteger(bytes);
        x = c.mod(l.subtract(BigInteger.valueOf(1))).add(BigInteger.valueOf(1));
        y = g.modPow(x,p);
    }

    public BigInteger getPublicKey(){

        return y;

    }

    public BigInteger getPrivateKey(){

        return x;
    }

    //génére un nombre secret aléatoire et son inverse
    public void genPerMessageNumber(){

        //génération de k
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[N+64];
        random.nextBytes(bytes);
        BigInteger c = new BigInteger(bytes);
        k = c.mod(l.subtract(BigInteger.valueOf(1))).add(BigInteger.valueOf(1));

        // calcul de l'inverse de k modulo l
        kInv = k.modInverse(l);
    }

    public BigInteger getPerMessageNumber(){

        return k;
    }

    public BigInteger getInvPerMessageNumber(){

        return kInv;
    }
}
