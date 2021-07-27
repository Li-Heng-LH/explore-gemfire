# explore-gemfire

## Some Learning Notes ##

### Main Concepts and Components ###
* _Cache_: a node
* _Regions_: tables. 
  * within each cache
  * name/value pairs
  * replicated region: stores identical copies of the data on each cache member of a distributed system
  * partitioned region:  spreads the data among cache members
  * client applications can access the distributed data in regions without knowledge of the underlying system architecture
* _Locators_: for member discovery and load-balancing
  * we configure clients with a list of locators
  * locators maintain a dynamic list of member servers
* _Server_: a Pivotal GemFire process that runs as a long-lived, configurable member of a cluster
  * **used primarily for hosting regions**

&nbsp;

### gfsh ###
* Tutorial steps to start Gemfire system: 
  * `gfsh`
  * `start locator --name=locator1`
  * `start pulse`
  * `start server --name=server1 --server-port=40411`
  * `create region --name=regionA --type=REPLICATE_PERSISTENT`  Note that the region is hosted on server1.
* Check status
  * `list regions`  --> regionA
  * `list members`  --> locator1, server1
  * `describe region --name=regionA`
* Manipulate data in the region
  * In most applications, a Java program adds, deletes and modifies stored data. 
  * `put --region=regionA --key="1" --value="one"`
  * `put --region=regionA --key="2" --value="two"`
  * `query --query="select * from /regionA"`
* Demonstrate persistence
  * After `put` in the above step, Disk Store Files are created in server1
  * _BACKUPDEFAULT_1.crf_: data from persistent region, Default disk store name, for create, update, and invalidate operations, 
  Pre-allocated 90% of the total max-oplog-size
  * _BACKUPDEFAULT_1.drf_: data from persistent region, Default disk store name, for delete operations, 
  Pre-allocated 10% of the total max-oplog-size
  * `stop server --name=server1`
  * `start server --name=server1 --server-port=40411`
  * `query --query="select * from /regionA"`  --> should still see data in regionA
* Examine the effects of replication
  * `start server --name=server2 --server-port=40412`
  * `describe region --name=regionA`  --> regionA is hosted on both server1 and server2
  * When gfsh starts a server, it requests the configuration from the _cluster configuration service_ 
  which then distributes the shared configuration to any new servers joining the cluster.
  * `put --region=regionA --key="3" --value="three"`
  * `query --query="select * from /regionA"`
  * `stop server --name=server1`
  * `put --region=regionA --key="4" --value="four"`
  * `stop server --name=server2`  --> so now, data 4 is only on server2
* We need servers to host regions!!  
  Now, if we run  
  * `describe region --name=regionA`  --> Error: No member found for executing function : org.apache.geode.management.internal.cli.functions.GetRegionDescriptionFunction
  * `query --query="select * from /regionA"` --> false: Cannot find regions in any of the members
* Restart the cache servers in parallel
  * `start server --name=server1 --server-port=40411`  --> it needs data from the other server to start and waits for the server to start
  * In another terminal, `gfsh`
  * In another terminal, `connect --locator=localhost[10334]`  --> connect to locator1
  * In another terminal, `start server --name=server2 --server-port=40412`  --> start servers in parallel
* `shutdown --include-locators=true`

&nbsp;


&nbsp;
----
### Useful links ###
* []()