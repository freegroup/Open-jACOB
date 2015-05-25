package jacob.common.masterdetail;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;

public abstract class MasterDetailManagerConfiguration
{
    public abstract String getPropagationRelationset();
    public abstract String getDetailContainer();
    public abstract String getMasterList();
    public abstract String getButtonUpdate();
    public abstract String getButtonNew();
    public abstract String getButtonDelete();
    public abstract String getButtonUp();
    public abstract String getButtonDown();
    public abstract String getButtonTop();
    public abstract String getForeignAlias();
    public abstract String getDetailAlias();
    public abstract String getSortField();
    public abstract String getFkey();
    public abstract String getPkey();
    public abstract String getTransactionProperty();

    /**
     * Returns <code>true</code> if at least one record is required; the last
     * record cannot be deleted if so.
     *
     * @return <code>true</code> if at least one record is required, or
     *         <code>false</code> otherwise.
     * @since 17 Nov 2009
     */
    public abstract boolean isRequired();

    public void onDetailBackfill(IClientContext context, IDataTableRecord selectedRecord) throws Exception
    {
    }
}
