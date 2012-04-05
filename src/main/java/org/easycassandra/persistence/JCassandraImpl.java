package org.easycassandra.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.easycassandra.annotations.read.EnumRead;
import org.easycassandra.persistence.DescribeField.TypeField;
import org.easycassandra.util.EncodingUtil;


/**
 * Class intepreter for CQL Cassandra
 * @author otavio
 *
 */
 class JCassandraImpl implements JCassandra {  
   /**
    * Class for run the queries
    */
	private Persistence persistence;
	/**
	 * 
	 */
	private StringBuilder cql;
	
	/**
	 * information about the query 
	 */
	private InformationQuery informationCQL;
	
	/**
	 * to start the position
	 */
	private int startPosition=0;
	/**
	 * maxResult in query
	 */
	private int maxResult=-1;
	/**
	 * set Persistence in Jcassandra Class	
	 * @param persistence
	 */
	public void setPersistence(Persistence persistence) {
		this.persistence=persistence;
		
	}
	
	/**
	 * Constructor method
	 * @param expression
	 */
	JCassandraImpl(String expression) {
	   informationCQL=new InformationQuery();
	   
       
       StringTokenizer expressionTokens =  new StringTokenizer(cqlQuery(expression));
       
       //in the future inner class
       Period period=Period.BEFORE_SELECT;
    
       
       //end of inner class
       while (expressionTokens.hasMoreTokens())
       {
    	   String actualExpression=expressionTokens.nextToken();
    	   if(Period.FROM.equals(period)){
    		   informationCQL.columnFamily=actualExpression;
    		   period=Period.AFTER_FROM;
           	   continue;
              }
    	   
           switch (actualExpression) {
           
         
           
		case "select":
			period=Period.SELECT;
			break;
		case "*":
			informationCQL.allObject=true;
			break;
		case "from":
			period=Period.FROM;
			
			break;
		case "where":
			period=Period.WHERE;
			break;
		case ",":
			isNeedComma();
			break;
		case "count(*)":
			informationCQL.countMode=true;
		break;
		case "<":
		case ">":
		case "=":
		case "<=":
		case ">=":
		case "and":
		case "or":
			informationCQL.needsCondition=false;
		break;
		default:
			defaultCondition(period, actualExpression);
			
			break;
		}
           isStartCorrect(period);
        	
           
       
         
        
       }
       verifySyntax(informationCQL);
       cql=new StringBuilder(expression);
      
       
   }

	private void isNeedComma() {
		if(!informationCQL.needsComma){
		   throw new EasyCassandraException(" Syntax error: unnecessary comma ");
		}
		informationCQL.needsComma=false;
	}

	private void isStartCorrect(Period period) {
		if(Period.BEFORE_SELECT.equals(period)){
           	   throw new EasyCassandraException(" Syntax error: the CQL must begin with the select command ");
              }
	}
/**
 * Execute when enter in default option 
 * @param period
 * @param actualExpression
 */
	private void defaultCondition(Period period, String actualExpression) {
		if(Period.SELECT.equals(period)){
			if(informationCQL.needsComma){
				  throw new EasyCassandraException(" Syntax error: need separate the attributes with ',' ");	
			}
			informationCQL.variable.add(actualExpression);
			informationCQL.needsComma=true;
		}
		else if(Period.WHERE.equals(period)){
			if(informationCQL.needsCondition){
				  throw new EasyCassandraException(" Syntax error: need condiction in query ");	
			}
			
			informationCQL.addVariable(actualExpression);
			informationCQL.needsCondition=true;
		}
	}
/**
 * 
 * @param informationCQL
 */
private void verifySyntax(final InformationQuery informationCQL) {
	
	
	if(!informationCQL.iscondition){
    	   throw new EasyCassandraException(" Syntax error:  "); 
       }
       DescribeFamilyObject describeFamilyObject=EasyCassandraManager.getFamily(informationCQL.columnFamily);
      if(describeFamilyObject==null){
    	  throw new EasyCassandraException(" Syntax error: unknown Column Family "+informationCQL.columnFamily);
      }
       for(String column:informationCQL.variable){
    	 if(describeFamilyObject.getField(column)==null){
    		 throw new EasyCassandraException(" Syntax error: unknown column "+column+" in  Column Family "+informationCQL.columnFamily); 
    	 }  
       }
       
       for(String key:informationCQL.variabledMap.keySet()){
    	   DescribeField describeField=describeFamilyObject.getField(informationCQL.variabledMap.get(key).variableName);
      	 if(describeField==null){
      		 throw new EasyCassandraException(" Syntax error: unknown column "+informationCQL.variabledMap.get(key).variableName+" in  Column Family "+informationCQL.columnFamily); 
      	 }  
      	 if(TypeField.INDEX.equals(describeField.getTypeField())||TypeField.INDEX.equals(describeField.getTypeField())){
      		 throw new EasyCassandraException("The field "+describeField.getName()+" must be a key or index for be in condition"); 
      	 }
         }
}
   
   private String cqlQuery(String cql){
	  
		   StringBuilder newCql=new StringBuilder();
		   
		   int firtTime=0;
		   for(String part:cql.split(",")){
			   if(firtTime>0){
				   newCql.append(" , ");
			   }
			   if(part.contains("=")){
				   part=part.replace("=","= ");
			   }
			   newCql.append(part);
			   
			   firtTime++;
		   }
	  
	   return newCql.toString().trim();
   }
  
   /**
    * The class has information about the query
    * @author otavio
    *
    */
   class InformationQuery{
	

	/**
	    * Name of the column Family
	    */
	private   String columnFamily;
	
	/**
	 * all object checked
	 */
	private   boolean allObject;
	/**
	 * using count in select
	 */
	private boolean countMode;
	
	   /**
	    * name of the variables
	    */
	private   List<String> variable;
	   /**
	    * verify if is condition or variable
	    */
	private   boolean iscondition=true;
	/**
	 * need the character ','
	 */
	private   boolean needsComma=false;
	/**
	 * need the condition
	 */
    private  boolean needsCondition=false;
	
	   /**
	    * 
	    */
	   private VariableConditions variableConditions;
	   /**
	    * map of the variable conditions
	    * The key is name of the fields
	    */
	   private Map<String,VariableConditions> variabledMap;
	 
	 void addVariable(String value){
		 if(iscondition){
			 variableConditions.variableName=value;
		 }else{
			 variableConditions.condition=value;
			 variabledMap.put(variableConditions.variableName, variableConditions);
			 variableConditions=new VariableConditions();
		 }
		 
		
		 iscondition=!iscondition;
	 }
	   {variable=new ArrayList<>(); variabledMap=new HashMap<>();variableConditions=new VariableConditions();}
   }
   
   /**
    * 
    * @author otavio
    *
    */
   class VariableConditions{
	   /**
	    * name of the variable
	    */
	  private   String variableName;
	   /**
	    * Condition
	    */
	  private String condition;
	   

	   
	   
	   
   }
   /**
    * This enum inform period that is a query
    * @author otavio
    *
    */
   enum Period{BEFORE_SELECT,SELECT,FROM,AFTER_FROM,WHERE}
   
   
@Override
public List<?> getResultList() {
	DescribeFamilyObject describeFamilyObject= EasyCassandraManager.getFamily(informationCQL.columnFamily);
	String cqlNew = replaceToCQLName(describeFamilyObject);
	List<?> list=null;
	if(informationCQL.countMode){
		List<Long> longList=new ArrayList<>();
		longList.add(persistence.executeCommandCQL(cqlNew.toString()).rows.get(0).getColumns().get(0).value.asLongBuffer().get());
		list=longList;
	}
	else if(informationCQL.allObject){
		
        list=executeAll(describeFamilyObject, cqlNew);
	}else{
		list=executeSomeFields(describeFamilyObject, cqlNew);
	}
	
	runStartPosition(list);
	runMaxResult(list);
	 return list;
}
/**
 * run the max resulst
 * @param list
 */
private void runMaxResult(List<?> list) {
	if(maxResult==-1){
		return;	
	}
	while(list.size()>maxResult){
		list.remove(list.size()-1);
	}
}
/**
 * run the start position
 * @param list
 */
private void runStartPosition(List<?> list) {
	for(int index=0;index<startPosition;index++){
		if(index>list.size()){
			break;
		}
		list.remove(0);
	}
}
/**
 * When the query has only fields in the objects
 * @param describeFamilyObject
 * @param cql
 * @return
 */
private List<?> executeSomeFields(DescribeFamilyObject describeFamilyObject,
		String cql) {
	List<Map<String, Object>> listCql= new ArrayList<>();
	for(Map<String, String> resultSet:persistence.executeCql(cql)){
		Map<String, Object> resulMap=new HashMap<>();
		for(String key: resultSet.keySet()){
			
			
			DescribeField describeField= describeFamilyObject.getField(describeFamilyObject.getFieldsName(key));
			if(describeField.getClassField().isEnum()){
				 
                 resulMap.put(describeField.getName(),new EnumRead(describeField.getClassField()).getObjectByByte(EncodingUtil.stringToByte(resultSet.get(key))));
			}else{
			resulMap.put(describeField.getName(), Persistence.getReadManager().convert(EncodingUtil.stringToByte(resultSet.get(key)), describeField.getClassField()));
			}
		}
		listCql.add(resulMap);
	}
	return listCql;
}

/**
 * when the query is for all fields in object
 * @param describeFamilyObject
 * @param cql
 * @return
 */
private List<?> executeAll(DescribeFamilyObject describeFamilyObject, String cql) {
	try {
		return persistence.listbyQuery(persistence.executeCommandCQL(cql),describeFamilyObject.getClassFamily());
	} catch (InstantiationException | IllegalAccessException exception) {
	    Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, exception);
	}
	return new ArrayList<>();
}


