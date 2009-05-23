<?php 
include "header.php"; 
?>
<body>
<?php
showTop();
showMenu("reports.php");
?>

<h2>Prueba la Latencia desde tu pais</h2>

<APPLET code="simon.client.latency.Applet.class" 
		archive="../jar/simon-applet.jar" 
		width="640" height="500">
    Este browser no soporta Java 1.5.
    
</APPLET>

<?php include "footer.php"; ?>
