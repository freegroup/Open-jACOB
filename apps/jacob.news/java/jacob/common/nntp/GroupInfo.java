/*
 * Created on 25.08.2005
 *
 */
package jacob.common.nntp;

public class GroupInfo
{
  final String firstArticle;
  final String lastArticle;
  final int    count;
  /**
   * @param count
   * @param article
   * @param article2
   */
  public GroupInfo(int count, String firstArticle, String lastArticle)
  {
    this.count        = count;
    this.firstArticle = firstArticle;
    this.lastArticle  = lastArticle;
  }
}
