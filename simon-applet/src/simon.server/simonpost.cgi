#!/opt/local/bin/perl -w

use CGI.pm;
use XML::Validator::Schema;
use XML::SAX::ParserFactory;
use XML::Simple;

my $xs = XML::Simple->new(ForceArray => 1, KeepRoot => 1);
local($xml,$value);

# First I get POST information in $xml variable.
 
$ENV{'REQUEST_METHOD'} =~ tr/a-z/A-Z/;

if ($ENV{'REQUEST_METHOD'} eq "POST")
    {
      read(STDIN,$xml,$ENV{'CONTENT_LENGTH'})|| die "Could not get query\n";
    }else {
      $xml = $ENV{'QUERY_STRING'};
    }

# Second I need to validate the XML info with the Schema.

my $schema_file = 'SimonXMLSchema.xsd';
my $validator = XML::Validator::Schema->new(file => $schema_file);
my $parser = XML::SAX::ParserFactory->parser(Handler => $validator);
eval { $parser->parse_string($xml); };
    die "File failed validation: $@" if $@;

# Here I could answer with an HTTP message if there is an error and the parcing does not pass.

# 
my $value = $xs->XMLin($xml);

# Now I need to add the fields to the Database:

#
    
print "Content-type:text/html\r\n\r\n";
print "<html>";
print "<head>";
print "<title>INFO</title>";
print "</head>";
print "<body>";
print "<h2>$value</h2>";
print "</body>";
print "</html>";
