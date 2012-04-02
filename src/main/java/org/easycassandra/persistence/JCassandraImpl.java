package org.easycassandra.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Class intepreter for CQL Cassandra
 * @author otavio
 *
 */
public class JCassandraImpl {  
    
	
   public boolean interpret(String expression) {
	   InformationQuery informationCQL=new InformationQuery();
       
       StringTokenizer expressionTokens =  new StringTokenizer(cqlQuery(expression));
       
       //in the future inner class
       Period period=Period.BEFORE_SELECT;
       boolean needsComma=false;
       boolean needsCondition=false;
       
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
			if(!needsComma){
			   throw new EasyCassandraException(" Syntax error: unnecessary comma ");
			}
			needsComma=false;
		case "count":
		break;
		case "<":
		case ">":
		case "=":
		case "<=":
		case ">=":
		case "and":
		case "or":
		needsCondition=false;
		break;
		default:
			if(Period.SELECT.equals(period)){
				if(needsComma){
					  throw new EasyCassandraException(" Syntax error: need separate the attributes with ',' ");	
				}
				informationCQL.variable.add(actualExpression);
				needsComma=true;
			}
			else if(Period.WHERE.equals(period)){
				if(needsCondition){
					  throw new EasyCassandraException(" Syntax error: need condiction in query ");	
				}
				
				informationCQL.addVariable(actualExpression);
				needsCondition=true;
			}
			
			break;
		}
           //in the future methods trates excpecionts
           if(Period.BEFORE_SELECT.equals(period)){
        	   throw new EasyCassandraException(" Syntax error: the CQL must begin with the select command ");
           }
        
       }


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
      	 if(describeFamilyObject.getField(informationCQL.variabledMap.get(key).variableName)==null){
      		 throw new EasyCassandraException(" Syntax error: unknown column "+informationCQL.variabledMap.get(key).variableName+" in  Column Family "+informationCQL.columnFamily); 
      	 }  
         }
       return true;
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
	  
	   return newCql.toString().trim().toLowerCase();
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
	private   boolean allObject;
	   /**
	    * name of the variables
	    */
	private   List<String> variable;
	   /**
	    * verify if is condition or variable
	    */
	private   boolean iscondition=true;
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
}
