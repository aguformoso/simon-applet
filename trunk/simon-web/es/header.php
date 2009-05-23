<!-- header.php -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html templateref="file:///Users/jmguzman/Downloads/Internet_Services/index.html" xmlns="http://www.w3.org/1999/xhtml">
<?php

include "../lib/functions.php"; 
connectDB();

?>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">	<meta name="author" content="Luka Cvrk (www.solucija.com)" />
	<meta http-equiv="content-type" content="text/html;charset=iso-8859-1" />
	<link rel="stylesheet" href="../images/style.css" type="text/css" />
	<title>Proyecto Simon</title>
</head>
<?php
function showtop() {
?>
	<div class="content">
		<div class="header">
			<div class="top_info">
				<div class="top_info_right">
					Midiendo la conectividad entre paises <br>.. para poder mejorar la Internet de latino&aacute;merica.
				</div>		
				<div class="top_info_left">
					<p><b><?php echo date("F j, Y, g:i a"); ?></b><br />
				</div>
			</div>			
			<div class="logo">
				<h1><a href="index.php" title="Projecto Simon">Proyecto<span class="dark">Sim&oacute;n</span></a></h1><br>
			</div>
<?php
}
?>

<?php
	function menuOption($text, $page, $currentPage) {
		if ($page == $currentPage) {
			echo "				<li class=\"active\">".$text."</li>\n";
		} else {
			echo "				<li><a href=\"".$page."\">".$text."</a></li>\n";
		}
	}
	
	function showMenu($currentPage) {
?>
		<div class="bar">
			  <ul>
				<li class="browse_category">Categorias:</li>	
<?php
		menuOption("Inicio","index.php", $currentPage);
		menuOption("Objetivos","objetives.php", $currentPage);
		menuOption("Latencia","latency.php", $currentPage);
		menuOption("Reportes","reports.php", $currentPage);
		menuOption("Equipo","team.php", $currentPage);
		menuOption("Glosario","glossary.php", $currentPage);
		menuOption("Contacto","contact.php", $currentPage);
?>
			  </ul>
			</div>
			<div class="search_field">
			<form method="post" action="?">
				<div class="search_form">
				<p>Latencia promedio en latino&aacute;merica: <a href="reports.php"><?php echo averageRTT(); ?> ms</a> (<a href="glossary.php#rtt">RTT</a>)
				</div>
			</form>
			<p><a href="applet.php">Prueba la latencia </a> desde tu pais!</p>
			</div>
		
		<!-- Content starts here  -->
<?php		
	}
?>		
	
	