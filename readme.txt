Easy-Cassandra

Easily programming with this framework, the Easy Cassandra.
The EasyCassandra uses the Thrift implementation and has as main objective to be a simple ORM( Object relational manager). It needs the jdk 7 to run, because some parts in his code are using invoke dynamic instead reflection. Due to it, it will have a faster behavior than other frameworks.

Version: 1.1.2
* use different keyspace with schema

Version: 1.1.1
* when you insert or merge you don't need to cast to your object
* refactoring messages errors

Version: 1.1.0
* Client for many Client in sequencial mode
* Client for many Client in sequencial random
* DownGrade to java 6
* create keyspace automatically
* do single client

Version: 1.0.9

* Supporting at jpa 2.0 annotation
* Query with objects
* Version to retrieve the timestamp of the object
* Using the JPQL

Version: 1.0.8
* Select Key from 'in'  CQL command
* Create automatically the ColumnFamily in Run Time
* Create automatically the IndexValue in Run Time
* Suport with cql 2.0


Version: 1.0.7
* update cassandra-thrift to 1.0.7

Version: 1.0.6
* Fixes bug with File
* Support Calendar interface

Version: 1.0.5
* Can now store files
* Support java.io.File and java.nio.file.Path

Version: 1.0.4
* more performance
* less memory
* now, all primitives types are supported 
* now, Byte, character, Short, BigInteger and BigDecimal are supported 

Version: 1.0.3
* Fixes bug with result
* update for Thrift 1.0.6
* Log now using java.util.loggin

Version: 1.0.2
* Fixes bug with Boolean's Object
* Now the Cassandra's lib is supported. In this way is possible to use every Cassandra above of the version 0.8.0

Version: 1.0.1
Modifications
* Allowed use ColumnValue and ColumnFamilyValue in default mode. In this way, it gets the field's name
* Fixes bug in Reflection
* Auto-Increment saved in XML Document