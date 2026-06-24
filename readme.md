Pour lancer l'application, vous devez exécuter la commande suivante dans le répertoire racine du projet :

En émission java -jar target\artemis-xml-app-1.0.0.jar send data/info.xml
Le nombre de messags émis est dans 
XmlProducer
final int maxMessages = 20;

En réception java -jar target\artemis-xml-app-1.0.0.jar receive xx 
Reçoit jusqu'à XX messages et s'arrête ensuite.

Voir aussi d'autres infos dans le dossier data