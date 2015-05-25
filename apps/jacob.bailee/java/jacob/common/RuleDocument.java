package jacob.common;

public class RuleDocument
{
  public final byte[] xml;
  public final String pkey;
  
  public RuleDocument(String pkey, byte[] xml)
  {
    this.xml = xml;
    this.pkey = pkey;
  }
}
