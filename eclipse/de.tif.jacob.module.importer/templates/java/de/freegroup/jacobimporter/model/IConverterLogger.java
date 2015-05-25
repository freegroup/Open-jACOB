package de.freegroup.jacobimporter.model;

public interface IConverterLogger 
{
	  public String getLog();
	  public void resetLog();
	  public void log_debug(String message);
	  public void log_info(String message);
	  public void log_warn(String message);
	  public void log_error(String message);
	  public boolean hasErrors();
	  public boolean hasWarnings();
}
