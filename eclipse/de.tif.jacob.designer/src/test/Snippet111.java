/*
 * Created on 23.11.2004
 *
 */
package test;

import java.io.File;

public class Snippet111 
{

	public static void main(String[] args) 
	{
	  byte[] target = new byte[256];
	  File file = new File("/home/andherz/workspace/etr/javadoc.xml");
	  System.out.println(file.getName());
	  String name = file.getName();
	  name.getBytes();
	  
	  System.arraycopy(name.getBytes(),0,target,0,Math.min(name.getBytes().length, target.length));
	  
	  System.out.println(new String(target));
	}
}
