<?php 
include "header.php"; 
?>
<!-- <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAMCjdHlLjIFjB5ejHEttX0BSr45LnWunmqAIRIsNmYi3ZFpMd-hTGumnWQsNdD4PTa8xim7SLE3YvuQ" type="text/javascript"></script> -->
<script 
 src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAMCjdHlLjIFjB5ejHEttX0BRz2sXhdxBqZn6YRHo9CqG4-ZEOZRS8hV3OoeOzzJ1RncA4WcPGe7jBlw" 
 type="text/javascript">
</script>
<script type="text/javascript">
    function initialize() {
      if (GBrowserIsCompatible()) {
         var map = new GMap2(document.getElementById("map_canvas"),{ size: new GSize(375,525) } );
         map.setCenter(new GLatLng(-22, -67), 3);
         geoXml = new GGeoXml("http://www.google.com/maps/ms?ie=UTF8&e=ForceExperiment&hl=en&vps=2&jsv=159e&msa=0&output=nl&msid=118261029966754648798.00044486c00971df30602");
    	 map.addOverlay(geoXml);
       }
    }
</script>

<!-- <body onload="initialize()" onunload="GUnload()"> -->
<body>

<?php
showTop();
showMenu("reports.php");

$oCC = $_GET["oCC"];
?>
<h2>Reportes</h2>
<p>Este reporte intenta indicar cuales son los parametros de latencia (RTT), desde un pais de origen determinado, hacia el resto de la region.
<p>Las muetras consideradas fueron obtenidas por la contribucion de cientos de usuarios en cada uno de los paises.
<HR>
<FORM METHOD=get ACTION=?>
<LABEL>Pais de Origen:</LABEL>
<?php countrySelection($oCC); ?>
<INPUT type="submit" value="Submit Form" />
</FORM>

<?php
if (strlen($oCC)>0) {
?>
<HR>
<TABLE width=100%><TR>
<TD valign=top>
<?php countryTable($oCC); ?>
<TD valign=top>
<div id="map_canvas" style="width: 300; height: 550"></div>
</TR></TABLE>
<?php
}
?>

<?php include "footer.php"; ?>
