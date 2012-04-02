package org.easycassandra.persistence;

import junit.framework.Assert;

import org.easycassandra.bean.model.Person;
import org.junit.BeforeClass;
import org.junit.Test;

public class JCassandraImplTest {
	
@BeforeClass	
public static void init(){
	EasyCassandraManager.addFamilyObject(Person.class);
}
	
	@Test
	public void correctSyntaxTest(){
		  JCassandraImpl dvdInterpreterClient =  new JCassandraImpl();
		  Assert.assertTrue(dvdInterpreterClient.interpret("select * from Person"));
		  
	}
	
	
	@Test(expected=EasyCassandraException.class)
	public void erroSyntaxeSelectTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret(" * from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroSyntaxeAllCheckEspaceTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select * , name  from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroUnecessaryCommaTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select *, name , from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroSyntaxeAllCheckTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select *, name  from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroNeedComa(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name age  from Person");
	}
	@Test
	public void selectVariableTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name, id from Person");
	}
	
	@Test
	public void selectWhereTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name, id from Person where id = :32");
	}
	
	@Test
	public void selectWhere2ConditionTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name, id from Person where id = :32 and name = 'Person' ");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhere2FailConditionTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name, id from Person where KEY = :32 and name =  ");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhereFailTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name, id from Person where KEY  name");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void unknownColumnFamilyTest(){
		 JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select name, id from unknown ");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhereUnknownColumnTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  cassandraInterpreterClient.interpret("select unknow,name,id from Person where id =32 ");
	}
	
	@Test
	public void selectTest(){
		  JCassandraImpl cassandraInterpreterClient =  new JCassandraImpl();
		  Assert.assertTrue(cassandraInterpreterClient.interpret("select sex,name,id from Person where id =32 "));
		  
	}
	
}
