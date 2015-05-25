/*
 * Created on 22.03.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;
import jacob.model.Contract;
import jacob.model.Faplisbuilding;
import jacob.model.Faplisbuildingpart;
import jacob.model.Faplissite;
import jacob.model.Faplissitepart;
import jacob.model.Locationcontract;

import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
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
public class ContractTableRecord extends DataTableRecordEventHandler
{
  private static class LocationEntry
  {
    private final IDataTableRecord locationcontractRecord;

    private LocationEntry(IDataTableRecord locationcontractRecord)
    {
      this.locationcontractRecord = locationcontractRecord;
    }

    public int hashCode()
    {
      try
      {
        return 7 * this.locationcontractRecord.getSaveStringValue(Locationcontract.site_key).hashCode() //
            + 5 * this.locationcontractRecord.getSaveStringValue(Locationcontract.sitepart_key).hashCode() //
            + 3 * this.locationcontractRecord.getSaveStringValue(Locationcontract.building_key).hashCode() //
            + 1 * this.locationcontractRecord.getSaveStringValue(Locationcontract.buildingpart_key).hashCode();
      }
      catch (RuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        // should not occur
        throw new RuntimeException(ex);
      }
    }

    public String toString()
    {
      StringBuffer buffer = new StringBuffer();
      try
      {
        IDataTableRecord contract = this.locationcontractRecord.getLinkedRecord(Contract.NAME);
        if (contract == null)
          buffer.append(contract);
        else
          buffer.append(contract.getStringValue(Contract.description));

        buffer.append("|");

        IDataTableRecord site = this.locationcontractRecord.getLinkedRecord(Faplissite.NAME);
        if (site == null)
          buffer.append(site);
        else
          buffer.append(site.getStringValue(Faplissite.name));

        buffer.append("|");

        IDataTableRecord sitepart = this.locationcontractRecord.getLinkedRecord(Faplissitepart.NAME);
        if (sitepart == null)
          buffer.append(sitepart);
        else
          buffer.append(sitepart.getStringValue(Faplissitepart.name));

        buffer.append("|");

        IDataTableRecord building = this.locationcontractRecord.getLinkedRecord(Faplisbuilding.NAME);
        if (building == null)
          buffer.append(building);
        else
          buffer.append(building.getStringValue(Faplisbuilding.name));

        buffer.append("|");

        IDataTableRecord buildingpart = this.locationcontractRecord.getLinkedRecord(Faplisbuildingpart.NAME);
        if (buildingpart == null)
          buffer.append(buildingpart);
        else
          buffer.append(buildingpart.getStringValue(Faplisbuilding.name));
      }
      catch (RuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        // should not occur
        throw new RuntimeException(ex);
      }

      return buffer.toString();
    }

    public boolean equals(Object obj)
    {
      LocationEntry other = (LocationEntry) obj;

      try
      {
        return this.locationcontractRecord.getSaveStringValue(Locationcontract.site_key).equals(other.locationcontractRecord.getSaveStringValue(Locationcontract.site_key)) //
            && this.locationcontractRecord.getSaveStringValue(Locationcontract.sitepart_key).equals(other.locationcontractRecord.getSaveStringValue(Locationcontract.sitepart_key)) //
            && this.locationcontractRecord.getSaveStringValue(Locationcontract.building_key).equals(other.locationcontractRecord.getSaveStringValue(Locationcontract.building_key)) //
            && this.locationcontractRecord.getSaveStringValue(Locationcontract.buildingpart_key).equals(other.locationcontractRecord.getSaveStringValue(Locationcontract.buildingpart_key));
      }
      catch (RuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        // should not occur
        throw new RuntimeException(ex);
      }
    }
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
      return;

    // Wurde der Vertrag auf aktiv geändert?
    if (tableRecord.hasChangedValue(Contract.contractstatus) && Contract.contractstatus_ENUM._aktiv.equals(tableRecord.getValue(Contract.contractstatus)))
    {
      // Prüfen das nicht schon ein anderer aktiver Vertrag diese Ortsangebe
      // definiert hat.

      IDataAccessor accessor = tableRecord.getAccessor().newAccessor();

      IDataTable locationContractTable = accessor.getTable(Locationcontract.NAME);
      locationContractTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
      locationContractTable.qbeSetKeyValue(Locationcontract.contract_key, tableRecord.getValue(Contract.pkey));
      locationContractTable.search();
      if (locationContractTable.recordCount() > 0)
      {
        // Locations unseres Vertrages aufsammeln
        //
        Set owncontractlocations = new HashSet();
        for (int i = 0; i < locationContractTable.recordCount(); i++)
        {
          owncontractlocations.add(new LocationEntry(locationContractTable.getRecord(i)));
        }

        String msg = "Dieser Vertrag kann nicht aktiviert werden, da ansonsten doppeldeutige Vertragszuordnungen entstehen."//
            + "\\r\\rFolgende Vertragszuordnungen von aktiven Verträgen existieren bereits:";

        // Die Locations der übrigen aktiven Verträge prüfen
        //
        locationContractTable.qbeClear();
        locationContractTable.qbeSetValue(Locationcontract.contract_key, "!" + tableRecord.getValue(Contract.pkey));
        IDataTable iContractTable = accessor.getTable(Contract.NAME);
        iContractTable.qbeSetKeyValue(Contract.contractstatus, Contract.contractstatus_ENUM._aktiv);
        final String RELATIONSET = "r_locationcontract";
        locationContractTable.search(RELATIONSET);
        int found = 0;
        for (int i = 0; i < locationContractTable.recordCount(); i++)
        {
          LocationEntry locationentry = new LocationEntry(locationContractTable.getRecord(i));
          if (owncontractlocations.contains(locationentry))
          {
            found++;
            if (found > 5)
            {
              msg += "\\r\\n  ..";
              break;
            }
            msg += "\\r\\n  " + locationentry.toString();
          }
        }

        if (found > 0)
          throw new BusinessException(msg);
      }
    }
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}
