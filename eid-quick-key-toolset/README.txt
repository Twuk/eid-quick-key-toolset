README for FedICT Quick-Key Toolset Project
================================

=== 1. Introduction

This project contains the source code tree of the FedICT Quick-Key Toolset project.
The source code is hosted at: http://code.google.com/p/eid-quick-key-toolset/
For portability and compatibility reasons the source code also contains external libraries.
In the eid-applet folder of the project a stripped version of the Java 1.4.1_07 SDK is included 
to enable correct Java Card 2.2.1 compilation. This is also the case for the Java Card 2.2.1 
libraries included in the source files of this folder. The specific licenses for these libraries 
can be found in the respective folders.
In the eid-toolset source folder both the Global Platform version 2.1.1 and GPShell 
version 1.4.2 libraries and files are included to enable a correct conversion and upload of 
the eID java card applet. The corresponding licenses can also be found there.
Note: under Linux it is up to the user to install GPShell (found on sourceforge.net) if he want to 
upload the empty eID applet to a smart card.


=== 2. Requirements

The following is required for compiling the eID Applet and Engine software:
* Sun Java Development Kit (JDK) 1.6.0_16+
* Apache Maven 3.0.2

For running the compiled code under Unix/Mac OS systems, GPShell 1.4.2+ should be 
installed and 'gpshell' should be available in the command path.


=== 3. Build

The project can be build via:
	mvn clean install


=== 4. Eclipse IDE

The Eclipse project files can be created via:
	mvn eclipse:eclipse

Afterwards simply import the projects in Eclipse via:
	File -> Import... -> General:Existing Projects into Workspace

First time you use an Eclipse workspace you might need to add the maven 
repository location. Do this via:
    mvn eclipse:add-maven-repo -Declipse.workspace=<location of your workspace>


=== 5. NetBeans IDE

As of NetBeans version 6.7 this free IDE from Sun has native Maven 2 support.


=== 6. License

The license conditions can be found in the file: LICENSE.txt
For the external libraries used, the licenses can be found in the specific folders,
as said in 1.
