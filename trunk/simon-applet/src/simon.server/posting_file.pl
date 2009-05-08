#!/opt/local/bin/perl -w

use strict;
use LWP::UserAgent;
use HTTP::Request::Common;
use FileHandle;

open FILE, "../simon/XML/ExampleXML.xml" or die "Couldn't open file: $!"; 
my $fh = join("", <FILE>); 
close FILE;

print "$fh \n";

my $userAgent = LWP::UserAgent->new(agent => 'perl post');

my $response = $userAgent->request(POST 'http://localhost/cgi-bin/simonpost.cgi', 
Content_Type => 'text/xml',
Content => $fh);

if($response->code == 200) {
        print $response->as_string;
}
else {
        print $response->error_as_HTML;
}