@Override
public Object getSingleResult() {
	List<?> list=getResultList();
	if(list.size()>0){
	return list.get(0);
	}
	return null;
}
/**
 * replace the objects name to column's name
 * @param describeFamilyObject
 * @return cql with column's name
 */
private String replaceToCQLName(DescribeFamilyObject describeFamilyObject) {
	String aux=cql.toString().replace(describeFamilyObject.getClassFamily().getSimpleName(), describeFamilyObject.getColumnFamilyName());
	  for(String column:informationCQL.variable){
	    	 if(aux.contains(column)){
	    		 aux=aux.replace(column, describeFamilyObject.getField(column).getRealCqlName()); 
	    	 }  
	       }
	       
	       for(String key:informationCQL.variabledMap.keySet()){
	    	   DescribeField describeField=describeFamilyObject.getField(informationCQL.variabledMap.get(key).variableName);
	      	 if(aux.contains(key)){
	      		 aux=aux.replace(key, describeField.getRealCqlName()); 
	      	 }  
	       }
	return aux;
}

@Override
public void setFirstResult(int startPosition) {
isNegativeValue(startPosition);
this.startPosition=startPosition;
}
/**
 * Verify is the value is negative
 * @param startPosition
 */
private void isNegativeValue(int startPosition) {
	if(startPosition<0){
		throw new EasyCassandraException("Illegal Argument: The argument must be not a negative value");
	}
}

@Override
public void setMaxResults(int maxResult) {
	isNegativeValue(maxResult);
	this.maxResult=maxResult;
	
}


}
