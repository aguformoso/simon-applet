<?php 
include "header.php"; 
?>
<body>
<?php
showTop();
showMenu("index.php");
?>
		<div class="left">
			<h3>Novedades:</h3>
			<div class="left_box">
				<h2>Proyecto Sim&oacute;n se presenta en LACNIC XII</h2>
				<a href="http://lacnic.net/en/eventos/lacnicxii/"><img src="http://lacnic.net/imgs/lxii/homepage.jpg" class="image" alt="LACNIC XII" width=90 height=90 border=0/></a> 
				<p>El proyecto Simon fue presentado en <a href="http://lacnic.net/en/eventos/lacnicxii/">LACNIC XII</a>, la reuni&oacute;n de Internet mas importante de la region.
				<p>En la presentacion se explicaron los objetivos del proyecto, y se invito a los asistentes a participar en este proyecto abierto y colaborativo, que busca entender
				la situaci&oacute;n de calidad de la Internet en Latinoam&eacute;rica, y con ello impulsar el desarrollo de una mejor conectividad en la regi&oacute;n.
				
			</div>
			<div class="left_box">
				<!-- <img src="images/bigimage.gif" class="image" alt="Big Image" /> -->
				<h2>Se crea el comit&eacute; del proyecto Sim&oacute;n</h2>
				<p>Con el objetivo de formar un grupo que gestione y administre el proyeto Simon, 
				   se forma un comit&eacute; regional, conformado por representamte de diversas 
				   organizaciones y empresas de la regi&oacute;n. 
				<p>Para ver quienes forman parte de este proyecto, ingresa <a href="equipo.php">aqui</a>.
				<p>Si tu tambien quieres participar, <a href="contacto.php">cont&aacute;ctanos</a>.
				
			</div>
			
			
			<h3>Reporte Latencia</h3>	
			<a href="reports.php"><img src="../images/histogram.png" class="image" alt="Big Image" border=0 /></a>
			<p>Analiza los <a href="reports.php">reportes de latencia</a> que hemos preparado a partir de la informacion 
			   recolectada por cientos de usuarios, desde distintos puntos de la region.

			<h3>Quieres Participar?</h3>
			<div class="left_box">
				<p>La forma mas simple de participar, es ayudandonos a recolectar informaci&oacute;n de latencia desde tu pa&iacute;s. Para ello, solo debes ejecutar el <a href="applet.php">Applet de prueba</a>.
				<p>Si deseas participar mas a fondo en el proyecto, <a href="contacto.php">escr&iacute;benos</a> y con gusto te sumaremos a esta iniciativa.
			</div>
		</div>	
		<div class="right">
			<h3>Prueba tu Latencia!</h3>
			<div class="right_articles">
				<b>Quieres ayudar <a href="applet.php">probando la latencia </a>desde tu pais?</b><br />
				
				<p><a href="applet.php"><img src="../images/wave.jpg" alt="Java Aplet" title="Applet" class="image" border=0 width=55 height=100></a>

				Ejecuta nuestro <a href="applet2.php">applet de prueba</a>, y podras ver cuando tardan tus paquetes hacia los distintos paises de la region, 
				y de paso estaras ayudando a construir el reporte regional de latencia.
				</p>
			</div>
			<h3>Lecturas Sugeridas:</h3>
			<div class="right_articles">
				<p><a href="http://es.wikipedia.org/wiki/Latencia"><b>Latencia</b></a><br/>Definici&oacute;n de Latencia (wikipedia.org)</p>
			</div>
			<div class="right_articles">
				<p><a href="http://es.wikipedia.org/wiki/Peering"><b>Peering</b></a><br/>Definici&oacute;n de Peering (wikipedia.org)</p>
			</div>
			<div class="right_articles">
				<p><a href="http://en.wikipedia.org/wiki/Internet_exchange"><b>Internet Exchange Points</b></a><br/>Definici&oacute;n de Puntos de Intercambio de Trafico (wikipedia.org)</p>
			</div>
			
			
			
		</div>	

<?php include "footer.php"; ?>
