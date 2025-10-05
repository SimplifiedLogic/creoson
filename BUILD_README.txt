The application is built with a series of Ant build files.

The creoson_source_doc.pdf file gives a high-level overview of how the 
source code is organized.


REQUIREMENTS

Building the project requires that you have the following before building:

1) An installation of Creo which includes the JLink libraries as part of the 
installation.  Specifically, "text/java/pfcasync.jar" under the Common Files
directory.

2) Copies of the CreosonSetup zip files which can be downloaded from 
the Releases on the github site.
https://github.com/SimplifiedLogic/creoson/releases

3) A copy of the Apache Commons Codec jar.  The current releases were built
with version 1.10 of commons-codec.jar.
https://commons.apache.org/proper/commons-codec/

4) A copy of the Jackson library for manipulating JSON data.  The current
releases were built with version 2.12.7 of jackson-core and jackson-annotations,
and 2.12.7.2 of jackson-databind.
https://github.com/FasterXML/jackson

5) An output directory to which the built jars will be written.


Before doing any builds, you must first edit "build_vars.properties" file and 
update the entries to point to the directories for these two items.


BUILD FILES

1. Build Directories

All build files are written to put files in the "out" sub-directory, and
the ones which depend on other jars in the project expect them to be in that
directory.

Certain third-party jars are needed by the application, and those are found
in the "third-party" sub-directory.


2. Build Files

There are individual build.xml files for each sub-project.  Each of them 
builds the jar file for that particular project.

In addition, the root directory contains three more build files:

build-javadoc.xml

This build file generates javadoc for all Java classes, and assembles them
into the creoson-javadoc.jar file.

build-zip.xml

This build file will create the final release ZIP files.  It will do it in the
following stages: 

a) Extract the CreosonSetup zip files so that their contents can be 
incorporated into the final release files.

b) Run the GenTemplates program to generate the JSON template files for the 
internal website.

c) Create the Release ZIP files CreosonServer-win32.zip and 
CreosonServer-win64.zip by including 
	- all the jars in the "out" directory
	- all the commons-codec and jackson jars
	- the website files
It will also create CreosonServerWithSetup-win32.zip and 
CreosonServerWithSetup-win64.zip which includes the above but
also contains
	- The CreosonSetup application, which includes an embedded JRE.

build-all.xml

This build file will delete and recreate the out directory, then execute all
of the other build files in the correct order.
