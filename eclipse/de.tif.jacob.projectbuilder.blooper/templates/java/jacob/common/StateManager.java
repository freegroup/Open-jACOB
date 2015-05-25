package jacob.common;

import jacob.model.Bug;

import java.util.ArrayList;
import java.util.List;

public class StateManager 
{
	public static String[] getValidStates(String currentState)
	{
	  List result = new ArrayList();
	  
	  if(currentState.equals(Bug.state_ENUM._open))
	  {
	  	result.add(Bug.state_ENUM._open);
	  	result.add(Bug.state_ENUM._verified);
	  	result.add(Bug.state_ENUM._closed);
	  }
	  else if(currentState.equals(Bug.state_ENUM._verified))
	  {
	  	result.add(Bug.state_ENUM._verified);
	  	result.add(Bug.state_ENUM._closed);
	  }
	  else if(currentState.equals(Bug.state_ENUM._closed))
	  {
	  	result.add(Bug.state_ENUM._closed);
	  }
	  
	  return (String[])result.toArray(new String[0]);
	}
}
