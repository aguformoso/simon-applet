#!/opt/local/bin/perl -w

# This is the server CGI that analyze the POSTED XML file and fills the database.
# Todo: Improve error messages and checks.

use strict;
use GD::Graph::histogram;
use DBI;
use POSIX;

my (@data,@countries_or,@countries_dest,$id,$name);

# Setting up the connection to the database.
my $dbh = DBI->connect("DBI:Pg:dbname=simon;host=localhost", "simon", "bolivar", {'RaiseError' => 1});

# I first look for all country codes:
my $query="select distinct country_origin from results";
my $sth1 = $dbh->prepare($query);
$sth1->execute();
my $i=0;
while (@data = $sth1->fetchrow_array()) {
            $countries_or[$i] = $data[0];
            $i++;
   }
$sth1->finish;


$query="select distinct country_destination from results";
my $sth2 = $dbh->prepare($query);
$sth2->execute();
$i=0;
while (@data = $sth2->fetchrow_array()) {
            $countries_dest[$i] = $data[0];
            $i++;
}
$sth1->finish;
$sth2->finish;
my @countries = (@countries_or, @countries_dest);
#Unique country.
my %seen=() ;
my @unique_country = grep { ! $seen{$_}++ } @countries ;

my (@data_min,@data_max,@data_ave,@data_med);
my ($data_min_ref,$data_max_ref,$data_med_ref,$data_ave_ref);
my ($gd_min,$gd_max,$gd_med,$gd_ave);

unlink (glob("*histogram_*")) or warn "can't delete files: $!"; 


foreach my $cc1 (@unique_country) {
	foreach my $cc2 (@unique_country) {
		my $graph_min = new GD::Graph::histogram(2000,1000);
		my $graph_med = new GD::Graph::histogram(2000,1000);
		my $graph_ave = new GD::Graph::histogram(2000,1000);
		my $graph_max = new GD::Graph::histogram(2000,1000);
	 	$graph_min->set( 
                x_label         => 'Delay Min',
                y_label         => 'Count',
                x_label_position => '0.5',
                y_label_position => '0.5',
                title           => "$cc1 - $cc2",
                x_labels_vertical => 1,
                bar_spacing     => 0,
                shadow_depth    => 1,
                shadowclr       => 'dred',
                transparent     => 0,
                histogram_bins => 30,
                histogram_type => 'percentage'   
        ) 
        or warn $graph_min->error;
     $graph_min->set_title_font('Arial.ttf', 24);
     $graph_min->set_x_label_font('Arial.ttf', 18);
     $graph_min->set_y_label_font('Arial.ttf', 18);
     $graph_min->set_x_axis_font('Arial.ttf', 11);
     $graph_min->set_y_axis_font('Arial.ttf', 11);

	 	$graph_med->set( 
                x_label         => 'Delay Median',
                y_label         => 'Count',
                x_label_position => '0.5',
                y_label_position => '0.5',
                title           => "$cc1 - $cc2",
                x_labels_vertical => 1,
                bar_spacing     => 0,
                shadow_depth    => 1,
                shadowclr       => 'dred',
                transparent     => 0,
                histogram_bins => 30,
                histogram_type => 'percentage'   
                          
        ) 
        or warn $graph_med->error;
     $graph_med->set_title_font('Arial.ttf', 24);
     $graph_med->set_x_label_font('Arial.ttf', 18);
     $graph_med->set_y_label_font('Arial.ttf', 18);
     $graph_med->set_x_axis_font('Arial.ttf', 11);
     $graph_med->set_y_axis_font('Arial.ttf', 11);
     
     	 	$graph_max->set( 
                x_label         => 'Delay Maximum',
                y_label         => 'Count',
                x_label_position => '0.5',
                y_label_position => '0.5',
                title           => "$cc1 - $cc2",
                x_labels_vertical => 1,
                bar_spacing     => 0,
                shadow_depth    => 1,
                shadowclr       => 'dred',
                transparent     => 0,
                histogram_bins => 30,
                histogram_type => 'percentage'   
                          
        ) 
        or warn $graph_max->error;
     $graph_max->set_title_font('Arial.ttf', 24);
     $graph_max->set_x_label_font('Arial.ttf', 18);
     $graph_max->set_y_label_font('Arial.ttf', 18);
     $graph_max->set_x_axis_font('Arial.ttf', 11);
     $graph_max->set_y_axis_font('Arial.ttf', 11);
     
     	 	$graph_ave->set( 
                x_label         => 'Delay Average',
                y_label         => 'Count',
                x_label_position => '0.5',
                y_label_position => '0.5',
                title           => "$cc1 - $cc2",
                x_labels_vertical => 1,
                bar_spacing     => 0,
                shadow_depth    => 1,
                shadowclr       => 'dred',
                transparent     => 0,
                histogram_bins => 30,
                histogram_type => 'percentage'   
                          
        ) 
        or warn $graph_ave->error;
     $graph_ave->set_title_font('Arial.ttf', 24);
     $graph_ave->set_x_label_font('Arial.ttf', 18);
     $graph_ave->set_y_label_font('Arial.ttf', 18);
     $graph_ave->set_x_axis_font('Arial.ttf', 11);
     $graph_ave->set_y_axis_font('Arial.ttf', 11);

        my $sth3=$dbh->prepare( qq[ select min_rtt,max_rtt,median_rtt,ave_rtt from results where (((country_origin='$cc1' and country_destination='$cc2') or (country_origin='$cc2' and country_destination='$cc1')) and min_rtt<1000) ] );
    	$sth3->execute();
    	$i=0;
		while (@data = $sth3->fetchrow_array()) {
            $data_min[$i] = floor($data[0]);
            $data_max[$i] = floor($data[1]);
			$data_med[$i] = floor($data[2]);
            $data_ave[$i] = floor($data[3]);
            $i++;
		}
		$sth3 -> finish();
		$data_min_ref = \@data_min;
        $gd_min = $graph_min->plot($data_min_ref) or die $graph_min->error;
 		if (!-e "histogram_min_$cc1$cc2.png")
		{ 
			open(IMG, ">histogram_min_$cc1$cc2.png") or die $!;
	   		binmode IMG;
	     	print IMG $gd_min->png;
     		close IMG;
		}
		$data_med_ref = \@data_med;
        $gd_med = $graph_med->plot($data_med_ref) or die $graph_med->error;
 		if (!-e "histogram_med_$cc1$cc2.png")
		{ 
			open(IMG, ">histogram_med_$cc1$cc2.png") or die $!;
	   		binmode IMG;
	     	print IMG $gd_med->png;
     		close IMG;
		}
				$data_max_ref = \@data_max;
        $gd_max = $graph_max->plot($data_max_ref) or die $graph_max->error;
 		if (!-e "histogram_max_$cc1$cc2.png")
		{ 
			open(IMG, ">histogram_max_$cc1$cc2.png") or die $!;
	   		binmode IMG;
	     	print IMG $gd_max->png;
     		close IMG;
		}
				$data_ave_ref = \@data_ave;
        $gd_ave = $graph_ave->plot($data_ave_ref) or die $graph_ave->error;
 		if (!-e "histogram_ave_$cc1$cc2.png")
		{ 
			open(IMG, ">histogram_ave_$cc1$cc2.png") or die $!;
	   		binmode IMG;
	     	print IMG $gd_ave->png;
     		close IMG;
		}
		undef @data;
		undef @data_min;
		undef @data_med;
		undef @data_ave;
		undef @data_max;
	}
} 

$dbh -> disconnect;