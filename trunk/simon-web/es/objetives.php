<?php 
include "header.php"; 
?>
<body>
<?php
showTop();
showMenu("objetives.php");
?>
		
<h2>Motivaci&oacute;n</h2>
<p>Desde hace años se viene escuchando de las limitaciones de infrastructura de comunicaciones en Latino America.. especificamente en conectividad entre paises. 
Mientras la conectividad entre paises ha tenido un enorme avance en los ultimos años, 
reforzada con la aparición de muchos puntos de intercambio de tráfico (IXPs) en casi todos los 
paises de la región, la situación de la conectividad entre paises sigue siendo un misterio.

<p>Algunas pruebas simples, y no exhaustivas, por lo que no pueden considerarse aun representativas, 
indican que incluso entre paises vecinos en nivel de conectividad es muy limitado, con latencias 
del orden de 100-300ms, dependiendo de los proveedores de backbone que se utilicen.
<center>
<!--
<table border=1 cellspacing=0>
<tr><th>Destino<th>RTT(ms)</tr>
<tr><td>Argentina<td>30-<font color=red>275</font>ms</tr>
<tr><td>Brasil<td><font color=red>162</font> ms</tr>
<tr><td>Bolivia<td>137 ms</tr>
<tr><td>Colombia<td>92-<font color=red>192</font> ms</tr>
<tr><td>Ecuador<td>158-<font color=red>204</font> ms</tr>
<tr><td>Paraguay<td><font color=red>151</font> ms</tr>
<tr><td>Uruguay<td><font color=red>254</font> ms</tr>
<tr><td>USA<td>130 ms</tr>
</table>
-->
<img src="../images/rtt-table.png" width=198 heigth=341/>
</center>

<p>(*) <i>Pruebas de Santiago, Chile, usando pings hacia distintos periodicos electronicos, y promediando resultados.</i>
<br/>
<p>La pregunta que surge, es si esto es un caso particular o si desde otros paises se observan 
   comportamientos similares, pero lamentablemente hasta este momento no se han realizado estudios 
   en esta area.

<p>La principal razon para estas latencias tan elevadas es la falta de interconexion local entre 
   los distintos proveedores de backbone, o simplemente la falta de infrastructura entre paises, 
   lo que obliga a que el trafico entre paises vecinos algunas veces sea intercambiado en USA.

<h3>Cual es el problema</h3>
<p>En la medida que los paises estan fragmentados, se introducen una serie de limitaciones que afectan el desarrollo de la Internet Regiomal
<ul>
<li>- Hay poco intercambio de trafico local.. y asi seguira siendo hasta que haya mas capacidad entre paises
<li>- Se desfavorece la implementacion de aplicaciones altamente interactivas en la region (hosteando estas aplicaciones en USA introduce mucha latencia)
<li>- No existen incentivos para construccion de infrastructura regional (porque supuestamente no hay trafico)
<li>- Las aplicaciones P2P consumen mucho ancho de banda en el "long-haul", en lugar de hacerlo localmente a un costo menor.
<li>- Se desfavorece los acuerdos de intercambio de trafico local (peering regional)
</ul>
<br/>
<h2>El Proyecto Simon</h2>
<P>El proyecto Simon busca entregar a las empresas que estan en condiciones de invertir en la región, antecedes que permitan sustentar planes de negocio para desarrollar la internet regional
<P>Creemos que midiento los niveles de latencia que hoy se tienen entre paises, y estimando los volumenes de trafico que estan sujetos a estas latencias, podria:
<ul>
<li>- Motivar a proveedores de backbone, a realizar interconexiones locales (a costo muy bajo)
<li>- Alimentar estudios de inversión de empresas de telecomunicaciones que deseen desarrollar negocios de transporte de trafico regional.
</ul> 
<P>Con ese objetivo se establece el Proyecto Simon, como un esfuerzo conjunto, colaborativo y 
   abierto a toda la comunidad, y que permita organizar los esfuerzos de conocer la situacion de 
   la internet regional, y apoyar a las empresas que deseen ayudar a su resolucion.

<h3>Entregables</h3>
<P>El proyecto nace con 2 objetivos concretos, que constituyen los entregables de la iniciativa:

<ul>
<li>1) <B>Matriz de Latencia entre paises</B><BR/>
       Proporcionar una matriz que indique el valor promedio, y desviacion estandard, entre cualquier par de paises de la region.
       <img src="../images/rtt-matrix.png">
       
<li>2) <B>Matriz de Trafico entre paises</B><BR/>
       Proporcionar una matriz que estime el volumen de trafico/ancho de banda, entre cualquier par de paises de la region.
       <img src="../images/traffic-matrix.png">
       
</ul> 

<?php include "footer.php"; ?>
