/*
 * Created on 22.03.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;
import jacob.model.Contract;
import jacob.model.Locationcontract;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LocationcontractTableRecord extends DataTableRecordEventHandler
{
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord locationContractRecord, IDataTransaction transaction) throws Exception
  {
    if (locationContractRecord.isDeleted())
      return;

    // Ist dieser Eintrag für einen aktiven Vertrag?
    IDataTableRecord contract = locationContractRecord.getLinkedRecord(Contract.NAME);
    if (contract != null && Contract.contractstatus_ENUM._aktiv.equals(contract.getValue(Contract.contractstatus)))
    {
      // Prüfen das nicht schon ein anderer aktiver Vertrag diese Ortsangebe
      // definiert hat.

      final String RELATIONSET = "r_locationcontract";

      IDataAccessor accessor = locationContractRecord.getAccessor().newAccessor();

      // Achtung: Wir berücksichtigen nur Einträge von Verträgen, welche aktiv
      // sind.
      IDataTable iContractTable = accessor.getTable(Contract.NAME);
      iContractTable.qbeSetKeyValue(Contract.contractstatus, Contract.contractstatus_ENUM._aktiv);

      IDataTable locationContractTable = accessor.getTable(Locationcontract.NAME);

      locationContractTable.qbeSetValue(Locationcontract.contract_key, "!" + contract.getValue(Contract.pkey));

      if (locationContractRecord.hasNullValue(Locationcontract.site_key))
        locationContractTable.qbeSetValue(Locationcontract.site_key, "NULL");
      else
        locationContractTable.qbeSetKeyValue(Locationcontract.site_key, locationContractRecord.getValue(Locationcontract.site_key));

      if (locationContractRecord.hasNullValue(Locationcontract.sitepart_key))
        locationContractTable.qbeSetValue(Locationcontract.sitepart_key, "NULL");
      else
        locationContractTable.qbeSetKeyValue(Locationcontract.sitepart_key, locationContractRecord.getValue(Locationcontract.sitepart_key));

      if (locationContractRecord.hasNullValue(Locationcontract.building_key))
        locationContractTable.qbeSetValue(Locationcontract.building_key, "NULL");
      else
        locationContractTable.qbeSetKeyValue(Locationcontract.building_key, locationContractRecord.getValue(Locationcontract.building_key));

      if (locationContractRecord.hasNullValue(Locationcontract.buildingpart_key))
        locationContractTable.qbeSetValue(Locationcontract.buildingpart_key, "NULL");
      else
        locationContractTable.qbeSetKeyValue(Locationcontract.buildingpart_key, locationContractRecord.getValue(Locationcontract.buildingpart_key));

      locationContractTable.search(RELATIONSET);
      if (locationContractTable.recordCount() != 0)
      {
        String msg = "Es darf für diese Ortsangabe nur einen aktiven Vertrag geben.";

        IDataTableRecord othercontract = locationContractTable.getRecord(0).getLinkedRecord(Contract.NAME);
        if (othercontract != null)
          msg += "\\r\\nDer aktive Vertrag '" + othercontract.getStringValue(Contract.description) + "' ist schon für diese Ortsangabe hinterlegt.";
        throw new BusinessException(msg);
      }
    }
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}
