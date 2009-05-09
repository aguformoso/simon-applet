#!/opt/local/bin/perl -w

# This script runs on cron and generates a file with the test points every 12 hours.
# The result is printed as a coma separated file that can be download by the applet.

use strict;
use DBI;

my $out_file='./testpoints.txt';
open FILE, ">$out_file" or die $!;

my $dbh = DBI->connect("DBI:Pg:dbname=simon;host=localhost", "simon", "bolivar", {'RaiseError' => 1});

my $sth = $dbh->prepare("SELECT * FROM test_points where enabled");
$sth->execute();

# iterate through resultset
# print values
while(my $ref = $sth->fetchrow_hashref()) {
    print FILE "$ref->{'testpointid'},$ref->{'description'},$ref->{'testtype'},$ref->{'ip_address'},$ref->{'country'},$ref->{'date_created'}\n";
}

# clean up
$dbh->disconnect();
close (FILE);