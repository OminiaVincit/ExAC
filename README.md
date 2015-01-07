ExAC
=========

# ExAC Overview

* This project is developed for importing ExAC data to pipeline.
* Check SNV positions from each sample already registered in ExAC's VCF file or not. 

# About ExAC database

* Broad Institute of MIT. Link: http://exac.broadinstitute.org/
* 61,486 people (included 4,378 Asian) Exome data published on October, 2014 (version 0.2)
* 9,462,741 variation places
* Filter: DP >= 10 & GQ >= 20
* 2.9GB (gzip)
aaaaaaaaaaaaaa
# The VCF specification (v4.1)

* https://github.com/OminiaVincit/ExAC/blob/master/ref_files/VCFv4.1.pdf

# Sample program (getExAC.pl)

* Read VCFfile for each line
* Import chr, pos, alt, AC, AC_EAS, AN, AN_EAS to hash-table (key: chr, pos)
* From each sample, get SNV positions VCF file. Read this file for each line and search in ExAC hash table with (chr, pos) key. If search item is hitted, then add alt:AC, AC_EAS, AN, AN_EAS to VCF file.
* Problem: it takes about 15 mins to read ExAC VCF file (on RAM disk)

# Improvements

## Modify ExAC data
* Extract file with necessary columns only
* Add index, parallel processing for read/write

## Improve program
* Parallel processing for perl thread
* Implement by C, C++, Java

## Use Java 

# Development plan

* Create small sample ExAC VCF file  
* Read/write this small sample file, import to hash-table (Java)
* Import to pipeline & test

# 5000 people Exome data (different projects)


