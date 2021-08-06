# explore-gemfire
Walk through of the course: [Pluralsight: Pivotal GemFire Developer](https://www.pluralsight.com/courses/gemfire-developer-pivotal)

## Some Learning Notes from Course ##

### Cache ###
* A Java process. 
* Consists of regions (tables/distinct data sets).

&nbsp;

### Region ###
* Groups cached objects into namespaces
* Implements `java.util.Map`

&nbsp;

### Locator ###
* Enable server to discover other servers
* Enable client to locate servers hosting data
* Each member knows that the locator is found in `<host address>:<port number>`
* and that, can be configured by:
  * using `gemfire.properties`
  * gfsh: `--locators=`

&nbsp;

### Distributed system and membership ###
* A member, is simply a process that has created a connection (joined) to the system. 
* A distributed system is made up of 1 or more members that have been configured to communicate cache events with each other,   
  forming a single logical system. 
* In the context of client/server system, clients are not part of the distributed system.   
  They access the distributed system to get the data. 

&nbsp;

### How the client-locator-server system works ###
* Locators are started first. 
* Servers start, and contact the locators to register, send address and load information.
* Clients are configured to know where locator is.
* Clients request server host id and port number. Locator returns the address of the server. 
* **Client will then communicate directly with the server**. 

&nbsp;

### gfsh ###
* Need to specify name to start locator. Those in `[]` are optional flags.
* Locator is a network entity, so it listens to a specific port. Default port is 10334. 
* Similarly, need to specify name to start server. 
* The first thing a server does when it starts is to connect to a locator.   
  **By default, it looks for a locator on that default port**. 
* Server is also a network entity, so it listens to a specific port also. It has default port also.  
  If port is set to 0, **locator** will choose port dynamically for server.
* Similarly, need to specify name to create a region. 
* Discovery mechanism of members: 
  * recommend TCP/IP
  * instead of mcast

&nbsp;

### Regions in details ###
* Replicated Regions
  * a server will store the complete data set of region 
  * and replicates entire region to other members
  * Data sets are contained entirely in every JVM
  * High speed of data read and write access
  * Suitable for reference data, or small data that does not change much
  * Suitable for many-to-many relationships data.
  * Suitable for look-up table kind of data.
  * More suitable for read access data.
  * Suitable for static/reference/lookup data.
  * Cannot horizontally scale.
  * More rare case
* Partitioned Regions
  * Division of data into **buckets** across cache members
  * Suitable for very large data sets
  * provides horizontal scalability
  * Good for 1-to-many or many-to-1 relationships
  * Suitable for dynamic data.
  * There are still ways to support data failover and high availability.
  * Suitable for data that will keep being written (keep growing)
  * Suitable for 100's of GB
  * Most typical case

&nbsp;

&nbsp;
----
### Useful links ###
* [Pluralsight: Pivotal GemFire Developer](https://www.pluralsight.com/courses/gemfire-developer-pivotal)
