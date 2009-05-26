<?php 
include "header.php"; 
?>
<body>
<?php
showTop();
showMenu("reports.php");
?>

<h2>Prueba la Latencia desde tu pais</h2>

<p>El proyecto Simon es un esfuerzo colaborativo para recopilar informacion desde toda la region, que nos permita construir un mapa de la Internet Regional.
<p>Para ello, nos apoyamos en los usuarios finales y/o proveedores de Internet que deseen colaborar probando la latencia desde sus paises.
<p><b>Para iniciar la prueba, haz click <a href="applet2.php">aqui</a></b>.

<h3>Nota</h3>
<p>Aun no tenemos un certificado digital para el applet, lo que hace que el Applet pida una confirmacion para poder ejecutarla.
Cuando le sea solicitado,presione [Run], [Ejecutar] o [Trust].
<CENTER>
<TABLE>
<TR><TD><IMG SRC="../images/warn_win.png" WIDTH=219 HEIGHT=135></TD>
    <TD><IMG SRC="../images/warn_mac.png" WIDTH=279 HEIGHT=107></TD>
</TR>
</TABLE>
</CENTER>

<h3>Como funciona la prueba?</h3>
En construccion...


<h3>Confidencialidad y Manejo de información</h3>
<p>La unica información suministrada es la de latencia obtenida mediante estas pruebas.. nada más es reportado (toda tu información sigue siendo privada).
<p>Todo es manejado en forma anomima.

<?php include "footer.php"; ?>
