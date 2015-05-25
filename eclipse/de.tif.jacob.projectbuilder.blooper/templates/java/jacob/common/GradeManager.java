package jacob.common;

import jacob.model.Bug;

import java.util.ArrayList;
import java.util.List;

public class GradeManager 
{
	public static String[] getValidGrades(String currentGrade)
	{
	  List result = new ArrayList();
	  
  	result.add(Bug.grade_ENUM._Bug);
  	result.add(Bug.grade_ENUM._Cosmetic);
  	result.add(Bug.grade_ENUM._Crash);
  	result.add(Bug.grade_ENUM._Missing_Functionality);
  	result.add(Bug.grade_ENUM._Remark);
  	result.add(Bug.grade_ENUM._Wrong_Behaviour);
	  
	  return (String[])result.toArray(new String[0]);
	}
}
