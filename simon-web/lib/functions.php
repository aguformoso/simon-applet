<?php

include "config.php";

function connectDB() {
	Global $dbconn;
	Global $dbhost, $dbname, $dbuser, $dbpass;
	$str = "host=$dbhost dbname=$dbname user=$dbuser password=$dbpass";
	$dbconn = pg_connect($str) or die('Could not connect: ' . pg_last_error());
}

function queryDB($query) {
	$result = pg_query($query) or die('Query failed: ' . pg_last_error());
	return $result;
}

function countrySelection($oCC) {
	echo "<SELECT NAME=\"oCC\">\n";
	$result = queryDB("SELECT country_origin as CC, name, count(*) as QT from results, country where country_origin=iso GROUP BY country_origin, name ORDER BY name");
	while ($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
    	$cc = $line["cc"];
   	 	$txt = $line["name"];
   	 	if (strcmp($cc,$oCC)==0) {
   	 		$sel = " SELECTED";
   	 	} else {
   	 		$sel = "";
   	 	}
   	 	echo "<OPTION VALUE=\"".$cc."\" ".$sel.">".$txt;
	}
	echo "</SELECT>\n";
}

function countryTable($oCC) {
	echo "<TABLE class=rtt>\n";
	echo "<TR><TH class=rtt rowspan=2>Pais Destino<TH class=rtt colspan=4>RTT<TH class=rtt rowspan=2>Muestras</TR>\n";
	echo "<TR><TH class=rtt>Min<TH class=rtt>Med<TH class=rtt>Prom<TH class=rtt>Max</TR>\n";
	$result = queryDB("SELECT name as dcc, min(min_rtt) as min, max(max_rtt) as max, avg(ave_rtt) as avg, avg(median_rtt) as med, count(*) as samples ".
					  "FROM results,country ".
					  "WHERE iso=country_destination and country_origin='$oCC' ".
					  "GROUP BY name");
	while ($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
    	$dcc = $line["dcc"];
   	 	$min = round($line["min"],0);
   	 	$max = round($line["max"],0);
   	 	$avg = round($line["avg"],0);
   	 	$med = round($line["med"],0);
   	 	$class = "rtt_low";
   	 	if ($med>120) {
   	 		$class="rtt_med";
   	 	}
   	 	if ($med>180) {
   	 		$class="rtt_high";
   	 	}
   	 	if ($med>240) {
   	 		$class="rtt_vhigh";
   	 	}	
   	 	$samples = $line["samples"];
    	echo "<TR> <TD class=\"$class\">$dcc".
    			  "<TD class=\"$class\" align=right>$min ms".
    			  "<TD class=\"$class\" align=right>$med ms".
    			  "<TD class=\"$class\" align=right>$avg ms".
    			  "<TD class=\"$class\" align=right>$max ms".
    			  "<TD class=\"$class\" align=right>$samples</TR>\n";
    }
	echo "</TABLE>";
}

function averageRTT() {
	$result = queryDB("SELECT  min(min_rtt) as min, max(max_rtt) as max, avg(ave_rtt) as avg, avg(median_rtt) as med, count(*) as samples from results");
	while ($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
    	return  round($line["avg"],1);
	}
}



?>