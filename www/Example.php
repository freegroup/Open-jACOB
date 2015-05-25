<?php

/////////////////////////////////////////////////////////////////////
// 
//  Dieses Beispiel-PHP-Skript soll Sie bei Ihren ersten Schritten
//  mit PHP-Skripten unterstützen.
//
//  Sie rufen es aus Ihrem Browser auf mit
//	http://www.openjacob.org/cgi-bin/Example.php?mailto=webmaster@openjacob.org
//  (natürlich können Sie auch eine andere mailadresse angeben, an die Sie
//  die Testmail senden wollen)
//
// BITTE ERSETZEN SIE UNTEN XXXX1XXXX durch ihr MySQL-Passwort!
//----------------------------------------------------------------
// So arbeiten Sie mit PHP auf unseren Premium Paketen:
// 1. CGI Parameter einlesen
// 2. e-mail versenden
// 3. Auf Ihre mysql Datenbank zugreifen
//
/////////////////////////////////////////////////////////////////////
//	VIEL SPASS!
/////////////////////////////////////////////////////////////////////

$user= 'U387269AA'; 		// Der User-Name (Kennung) Ihres Premium-Pakets
$password= 'XXXX1XXXX'; 	// Passwort für mysql-Zugriff.
				// XXXX1XXXX ist natürlich falsch:
				// Sie müssen es durch Ihr mysql-Passwort ersetzen.
				// Solange Sie es nicht ändern, ist Ihr mysql-Passwort
				// identisch mit dem Passwort, das Ihnen mit dem Authentifizierungsschreiben
				// zum Einloggen in den Kundenservice mitgeteilt wurde.

echo "<html>";
echo "<head>";
echo "<title>Test Skript ", __FILE__, "</title>";
echo "</head>";
echo "<body>";

echo "<h1>Test Skript ", __FILE__," gestartet am ", date("D M H:i:s"), "</h1>\n";

///////////////////////
// CGI Variablen ausgeben

echo "<h2>Alle CGI Parameter anzeigen</h2>\n";

$url = parse_url($REQUEST_URI);
echo "$url[query]";

///////////////////////
// Mail versenden mit PHP

echo "<h2>e-mail versenden</h2>\n";

if($mailto){
  $from = "webmaster@openjacob.org <webmaster@openjacob.org>";
  $subject = "test";
  $body = "Meine erste Testmail aus openjacob.org.\n";

  // jetzt noch eine Meldung auf die HTML-Seite schreiben

  echo "Mail versendet:<br>";
  echo "An: ", $mailto, "<br>";
  echo "Von: ", $from, "<br>";
  echo "Betreff: ", $subject, "<br>";

  mail($mailto, $subject, $body, "FROM: $from\n");
}else{
  echo "Um die Mailfunktion zu aktivieren müssen Sie dieses Skript mit dem Parameter mailto aufrufen!<br>\n";
  echo "z.B.: <a href='http://www.openjacob.org$REQUEST_URI?mailto=webmaster@openjacob.org'>";
  echo "http://www.openjacob.org$REQUEST_URI?mailto=webmaster@openjacob.org</a><p>";
  echo "(bitte beachten Sie, dass bei IDN-Domains die E-Mail immer an einen Empf&auml;nger mit dem technischen<br>
	 Domainnamen geschickt wird - unabh&auml;ig davon, was in Ihrem E-Mail-Programm angezeigt wird)\n";
}

///////////////////////
//MySQL Zugriff mit PHP

echo "<h2>Zugriff auf die MySQL Datenbank</h2>\n";

// $database= "U387269AA";	# Ihre Datenbank hat denselben Namen, wie Ihr Unix/FTP/SSH User!

$database = "test";

// Da Ihre Datenbank möglicherweise noch keine Tabellen enthält,
// benutzen wir die Datenbank "test", die allen Kunden mit lesendem Zugriff zur Verfügung steht.
// Wenn Sie $database= "test" auskommentieren und in Ihrer Datenbank U387269AA eine Tabelle "beispiel"
// anlegen (kleingeschrieben!), läuft dieses Programm auch auf Ihrer Datenbank U387269AA.

$host = "db.openjacob.org";

if ($password == 'XXXX1XXXX') {
  echo "Sie m&uuml;ssen Ihr Password erst in dieses Skript eintragen,";
  echo " da sonst die Verbindung zur Datenbank nicht hergestellt werden kann!<p>\n";
}

$dbverbindung = mysql_connect($host,$user,$password) or
    die ("Verbindung zur Datenbank konnte nicht hergestellt werden");
  mysql_select_db($database, $dbverbindung) or
    die ("Die Datenbank $database konnte nicht selektiert werden!");

$sql = "SELECT * FROM beispiel";
$ergebnis =  mysql_query($sql, $dbverbindung) or
  die (mysql_error());

echo "$sql<br>\n";
echo "Wir geben jetzt alle Zeilen aus:<p>";

echo "<table border='1'>\n";
echo "<tr>\n";
echo "<th>name</th><th>geburtsort</th><th>lebte</th>\n";
echo "</tr>\n";

while($row = mysql_fetch_object($ergebnis)) {
  echo "<tr>\n";
  echo "<td>", $row->name, "</td>\n";
  echo "<td>", $row->geburtsort, "</td>\n";
  echo "<td>", $row->lebte, "</td>\n";
  echo "</tr>\n";
}
echo "</table>\n";  

echo "</body>";
echo "</html>";

?>
