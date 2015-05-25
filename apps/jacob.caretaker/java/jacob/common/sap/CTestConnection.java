package jacob.common.sap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.ParameterList;

import de.tif.jacob.core.exception.UserException;

public class CTestConnection
{
  // ----------------------------------------------------------------------------
  /*
   * verbotener Konstruktor
   */
  private CTestConnection()
  {
    // NIX
  }

  public static String TestCon()
  {

    String sMES = "SAP - Fehler in Verbindung";


    try
    {
      ConnManager oCon = new ConnManager();
      oCon.done();
      oCon = new ConnManager();
      final JCO.Pool oPool = JCO.getClientPoolManager().getPool(oCon.getPoolName());
      if (null == oPool)
        throw new UserException("Pool '" + oCon.getPoolName() + "' is lost.");

      final IRepository oRepos = JCO.createRepository("_repos", oCon.getPoolName());
      final IFunctionTemplate oFctTempl = oRepos.getFunctionTemplate("GET_SYSTEM_NAME");
      final JCO.Function oFct = oFctTempl.getFunction();
      final JCO.Client oClient = JCO.getClient(oCon.getPoolName());
      try
      {
        oClient.execute(oFct);
        final ParameterList oOutParamLst = oFct.getExportParameterList();
        sMES = ("VERBUNDEN MIT: " + oOutParamLst.getField("SYSTEM_NAME").getValue().toString());

      }

      catch (Exception e1)
      {
        // TODO Auto-generated catch block
        
        e1.printStackTrace();
        return e1.getMessage();

      }
      finally
      {
        if (null != oClient)
          JCO.releaseClient(oClient);
      }
    }
    catch (UserException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return e.getMessage();
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return e.getMessage();
    }
    return sMES;

  }
}
