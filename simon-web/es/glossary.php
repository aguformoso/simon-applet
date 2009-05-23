<?php 
include "header.php"; 
?>
<body>
<?php
showTop();
showMenu("glossary.php");
?>


		
<h2>Glosario</h2>
<ul>
<li><b><a name="applet">Applet Java</a></b>: Es un componente de software escrito en Java, que puede ser ejecutado al interior del Browser. En este caso, usamos un Applet Java para realizar pruebas de latencia desde el browser de gran cantidad de usuarios.

<li><b><a name="latencia">Latencia</a></b>: Es el tiempo que tarda un paquete en viajar por la red, hasta el destino final. Es uno de los indicadores que define la calidad de la red. A menor latencia, mejor calidad de red.

<li><b><a name="peering">Peering</a></b>: Es una interconexion de dos (peering bilateral) o mas (peering multilateral) redes, con el fin de que intercambien trafico en forma directa, mas expedita, con mejor matencia, y con mejor control.

<li><b><a name="rtt">RTT</a></b>: Round-Trip-Time, es el tiempo de ida y vuelta de un paquete entre dos puntos, y la medida mas utilizada para expresar latencia (en milisegundos). Este caso, estamos interesados en saber el RTT entre dos paises (Pais1 -> Pais2 -> Pais2)

</ul>

<?php include "footer.php"; ?>
