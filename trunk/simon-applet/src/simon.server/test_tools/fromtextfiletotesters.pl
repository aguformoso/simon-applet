#!/opt/local/bin/perl -w

# This script runs on cron and generates a file with the test points every X hours.
# The result is printed as a coma separated file.

use strict;
use DBI;

my $in_file='./TestPointslist.txt';
open FILE, "$in_file" or die $!;

my $dbh = DBI->connect("DBI:Pg:dbname=simon;host=localhost", "simon", "bolivar", {'RaiseError' => 1});

my $testtype="ntp";
my $enabled="t";
my $date_created="2009-05-08";

while (my $line=<FILE>) {
		$line =~ s/\n//g ;
        my @fields=split(/,/,$line);
		my $description=$fields[0];
		my $country=$fields[1];
		my $ip_address=$fields[2];
		print @fields;
		print "$description,$testtype,$ip_address,$country,$enabled,$date_created \n";
		my $rows = $dbh->do( qq[ INSERT INTO test_points (description, testtype, ip_address, country, enabled, date_created) VALUES ('$description','$testtype','$ip_address','$country','$enabled','$date_created') ] );
}
$dbh->disconnect();
close (FILE);