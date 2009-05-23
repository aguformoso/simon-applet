#!/opt/local/bin/perl -w

# This is the server CGI that analyze the POSTED XML file and fills the database.
# Todo: Improve error messages and checks.

use strict;
use CGI;
use XML::Validator::Schema;
use XML::SAX::ParserFactory;
use XML::Simple;
use Data::Dumper;
use DBI;

my $xs = XML::Simple->new(ForceArray => 1, KeepRoot => 1);
my $xml;

# First I get POST information in $xml variable.
 
$ENV{'REQUEST_METHOD'} =~ tr/a-z/A-Z/;

if ($ENV{'REQUEST_METHOD'} eq "POST")
    {
      read(STDIN,$xml,$ENV{'CONTENT_LENGTH'})|| die "Could not get query\n";
    }else {
      $xml = $ENV{'QUERY_STRING'};
    }

# Second I need to validate the XML info with the Schema.

# Luego voy a sustituir esto por el POST

# I need to validate the XML that I received with the XML Schema.

my $schema_file = 'SimonXMLSchema.xsd';
my $validator = XML::Validator::Schema->new(file => $schema_file);
my $parser = XML::SAX::ParserFactory->parser(Handler => $validator);
my $validation_error_message = "Status: 400 Bad Request\nContent-type: text/html\n\n<HTML><HEAD><TITLE>XML does not validate</TITLE></HEAD><BODY></BODY></HTML>";
my $successful_message = "Status: 200 OK\nContent-type: text/html\n\n<HTML><HEAD><TITLE>Success</TITLE></HEAD></HTML>";


eval { $parser->parse_string($xml) };
 # Here I send the Error code 400 if the XML file does not validates and quit the script. 
  if ($@) 
    {
    print $validation_error_message;
    die "Validation Failed $xml" if $@;
    }

  
# If it validates I continue.
# I pass the XML info to a hash

my $ref = XMLin($xml);

# Adding fields to database.
# Setting up the connection to the database.
my $dbh = DBI->connect("DBI:Pg:dbname=simon;host=localhost", "simon", "bolivar", {'RaiseError' => 1});

# I need to go through all the test performed and do an insert for each row.
#execute INSERT query

# Generic info:
my $date_test= $ref->{date} . " " . $ref->{time};
my $version= $ref->{version};
# The remote IP I get it from the IP that is connecting to the server.
my $ip_origin= $ENV{'REMOTE_ADDR'};
my $country_origin=$ref->{local_country};;
my ($ip_destination,$testtype,$number_probe,$min_rtt,$max_rtt,$ave_rtt,$dev_rtt,$median_rtt,$packet_loss,$country_destination);

foreach my $test (@{$ref->{test}}) {
  $ip_destination= $test->{destination_ip};
  $testtype= $test->{testtype};
  $number_probe= $test->{number_probes};
  $min_rtt=$test->{min_rtt};
  $max_rtt=$test->{max_rtt};
  $ave_rtt=$test->{ave_rtt};
  $dev_rtt=$test->{dev_rtt};
  $median_rtt=$test->{median_rtt};
  $packet_loss=$test->{packet_loss};
  $country_destination=getCountryDestination($ip_destination);
# Now I add the record to the database only if the destination exists on the testpoint table.
  if ($country_destination) 
   {
  my $rows = $dbh->do( qq[ INSERT INTO results (date_test,version,ip_origin,ip_destination,testtype,number_probes,min_rtt,max_rtt,ave_rtt,dev_rtt,median_rtt,packet_loss,country_origin,country_destination) VALUES ('$date_test','$version','$ip_origin','$ip_destination','$testtype','$number_probe','$min_rtt','$max_rtt','$ave_rtt','$dev_rtt','$median_rtt','$packet_loss','$country_origin','$country_destination') ] );
   }
}
print $successful_message;
$dbh->disconnect();

sub getCountryDestination{
# I get the Destination Country by checking the country in the testpoints database.
 my $ip=$_[0];
 my $country = $dbh -> selectrow_array( qq[ SELECT country FROM test_points WHERE ip_address='$ip' ] );
 return $country
}