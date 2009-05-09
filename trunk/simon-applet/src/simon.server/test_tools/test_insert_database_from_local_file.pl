#!/opt/local/bin/perl -w

use XML::Validator::Schema;
use XML::SAX::ParserFactory;
use XML::Simple;
use Data::Dumper;
use POSIX;
use DBI;

# You also need: Bundle::DBD::Pg

my $date = strftime "%d %m %Y", localtime;
@campos=split(/ /,$date);
my $day=$campos[0];
my $month=$campos[1];
my $year=$campos[2];

# Luego voy a sustituir esto por el POST

# I need to validate the XML that I received with the XML Schema.

my $schema_file = '../simon/XML/SimonXMLSchema.xsd';
my $validator = XML::Validator::Schema->new(file => $schema_file);
my $parser = XML::SAX::ParserFactory->parser(Handler => $validator);

# Evaluo archivo XML- tengo que cambiarlo a parse_string cuando sea una variable.

eval { $parser->parse_uri('../simon/XML/ExampleXML.xml') };
    die "File failed validation: ops!" if $@;
  # Aqui mando mensaje de error de vuelta.
  
my $ref = XMLin('../simon/XML/ExampleXML.xml');

# Adding fields to database.
# Setting up the connection to the database.
my $dbh = DBI->connect("DBI:Pg:dbname=simon;host=localhost", "simon", "bolivar", {'RaiseError' => 1});

# I need to go through all the test performed and do an insert for each row.
#execute INSERT query

# Generic info:
my $date_test= $ref->{date} . " " . $ref->{time};
my $version= $ref->{version};
my $ip_origin= $ref->{local_ip};
my $country_origin="UY";
my ($ip_destination,$testtype,$number_probe,$min_rtt,$max_rtt,$ave_rtt,$dev_rtt,$packet_loss,$country_destination);

foreach my $test (@{$ref->{test}}) {
  $ip_destination= $test->{destination_ip};
  $testtype= $test->{testtype};
  $number_probe= $test->{number_probes};
  $min_rtt=$test->{min_rtt};
  $max_rtt=$test->{max_rtt};
  $ave_rtt=$test->{ave_rtt};
  $dev_rtt=$test->{dev_rtt};
  $packet_loss=$test->{packet_loss};
  $country_destination=getCountryDestination($ip_destination);

# Now I add the results to the database:

  my $rows = $dbh->do( qq[ INSERT INTO results (date_test,version,ip_origin,ip_destination,testtype,number_probes,min_rtt,max_rtt,ave_rtt,dev_rtt,packet_loss,country_origin,country_destination) VALUES ('$date_test','$version','$ip_origin','$ip_destination','$testtype','$number_probe','$min_rtt','$max_rtt','$ave_rtt','$dev_rtt','$packet_loss','$country_origin','$country_destination') ] );
}
$dbh->disconnect();


#sub getCountryOrigin{
#
#}

sub getCountryDestination{
# I get the Destination Country by checking the country in the testpoints database.
  $ip=$_[0];
  $country = $dbh -> selectrow_array( qq[ SELECT country FROM test_points WHERE ip_address='$ip' ] );
  return $country
}