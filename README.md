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

### Configuring and Running a Cluster ###
* New members will request configuration from a locator
* Locator will distribute the configuration to new servers joining the cluster. 
* The locators store the configurations in a **hidden region** that is available to all locators and 
also **write the configuration data to disk as XML files**.
* `start locator --name=locator1`
* `start server --name=server1 --groups=group1`
* `start server --name=server2 --groups=group1 --server-port=40405`
* `start server --name=server3 --server-port=40406`
* `create region --name=region1 --groups=group1 --type=REPLICATE`
  * region1 is created on all cache servers that specified the group named group1
* `create region --name=region2 --type=REPLICATE`
  * region2 is created on all members because no group was specified.
* `export cluster-configuration --zip-file-name=myClConfig.zip`
  * create a zip file that contains the cluster’s persisted configuration.
* `shutdown --include-locators=true`, and exit gfsh.
* Now if we navigate to another directory, start gfsh,
* `start locator --name=locator2 --port=10335`
* `import cluster-configuration --zip-file-name=myClConfig.zip`
* `start server --name=server4 --server-port=40414`
* `start server --name=server5 --groups=group1 --server-port=40415`
* region1 will be automatically created in server5(group1).
  region2 will be automatically created in server4 and server5.

&nbsp;

&nbsp;
----
### Useful links ###
* []()