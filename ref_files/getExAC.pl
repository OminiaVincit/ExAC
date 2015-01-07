#!/usr/bin/perl -w
use strict;
my @KEYWORD=("AC","AC_EAS","AN","AN_EAS");
my $VCF_FILE=$ARGV[0];
my $ExAC_FILE=$ARGV[1];
if(!defined($ExAC_FILE) || !defined($VCF_FILE)){ print STDERR "usage: getExAC.pl <vcf file> <ExAC file>\n"; exit; }

my %ExAC=();
while(glob("$ExAC_FILE")){
	my $in=$_;
	print STDERR "$in\n";
	open(IN, ($_ =~ /\.gz$/)? "unpigz -c $_|" : $_) || die "Error\n";
	while(<IN>){
		chomp;
		next if(/^#/);
		my ($chr,$start,$id,$ref,$alt,$qual,$filter,$info)=split(/\t/);
		my @INFO=split(/;/,$info);
		my $info_txt="$alt:";
		foreach(@INFO){
			my ($key,$value)=split(/=/);
			$info_txt.="$_," if(grep {$_ eq $key} @KEYWORD);
		}
		$info_txt=~s/,$//g;
		$ExAC{"chr$chr"}{$start}=$info_txt;
		print STDERR "chr$chr\t$start\t$info_txt\n";
	}
	close(IN);
}

open(IN, ($VCF_FILE =~ /\.gz$/)? "unpigz -c $VCF_FILE|" : $VCF_FILE) || die "Error\n";
while(<IN>){
	chomp;
	my ($chr,$pos)=split(/\t/);
	print "$_\tExAC:";
	if($ExAC{$chr}{$pos}){
		print "$ExAC{$chr}{$pos}\n";
	}
	else{
		print "-\n";
	}
}
close(IN);
