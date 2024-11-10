import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.math.BigInteger;

public class Program {
    public static void main(String[] args) throws Exception {
        
        Scanner scan = new Scanner(System.in);

        System.out.println("Mode du programme ? (test/signature/verification)");
        String input = scan.nextLine();

        if(input.equals("test")){

            System.out.println("Effectuer le test par default ? (oui/non)");
            input = scan.nextLine();

            if (input.equals("oui")){

                Signature test = new Signature();
                test.timeSign();
                test.timeVerif();

            } else if (input.equals("non")) {

                System.out.println("Entrer le chemin vers le fichier texte à tester :");
                input = scan.nextLine();

                try {
                    String texte = Files.readString(Path.of(input));

                    System.out.println("Entrer le nombre de tests à effectuer :");

                    int nombreDeTest = Integer.valueOf(scan.nextLine());

                    Signature test = new Signature();
                    test.timeSign(texte, nombreDeTest);
                    test.timeVerif(texte, nombreDeTest);
                
                } catch (IOException e){

                    System.out.println("Fichier texte introuvable.");
                }

            } else {

                System.out.println("La commande entrée est invalide. ");

            }
        } else if (input.equals("signature")){

            System.out.println("Utiliser le texte par défaut ? (oui/non)");
            input = scan.nextLine();

            if(input.equals("oui")){

                Signature test = new Signature();
                BigInteger[] signature = test.sign();
                System.out.println("La signature est : ( " + signature[0].toString() + ", " + signature[1].toString() + ")");
                System.out.println("La clé publique est : " + test.getPublicKey().toString());

            } else if (input.equals("non")) {

                System.out.println("Entrer le chemin vers le fichier texte à signer :");
                input = scan.nextLine();

                try {

                    String texte = Files.readString(Path.of(input));
                    Signature test = new Signature();
                    BigInteger[] signature = test.sign(texte);
                    System.out.println("La signature est ( " + signature[0].toString() + ", " + signature[1].toString() + ")");
                    System.out.println("La clé publique est : " + test.getPublicKey().toString());

                } catch (IOException e){

                    System.out.println("Fichier texte introuvable.");

                }
            } else {

                    System.out.println("La commande entrée est invalide. ");

            }
         } else if (input.equals("verification")){

            System.out.println("Entrer la signature à vérifier (r, s) :");
            input = scan.nextLine();
            String[] signature = input.split(", ");
            BigInteger r = new BigInteger(signature[0]);
            BigInteger s = new BigInteger(signature[1]);

            System.out.println("Entrer la clé publique :");
            BigInteger publicKey = new BigInteger(scan.nextLine());

            Signature test = new Signature();

            System.out.println("Utiliser le fichier texte par défaut ? (oui/non)");
            input = scan.nextLine();

            if (input.equals("oui")){

                if (test.verify(r, s, publicKey)){

                    System.out.println("La signature est valide.");

                } else {

                    System.out.println("La signature est invalide.");

                }
            } else if (input.equals("non")){

                System.out.println("Entrer le chemin vers le fichier texte associé à la signature : ");
                input = scan.nextLine();

                try {

                    String texte = Files.readString(Path.of(input));

                    if (test.verify(texte, r, s, publicKey)){

                        System.out.println("La signature est valide.");
    
                    } else {
    
                        System.out.println("La signature est invalide.");
    
                    }

                } catch (IOException e){

                    System.out.println("Fichier texte introuvable.");

                }
            } else {

                System.out.println("La commande entrée est invalide.");

            }
        } else {

            System.out.println("La commande entrée est invalide.");

        }

        scan.close();
    }
}
