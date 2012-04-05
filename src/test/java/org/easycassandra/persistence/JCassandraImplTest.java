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
		  Assert.assertNotNull(new JCassandraImpl("select * from Person"));
		  
	}
	
	
	@Test(expected=EasyCassandraException.class)
	public void erroSyntaxeSelectTest(){
		  new JCassandraImpl(" * from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroSyntaxeAllCheckEspaceTest(){
		  new JCassandraImpl("select * , name  from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroUnecessaryCommaTest(){
		  new JCassandraImpl("select *, name , from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroSyntaxeAllCheckTest(){
		 
		  new JCassandraImpl("select *, name  from Person");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void erroNeedComa(){
		 
		  new JCassandraImpl("select name age  from Person");
	}
	@Test
	public void selectVariableTest(){
		 
		  new JCassandraImpl("select name, id from Person");
	}
	
	@Test
	public void selectWhereTest(){
		 
		  new JCassandraImpl("select name, id from Person where id = :32");
	}
	
	@Test
	public void selectWhere2ConditionTest(){
		 
		  new JCassandraImpl("select name, id from Person where id = :32  ");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhere2FailConditionTest(){
		 
		  new JCassandraImpl("select name, id from Person where KEY = :32 and name =  ");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhereFailTest(){
		 
		  new JCassandraImpl("select name, id from Person where KEY  name");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void unknownColumnFamilyTest(){
		
		  new JCassandraImpl("select name, id from unknown ");
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhereUnknownColumnTest(){
		 
		  new JCassandraImpl("select unknow,name,id from Person where id =32 ");
	}
	
	@Test
	public void selectTest(){
		 
		  new JCassandraImpl("select sex,name,id from Person where id =32 ");
		  
	}
	
	@Test(expected=EasyCassandraException.class)
	public void selectWhereNotIndexOrSecundaryKeyTest(){
		 
		  new JCassandraImpl("select name, id from Person where id = :32 and name = 'Person' ");
	}
	
	
}
