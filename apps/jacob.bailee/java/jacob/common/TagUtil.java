package jacob.common;
public class TagUtil
{
  public static String strip(String input)
  {
    input = input.toLowerCase();
    // alle Sonderzeichen erstmal entfernen
    input = input.replaceAll("[^a-z0-9 ]", "");
    
    // zu kurze Strings erstmal entfernen
    input = input.replaceAll("( [a-z0-9]{0,1} )", " ");
    input = input.replaceAll("( [a-z0-9]{0,2} )", " ");

    input = input.replaceAll("[ ]{2,}", " ");
    return input;
  }
  
  public static void main(String[] args)
  {
    System.out.println(strip("[Fwd: bento jACOB query mockup]"));
  }
}
