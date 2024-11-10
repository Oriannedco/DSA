	Le programme permet de signer un message ou de vérifier une signature via DSA. Le niveau de sécurité utilisé est obsolète ( 80 bits ). 

- Algorithmes utilisés : 
	
	- La classe BigInteger est utilisée afin de stocker des grands nombres et de les manipuler plus facilement. 

	- La classe java.security.SecureRandom a été choisi pour la génération de nombres aléatoires car elle fournit un générateur de nombres aléatoires cryptographiquement fort. 

	- La classe java.security.MessageDigest permet l'utilisation de plusieurs fonctions de hashage, celle sélectionnée pour ce programme est SHA-256.

- Comment fonctionne le programme ? 

	Le programme est composé d'un main et de deux classes. 

	- La classe Generation prend en entrée les paramètres de domaine et permet de générer une paire de clés associée. Elle permet également la génération du Per-Message Secret Number nécessaire afin d'effectuer une signature DSA. 

		Détails des méthodes :

		- genKeyPair() : génère une paire de clés (x,y).

		- getPublicKey() : retourne la clé publique y générée par genKeyPair ou zéro si la méthode n'a pas été appelé.

		- getPrivateKey() : retourne la clé privée x générée par genKeyPair ou zéro si la méthode n'a pas été appelé.

		- genPerMessageNumber() : génère un Per-Message Secret Number k ainsi que son inverse kInv.

		- getPerMessageNumber() : retourne le Per-Message Secret Number k généré avec genPerMessageNumber() ou zéro si la méthode n'a pas été appelé.

		- getInvPerMessageNumber() : retourne l'inverse du Per-Message Secret Number généré avec genPerMessageNumber() ou zéro si la méthode n'a pas été appelé.

	- La classe Signature ne prend rien en entrée, elle permet de signer un message et de vérifier une signature, elle contient également des méthodes pour tester la rapidité de signature/vérification.

		Détails des méthodes :

		- sign(String message) : retourne la signature associée à message.

		- sign() : retourne la signature associée à alice.txt le texte par défaut stocké dans le dossier du programme.

		- verify(String message, BigInteger r, BigInteger s, BigInteger PublicKey) : retourne true si la signature (r,s) est bien valide pour le texte message et la clé PublicKey et false sinon.

		- verify(BigInteger r, BigInteger s, BigInteger PublicKey) : retourne true si la signature (r,s) est bien valide pour alice.txt le texte par défaut stocké dans le dossier du programme et la clé PublicKey et false sinon.

		- timeSign(String message, int nombreTest): retourne le temps en secondes pour effectuer nombreTest signatures associées au texte message. 
		
		- timeSign(): retourne le temps en secondes pour effectuer 10 000 signatures sur alice.txt.

		- timeVerif(String message, int nombreTest) : retourne le temps en secondes pour effectuer nombreTest vérifications associées au texte message.

		- timeVerif() : retourne le temps en secondes pour effectuer 10 000 vérifications sur alice.txt.

	- Execution du programme :

		Au lancement il est demandé à l'utilisateur de spécifier quel mode il souhaite utiliser. Il y a 3 possibilités : signature, verification et test. 
	Si l'utilisateur choisit la signature il lui est alors demandé s'il souhaite signer le texte par défaut ( alice.txt ) ou un texte de son choix auquel cas il lui faudra indiquer le chemin pour accéder à ce texte. Le programme va ensuite afficher dans le terminal la signature (r,s) ainsi que la clé publique associée. 
	Si l'utilisateur choisit la vérification il lui est alors demandé de fournir la signature à vérifier ainsi que la clé publique associée. Il est ensuite demandé à l'utilisateur si la vérification concerne le texte par défaut ( alice.txt ) ou un texte de son choix auquel cas il lui faudra indiquer le chemin pour accéder à ce texte. 
	Enfin si l'utilisateur choisit l'option test il lui est demandé s'il souhaite effectuer le test par défaut ( 10 000 signatures puis vérifications sur alice.txt ) ou un test personnalisé. Pour le deuxième choix il est ensuite demandé à l'utilisateur d'indiquer le chemin du texte à utiliser pour le test ainsi que le nombre de signatures/vérifications à effectuer. 


- Procédure et temps mis pour effectuer 10 000 signatures/vérifications sur alice.txt : 

	Il faut lancer le programme, écrire "test" dans la console puis "oui".

