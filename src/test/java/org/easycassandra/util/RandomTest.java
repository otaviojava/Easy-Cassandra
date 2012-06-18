package org.easycassandra.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

public class RandomTest {

	
	@Test
	public void randomTest(){
		List<String> string=new ArrayList<String>();
		string.add("olá 1");
		string.add("olá 2");
		string.add("olá 3");
		Random random=new Random();
		
		for(int index=0;index<10;index++){
			System.out.println(string.get(random.nextInt(string.size())));
		}
		Assert.assertTrue(true);
	}
}
