/*
 * Created on 23.04.2004
 * 
 * Version template
 */
package de.tif.jacob.core;

import java.util.StringTokenizer;

/**
 * This class defines a software version used for jACOB applications and the
 * jACOB application server itself.
 * 
 * @author Andreas Sonntag
 * 
 * DON'T CHANGE THE "Version.java" FILE!!!!
 * Any changes will be lost during the next ant call (deployment)
 */
public final class Version implements Comparable
{
	/**
	 * The internal revision control system id.
	 */
	static public final transient String RCS_ID = "$Id: Version.template,v 1.5 2009/04/21 15:14:46 ibissw Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	static public final transient String RCS_REV = "$Revision: 1.5 $";

	/**
	 * The version of the jACOB application server.
	 */
	public static final Version ENGINE = parseVersion("3.0");

	/**
	 * The name of the jACOB application server.
	 */
	public static final String ENGINE_NAME = "OpenjACOB";
	
	/**
   * Flag whether this jACOB application server is the open version.
   */
	public static final boolean IS_OPEN_ENGINE = ENGINE_NAME.toLowerCase().startsWith("open");

	/**
	 * The version of the jACOB admin application.
	 */
	public static final Version ADMIN = new Version(ENGINE.major, ENGINE.minor, ENGINE.fix);

	private final int        major;
	private final int        minor;
	private final int        fix;
	private final String     versionString;
	private final String     versionShortString;

	/**
	 * Constructs a new version instance consisting of a major and minor version
	 * number.
	 * 
	 * @param major
	 *          major version number
	 * @param minor
	 *          minor version number
	 */
	public Version(int major, int minor)
	{
	  this(major, minor, 0);
	}

	public Version(int major, int minor, int fix)
  {
    if (major < 0)
      throw new IllegalArgumentException("Invalid major version number: " + major);
    if (minor < 0)
      throw new IllegalArgumentException("Invalid minor version number: " + minor);
    if (fix < 0)
      throw new IllegalArgumentException("Invalid fix version number: " + fix);

    this.major = major;
    this.minor = minor;
    this.fix = fix;
    this.versionShortString = major + "." + minor;
    if (fix == 0)
      this.versionString = this.versionShortString;
    else
      this.versionString = this.versionShortString + "." + fix;
  }

	/**
	 * Compares whether this version is greater or equal to a given version.
	 * 
	 * @param v the version to compare
	 * @return true if 'this' version is greater or equal 'v'
	 */
	public boolean isGreaterOrEqual(Version v)
	{
	  return this.compareTo(v) >= 0;
	}

	/**
	 * True if 'this' version is compatible with the hands over version.<br>
	 * 
	 * this=1.1 v=1.0 -&gt; false;<br>
	 * this=1.1 v=1.1 -&gt; true;<br>
	 * this=1.1 v=1.2 -&gt; true;<br>
	 * this=1.1 v=2.0 -&gt; false;<br>
	 * 
	 * @param v the version to compare compatibility
	 * @return true if 'this' version is compatible with the hands over version.
	 */
	public boolean isCompatible(Version v)
	{
	  if(major== v.major && minor<=v.minor)
	    return true;

	  return false;
	}
	
  /**
   * This version matches the given version regarding the application deploy
   * process, i.e. the versions match regarding major and minor version number.
   * The fix version number is ignored.
   * 
   * @param v
   *          the version to compare
   * @return true if the version matches regarding deploy process.
   * @since 2.8.4
   */
  public boolean isSameForDeployProcess(Version v)
  {
    return major == v.major && minor == v.minor;
  }
  
  /**
   * This version matches the given version perfectly, i.e. even the fix version
   * number matches.
   * 
   * @param v the version to compare
   * @return true if the version has a perfect match.
   */
  public boolean isPerfect(Version v)
  {
    return major == v.major && minor == v.minor && fix == v.fix;
  }
  
	/**
	 * Converts the given version string to a version instance.
	 *
	 * @param versionStr the version string
	 * @return the resulting version instance
	 */
	public static Version parseVersion(String versionStr)
	{
		try
		{
			StringTokenizer st = new StringTokenizer(versionStr, ".");
			int major = Integer.parseInt(st.nextToken());
			int minor = 0;
			int fix = 0;
			if (st.hasMoreTokens())
			{
				minor = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens())
				{
					fix = Integer.parseInt(st.nextToken());
				}
			}

			return new Version(major, minor, fix);
		}
		catch (Exception e)
		{
			// ignore
		}
		throw new RuntimeException("Invalid version: " + versionStr);
	}

	/**
	 * Returns the major version number.
	 * 
	 * @return the major version number
	 */
	public int getMajor()
	{
		return major;
	}
	
  /**
   * Returns the minor version number.
   * 
   * @return the minor version number
   */
  public int getMinor()
  {
    return minor;
  }
  
  /**
   * Returns the fix version number.
   * 
   * @return the fix version number or <code>0</code>, if respective
   *         application version is not a fix
   * @since 2.8.4
   */
  public int getFix()
  {
    return fix;
  }
  
  /**
   * Returns the version as a short string, i.e. a string consisting of major
   * and minor version number only.
   * 
   * @return the short version string
   * @since 2.8.4
   */
  public String toShortString()
  {
    return this.versionShortString;
  }

  /**
   * Returns the version as a string, e.g. "1.7.2" or "1.1". The version string
   * consists of major and minor version number and fix version number, if
   * existing.
   * 
   * @return the version string
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return this.versionString;
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject)
	{
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof Version)
		{
			Version another = (Version) anObject;
			return this.versionString.equals(another.versionString);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return this.versionString.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{
		Version other = (Version) o;
		if (this.major != other.major)
		  return this.major - other.major;
		if (this.minor != other.minor)
		  return this.minor - other.minor;
		return this.fix - other.fix;
	}
	
	/**
	 * Internal method needed by the jacapp ant task.
	 **/ 
	public static void main(String[] args)
	{
    if(args.length==0)
    {
  		System.out.println("jacob.version=" + ENGINE);
  		System.out.println("admin.version=" + ADMIN);
    }
    else
    {
      String arg0 = args[0];
      if("jacob.version".equals(arg0))
        System.out.print(ENGINE);
      else if("admin.version".equals(arg0))
        System.out.print(ADMIN);
    }
	}
}
