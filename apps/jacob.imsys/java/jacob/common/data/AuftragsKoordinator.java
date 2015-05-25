/*
 * Created on Jun 21, 2004
 *
 */
package jacob.common.data;

/**
 *
 */
public class AuftragsKoordinator
{
  String name;
  String routingInfo;
  String pkey;
  String description;
  
  /**
   * @param name
   * @param routingInfo
   * @param pkey
   */
  public AuftragsKoordinator( String pkey,String name,String description, String routingInfo)
  {
    this.pkey = pkey;
    this.name = name;
    this.description = description;
    this.routingInfo = routingInfo;
  }
  /**
   * @return Returns the name.
   */
  public final String getName()
  {
    return name;
  }

  /**
   * @return Returns the pkey.
   */
  public final String getPkey()
  {
    return pkey;
  }

  /**
   * @return Returns the description.
   */
  public final String getDescription()
  {
    return description;
  }
  /**
   * @return Returns the routingInfo.
   */
  public final String getRoutingInfo()
  {
    return routingInfo;
  }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject) {
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof AuftragsKoordinator)
		{
			AuftragsKoordinator another = (AuftragsKoordinator) anObject;
			
			return this.pkey.equals(another.pkey);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.pkey.hashCode();
	}
}
