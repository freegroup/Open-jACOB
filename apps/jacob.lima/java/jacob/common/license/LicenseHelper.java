package jacob.common.license;

import jacob.event.data.LicenseTableRecord;
import jacob.model.Engine;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.license.EncryptionUtil;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;

public class LicenseHelper
{
  private LicenseHelper()
  {
    // to avoid instantiation
  }

  /**
   * Callback to execute specific actions after private key has been loaded.
   * 
   * @author Andreas Sonntag
   */
  public static interface LoadCallback
  {
    void afterLoad(IClientContext context) throws Exception;
  }

  private static class UploadPrivateKeyCallback implements IOkCancelDialogCallback
  {
    private static final String VALIDATION_TEXT = "the quick brown fox jumps ...";

    private final IDataTableRecord engineRecord;
    private final LoadCallback callback;

    private UploadPrivateKeyCallback(IDataTableRecord engineRecord, LoadCallback callback)
    {
      this.engineRecord = engineRecord;
      this.callback = callback;
    }

    public void onOk(IClientContext context) throws Exception
    {
      context.createUploadDialog(new IUploadDialogCallback()
      {
        public void onOk(IClientContext context, String fileName, byte[] privateKey) throws Exception
        {
          // validate whether the uploaded private key is the right one
          //
          String signature = LicenseGenerator.signMessage(VALIDATION_TEXT, privateKey);
          EncryptionUtil encrytor = new EncryptionUtil();
          if (!encrytor.verify(VALIDATION_TEXT, signature, EncryptionUtil.makePublicKey(engineRecord.getBytesValue(Engine.public_key))))
            throw new UserException(new ApplicationMessage("LicenseHelper.WrongPrivateKey"));

          // store key as user property
          LicenseTableRecord.setPrivateKey(engineRecord.getlongValue(Engine.pkey), privateKey);

          // call callback last
          callback.afterLoad(context);
        }

        public void onCancel(IClientContext context) throws Exception
        {
        }
      }).show();
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }
  }

  /**
   * Uploads a private key if necessary by means of requesting the user. 
   * @param context the current client context
   * @param engineRecord the record of the engine to upload private key for
   * @param callback callback to execute after private key has been uploaded 
   * @return <code>true</code> private key is already loaded, <code>false</code> if the user has to be requested for the key
   * @throws Exception
   */
  public static boolean uploadPrivateKeyIfNecessary(IClientContext context, IDataTableRecord engineRecord, LoadCallback callback) throws Exception
  {
    // private key for this engine already cached?
    byte[] privateKey = LicenseTableRecord.getPrivateKey(engineRecord.getlongValue(Engine.pkey));
    if (privateKey == null)
    {
      // NO -> ask for upload
      String version = engineRecord.getStringValue(Engine.version);
      context.createOkCancelDialog(new ApplicationMessage("LicenseHelper.UploadPrivateKey", version), new UploadPrivateKeyCallback(engineRecord, callback))
          .show();
      return false;
    }
    return true;
  }
}
