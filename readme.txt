Easy-Cassandra

Easily programming with this framework, the Easy Cassandra
The EasyCassandra uses the Thrift implementation and has like the main objective be one simple ORM( Object relational manager). It need the jdk 7 for run, because some parts in your code was replaced reflection for invoke dynamic. So will have a behavior faster than other framework.

Version: 1.1.0

* Downgrade to java 6


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
* now is supported all primitives types
* now is supported Byte, character, Short, BigInteger and BigDecimal

Version: 1.0.3
* Fixes bug with result
* update for Thrift 1.0.6
* Log now using java.util.loggin

Version: 1.0.2
* Fixes bug with Boolean's Object
* Now the Cassandra's lib is supported this way is possible use every Cassandra above of the version 0.8.0

Version: 1.0.1
Modifications
* Allowed use ColumnValue and ColumnFamilyValue in default mode this way its get the field's name
* Fixes bug in Reflection
* Auto-Increment saved in XML Document