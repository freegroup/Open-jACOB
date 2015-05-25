/**************************************************************************
 * Project  : jacob.qualitymaster
 * Date     : Wed Jul 01 17:48:14 CEST 2009
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.resources;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;

/**
 * @author andreas
 */
public class I18N
{
  static public final transient String RCS_ID = "$Id: I18N.java,v 1.4 2009-07-01 16:38:16 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  private static final Map map = new HashMap();
  private final String key;
  
   public static final I18N ATTACHMENT_DATECREATED = new I18N("ATTACHMENT.DATECREATED");
   public static final I18N ATTACHMENT_DOCDESCR = new I18N("ATTACHMENT.DOCDESCR");
   public static final I18N ATTACHMENT_DOCUMENT = new I18N("ATTACHMENT.DOCUMENT");
   public static final I18N ATTACHMENT_PKEY = new I18N("ATTACHMENT.PKEY");
   public static final I18N ATTACHMENT_REQUEST_KEY = new I18N("ATTACHMENT.REQUEST_KEY");
   public static final I18N BRANCH_MAKE_BRANCH = new I18N("BRANCH_MAKE_BRANCH");
   public static final I18N BRANCH_MAKE_RELEASE = new I18N("BRANCH_MAKE_RELEASE");
   public static final I18N BRANCH_MAKE_RELEASE_LIST = new I18N("BRANCH_MAKE_RELEASE_LIST");
   public static final I18N BROWSER_ATTACHMENT = new I18N("BROWSER.ATTACHMENT");
   public static final I18N BROWSER_INCIDENT = new I18N("BROWSER.INCIDENT");
   public static final I18N BROWSER_INCIDENT2REQUESTBROWSER = new I18N("BROWSER.INCIDENT2REQUESTBROWSER");
   public static final I18N BROWSER_INCIDENTENTRY = new I18N("BROWSER.INCIDENTENTRY");
   public static final I18N BROWSER_INCIDENTENTRY_ATTACHEMENTBROWSER = new I18N("BROWSER.INCIDENTENTRY_ATTACHEMENTBROWSER");
   public static final I18N BROWSER_INCIDENT_ATTACHEMENTBROWSER = new I18N("BROWSER.INCIDENT_ATTACHEMENTBROWSER");
   public static final I18N BROWSER_MILESTONE_RELEASE = new I18N("BROWSER.MILESTONE_RELEASE");
   public static final I18N BROWSER_ORGCATEGORY = new I18N("BROWSER.ORGCATEGORY");
   public static final I18N BROWSER_ORGMILESTONE = new I18N("BROWSER.ORGMILESTONE");
   public static final I18N BROWSER_REQUEST2INCIDENTBROWSER = new I18N("BROWSER.REQUEST2INCIDENTBROWSER");
   public static final I18N BROWSER_REQUESTBRANCH = new I18N("BROWSER.REQUESTBRANCH");
   public static final I18N BUDGET = new I18N("BUDGET");
   public static final I18N BUDGET_AMOUNT = new I18N("BUDGET.AMOUNT");
   public static final I18N BUDGET_BUDGET_RESPONSIBLE_KEY = new I18N("BUDGET.BUDGET_RESPONSIBLE_KEY");
   public static final I18N BUDGET_HISTORY = new I18N("BUDGET.HISTORY");
   public static final I18N BUDGET_ORGANIZATION_KEY = new I18N("BUDGET.ORGANIZATION_KEY");
   public static final I18N BUDGET_PKEY = new I18N("BUDGET.PKEY");
   public static final I18N BUDGET_REPORT_TEMPLATE_BUDGET_KEY = new I18N("BUDGET.REPORT_TEMPLATE_BUDGET_KEY");
   public static final I18N BUDGET_STATE = new I18N("BUDGET.STATE");
   public static final I18N BUDGET_SUBJECT = new I18N("BUDGET.SUBJECT");
   public static final I18N BUDGET_USED_AMOUNT = new I18N("BUDGET.USED_AMOUNT");
   public static final I18N BUDGET_RESPONSIBLE = new I18N("BUDGET_RESPONSIBLE");
   public static final I18N BUTTON_CREATE_INCIDENT = new I18N("BUTTON.CREATE_INCIDENT");
   public static final I18N BUTTON_CREATE_REQUEST = new I18N("BUTTON.CREATE_REQUEST");
   public static final I18N BUTTON_CREATE_TIMESPENT = new I18N("BUTTON.CREATE_TIMESPENT");
   public static final I18N BUTTON_PRINT_REPORT = new I18N("BUTTON.PRINT_REPORT");
   public static final I18N CATEGORY = new I18N("CATEGORY");
   public static final I18N CATEGORY_CATEGORYOWNER_KEY = new I18N("CATEGORY.CATEGORYOWNER_KEY");
   public static final I18N CATEGORY_CATEGORYSTATUS = new I18N("CATEGORY.CATEGORYSTATUS");
   public static final I18N CATEGORY_GLOBAL = new I18N("CATEGORY.GLOBAL");
   public static final I18N CATEGORY_HISTORY = new I18N("CATEGORY.HISTORY");
   public static final I18N CATEGORY_LONGNAME = new I18N("CATEGORY.LONGNAME");
   public static final I18N CATEGORY_NAME = new I18N("CATEGORY.NAME");
   public static final I18N CATEGORY_PARENTCATEGORY_KEY = new I18N("CATEGORY.PARENTCATEGORY_KEY");
   public static final I18N CATEGORY_PKEY = new I18N("CATEGORY.PKEY");
   public static final I18N CATEGORY_PROJECT_KEY = new I18N("CATEGORY.PROJECT_KEY");
   public static final I18N CATEGORY_STATE = new I18N("CATEGORY.STATE");
   public static final I18N CATEGORYOWNER = new I18N("CATEGORYOWNER");
   public static final I18N CONTEXTMENU_REQUESTOPENOWNER = new I18N("CONTEXTMENU.REQUESTOPENOWNER");
   public static final I18N CONTEXTMENU_REQUESTOPENRC = new I18N("CONTEXTMENU.REQUESTOPENRC");
   public static final I18N CONTEXTMENU_REQUESTOPENTESTER = new I18N("CONTEXTMENU.REQUESTOPENTESTER");
   public static final I18N CREATOR = new I18N("CREATOR");
   public static final I18N CUSTOMER = new I18N("CUSTOMER");
   public static final I18N DOMAIN_ADMIN = new I18N("DOMAIN.ADMIN");
   public static final I18N DOMAIN_ANONYMOUS = new I18N("DOMAIN.ANONYMOUS");
   public static final I18N DOMAIN_INCIDENTENTRY = new I18N("DOMAIN.INCIDENTENTRY");
   public static final I18N DOMAIN_QUALITYMASTER = new I18N("DOMAIN.QUALITYMASTER");
   public static final I18N EMPLOYEE = new I18N("EMPLOYEE");
   public static final I18N EMPLOYEE_EMAIL = new I18N("EMPLOYEE.EMAIL");
   public static final I18N EMPLOYEE_FULLNAME = new I18N("EMPLOYEE.FULLNAME");
   public static final I18N EMPLOYEE_HISTORY = new I18N("EMPLOYEE.HISTORY");
   public static final I18N EMPLOYEE_IMAGE = new I18N("EMPLOYEE.IMAGE");
   public static final I18N EMPLOYEE_LOGINNAME = new I18N("EMPLOYEE.LOGINNAME");
   public static final I18N EMPLOYEE_MOBILE = new I18N("EMPLOYEE.MOBILE");
   public static final I18N EMPLOYEE_ORGANIZATION_KEY = new I18N("EMPLOYEE.ORGANIZATION_KEY");
   public static final I18N EMPLOYEE_PASSWORD = new I18N("EMPLOYEE.PASSWORD");
   public static final I18N EMPLOYEE_PHONE = new I18N("EMPLOYEE.PHONE");
   public static final I18N EMPLOYEE_PKEY = new I18N("EMPLOYEE.PKEY");
   public static final I18N FORM_ADMIN = new I18N("FORM.ADMIN");
   public static final I18N FORM_ANONYMOUSINCIDENT = new I18N("FORM.ANONYMOUSINCIDENT");
   public static final I18N FORM_BUDGET = new I18N("FORM.BUDGET");
   public static final I18N FORM_CATEGORY = new I18N("FORM.CATEGORY");
   public static final I18N FORM_INCIDENT = new I18N("FORM.INCIDENT");
   public static final I18N FORM_INCIDENTENTRY = new I18N("FORM.INCIDENTENTRY");
   public static final I18N FORM_MILESTONE = new I18N("FORM.MILESTONE");
   public static final I18N FORM_MILESTONEENTRY = new I18N("FORM.MILESTONEENTRY");
   public static final I18N FORM_ORGANIZATIONS = new I18N("FORM.ORGANIZATIONS");
   public static final I18N FORM_RCS_BRANCH = new I18N("FORM.RCS_BRANCH");
   public static final I18N FORM_REQUEST = new I18N("FORM.REQUEST");
   public static final I18N FORM_TEMPLATE = new I18N("FORM.TEMPLATE");
   public static final I18N FORM_TIMESPENT = new I18N("FORM.TIMESPENT");
   public static final I18N GROUP_BUDGET = new I18N("GROUP.BUDGET");
   public static final I18N GROUP_CATEGORY = new I18N("GROUP.CATEGORY");
   public static final I18N GROUP_EMPLOYEE = new I18N("GROUP.EMPLOYEE");
   public static final I18N GROUP_INCIDENT = new I18N("GROUP.INCIDENT");
   public static final I18N GROUP_INCIDENTENTRY = new I18N("GROUP.INCIDENTENTRY");
   public static final I18N GROUP_MILESTONE = new I18N("GROUP.MILESTONE");
   public static final I18N GROUP_ORGANIZATION = new I18N("GROUP.ORGANIZATION");
   public static final I18N GROUP_RCS_BRANCH = new I18N("GROUP.RCS_BRANCH");
   public static final I18N GROUP_RCS_PROJECT = new I18N("GROUP.RCS_PROJECT");
   public static final I18N GROUP_REPORT_TEMPLATE = new I18N("GROUP.REPORT_TEMPLATE");
   public static final I18N GROUP_REQUEST = new I18N("GROUP.REQUEST");
   public static final I18N GROUP_TIMESPENT = new I18N("GROUP.TIMESPENT");
   public static final I18N INCIDENT = new I18N("INCIDENT");
   public static final I18N INCIDENT_BILLABLE = new I18N("INCIDENT.BILLABLE");
   public static final I18N INCIDENT_CATEGORY_KEY = new I18N("INCIDENT.CATEGORY_KEY");
   public static final I18N INCIDENT_CREATE_DATE = new I18N("INCIDENT.CREATE_DATE");
   public static final I18N INCIDENT_CUSTOMER_KEY = new I18N("INCIDENT.CUSTOMER_KEY");
   public static final I18N INCIDENT_CUST_SOLUTION_INFO = new I18N("INCIDENT.CUST_SOLUTION_INFO");
   public static final I18N INCIDENT_DESCRIPTION = new I18N("INCIDENT.DESCRIPTION");
   public static final I18N INCIDENT_HISTORY = new I18N("INCIDENT.HISTORY");
   public static final I18N INCIDENT_MILESTONE_KEY = new I18N("INCIDENT.MILESTONE_KEY");
   public static final I18N INCIDENT_MYOPEN = new I18N("INCIDENT.MYOPEN");
   public static final I18N INCIDENT_OPENINCIDENT = new I18N("INCIDENT.OPENINCIDENT");
   public static final I18N INCIDENT_ORGANIZATION_KEY = new I18N("INCIDENT.ORGANIZATION_KEY");
   public static final I18N INCIDENT_PKEY = new I18N("INCIDENT.PKEY");
   public static final I18N INCIDENT_PRIORITY = new I18N("INCIDENT.PRIORITY");
   public static final I18N INCIDENT_QA_DATE = new I18N("INCIDENT.QA_DATE");
   public static final I18N INCIDENT_STATE = new I18N("INCIDENT.STATE");
   public static final I18N INCIDENT_SUBJECT = new I18N("INCIDENT.SUBJECT");
   public static final I18N INCIDENTBILLABLE = new I18N("INCIDENTBILLABLE");
   public static final I18N INCIDENTENTRYCATEGORY = new I18N("INCIDENTENTRYCATEGORY");
   public static final I18N INCIDENT_ATTACHEMENT_CREATE_DATE = new I18N("INCIDENT_ATTACHEMENT.CREATE_DATE");
   public static final I18N INCIDENT_ATTACHEMENT_DESCRIPTION = new I18N("INCIDENT_ATTACHEMENT.DESCRIPTION");
   public static final I18N INCIDENT_ATTACHEMENT_DOCUMENT = new I18N("INCIDENT_ATTACHEMENT.DOCUMENT");
   public static final I18N INCIDENT_ATTACHEMENT_INCIDENT_KEY = new I18N("INCIDENT_ATTACHEMENT.INCIDENT_KEY");
   public static final I18N INCIDENT_ATTACHEMENT_PKEY = new I18N("INCIDENT_ATTACHEMENT.PKEY");
   public static final I18N INCIDENT_BUDGETENTRY = new I18N("INCIDENT_BUDGETENTRY");
   public static final I18N MAIN_REQUEST = new I18N("MAIN_REQUEST");
   public static final I18N MESSAGE_BRANCHMAKERELEASE_CHECKUNFINISHEDCODE = new I18N("MESSAGE.BRANCHMAKERELEASE.CHECKUNFINISHEDCODE");
   public static final I18N MESSAGE_BRANCHMAKERELEASE_CHECKUNTESTEDCODE = new I18N("MESSAGE.BRANCHMAKERELEASE.CHECKUNTESTEDCODE");
   public static final I18N MESSAGE_BRANCHMAKERELEASE_RELEASEUNTESTEDCODE = new I18N("MESSAGE.BRANCHMAKERELEASE.RELEASEUNTESTEDCODE");
   public static final I18N MESSAGE_BUDGET_CLOSED = new I18N("MESSAGE.BUDGET_CLOSED");
   public static final I18N MESSAGE_REQUESTUPDATE_NOBRANCHESAVAILABLE = new I18N("MESSAGE.REQUESTUPDATE.NOBRANCHESAVAILABLE");
   public static final I18N MESSAGE_REQUESTUPDATE_NOOWNER = new I18N("MESSAGE.REQUESTUPDATE.NOOWNER");
   public static final I18N MESSAGE_REQUESTUPDATE_NOTESTER = new I18N("MESSAGE.REQUESTUPDATE.NOTESTER");
   public static final I18N MESSAGE_REQUESTUPDATE_ORGCHANGENOTALLOWED = new I18N("MESSAGE.REQUESTUPDATE.ORGCHANGENOTALLOWED");
   public static final I18N MILESTONE = new I18N("MILESTONE");
   public static final I18N MILESTONE_ACTUALDONE = new I18N("MILESTONE.ACTUALDONE");
   public static final I18N MILESTONE_DESCRIPTION = new I18N("MILESTONE.DESCRIPTION");
   public static final I18N MILESTONE_DESCRIPTIONTEXT = new I18N("MILESTONE.DESCRIPTIONTEXT");
   public static final I18N MILESTONE_HISTORY = new I18N("MILESTONE.HISTORY");
   public static final I18N MILESTONE_ORGANIZATION_KEY = new I18N("MILESTONE.ORGANIZATION_KEY");
   public static final I18N MILESTONE_PERCENTAGEBAR = new I18N("MILESTONE.PERCENTAGEBAR");
   public static final I18N MILESTONE_PKEY = new I18N("MILESTONE.PKEY");
   public static final I18N MILESTONE_PLANDONE = new I18N("MILESTONE.PLANDONE");
   public static final I18N MILESTONE_REPORT_TEMPLATE_KEY = new I18N("MILESTONE.REPORT_TEMPLATE_KEY");
   public static final I18N MILESTONE_STATE = new I18N("MILESTONE.STATE");
   public static final I18N MILESTONE_SUBJECT = new I18N("MILESTONE.SUBJECT");
   public static final I18N MILESTONEENTRY = new I18N("MILESTONEENTRY");
   public static final I18N MILESTONE_RELEASE_MILESTONE_KEY = new I18N("MILESTONE_RELEASE.MILESTONE_KEY");
   public static final I18N MILESTONE_RELEASE_RELEASE_KEY = new I18N("MILESTONE_RELEASE.RELEASE_KEY");
   public static final I18N ORGANIZATION = new I18N("ORGANIZATION");
   public static final I18N ORGANIZATION_HISTORY = new I18N("ORGANIZATION.HISTORY");
   public static final I18N ORGANIZATION_NAME = new I18N("ORGANIZATION.NAME");
   public static final I18N ORGANIZATION_PKEY = new I18N("ORGANIZATION.PKEY");
   public static final I18N ORGCATEGORY_CATEGORY_KEY = new I18N("ORGCATEGORY.CATEGORY_KEY");
   public static final I18N ORGCATEGORY_ORGANIZATION_KEY = new I18N("ORGCATEGORY.ORGANIZATION_KEY");
   public static final I18N ORGCATEGORY_ORGCATEGORYIFBROWSER = new I18N("ORGCATEGORY.ORGCATEGORYIFBROWSER");
   public static final I18N OWNER = new I18N("OWNER");
   public static final I18N PARENTCATEGORY = new I18N("PARENTCATEGORY");
   public static final I18N PROJECT_PKEY = new I18N("PROJECT.PKEY");
   public static final I18N PROJECT_RCS_NAME = new I18N("PROJECT.RCS_NAME");
   public static final I18N RCS_BRANCH = new I18N("RCS_BRANCH");
   public static final I18N RCS_BRANCH_HISTORY = new I18N("RCS_BRANCH.HISTORY");
   public static final I18N RCS_BRANCH_NAME = new I18N("RCS_BRANCH.NAME");
   public static final I18N RCS_BRANCH_PKEY = new I18N("RCS_BRANCH.PKEY");
   public static final I18N RCS_BRANCH_RCS_PROJECT_KEY = new I18N("RCS_BRANCH.RCS_PROJECT_KEY");
   public static final I18N RCS_BRANCH_RCS_TAG = new I18N("RCS_BRANCH.RCS_TAG");
   public static final I18N RCS_BRANCH_STATUS = new I18N("RCS_BRANCH.STATUS");
   public static final I18N RCS_BRANCH_VERSION = new I18N("RCS_BRANCH.VERSION");
   public static final I18N RCS_BRANCH_VERSIONNBR = new I18N("RCS_BRANCH.VERSIONNBR");
   public static final I18N RCS_PROJECT = new I18N("RCS_PROJECT");
   public static final I18N RCS_PROJECT_DISPLAY_NAME = new I18N("RCS_PROJECT.DISPLAY_NAME");
   public static final I18N RCS_PROJECT_HISTORY = new I18N("RCS_PROJECT.HISTORY");
   public static final I18N RELEASE = new I18N("RELEASE");
   public static final I18N REPORT_TEMPLATE = new I18N("REPORT_TEMPLATE");
   public static final I18N REPORT_TEMPLATE_DESCRIPTION = new I18N("REPORT_TEMPLATE.DESCRIPTION");
   public static final I18N REPORT_TEMPLATE_DOCUMENT = new I18N("REPORT_TEMPLATE.DOCUMENT");
   public static final I18N REPORT_TEMPLATE_HISTORY = new I18N("REPORT_TEMPLATE.HISTORY");
   public static final I18N REPORT_TEMPLATE_PKEY = new I18N("REPORT_TEMPLATE.PKEY");
   public static final I18N REPORT_TEMPLATE_SUBJECT = new I18N("REPORT_TEMPLATE.SUBJECT");
   public static final I18N REPORT_TEMPLATE_TYPE = new I18N("REPORT_TEMPLATE.TYPE");
   public static final I18N REQUEST = new I18N("REQUEST");
   public static final I18N REQUEST_ADD_BRANCH = new I18N("REQUEST.ADD_BRANCH");
   public static final I18N REQUEST_CATEGORY_KEY = new I18N("REQUEST.CATEGORY_KEY");
   public static final I18N REQUEST_CREATER_KEY = new I18N("REQUEST.CREATER_KEY");
   public static final I18N REQUEST_CUST_SOLUTION_INFO = new I18N("REQUEST.CUST_SOLUTION_INFO");
   public static final I18N REQUEST_DATEREPORTED = new I18N("REQUEST.DATEREPORTED");
   public static final I18N REQUEST_DESCRIPTION = new I18N("REQUEST.DESCRIPTION");
   public static final I18N REQUEST_HISTORY = new I18N("REQUEST.HISTORY");
   public static final I18N REQUEST_MAIN_REQUEST_KEY = new I18N("REQUEST.MAIN_REQUEST_KEY");
   public static final I18N REQUEST_OWNER_KEY = new I18N("REQUEST.OWNER_KEY");
   public static final I18N REQUEST_PKEY = new I18N("REQUEST.PKEY");
   public static final I18N REQUEST_PRIORITY = new I18N("REQUEST.PRIORITY");
   public static final I18N REQUEST_STATE = new I18N("REQUEST.STATE");
   public static final I18N REQUEST_SUBJECT = new I18N("REQUEST.SUBJECT");
   public static final I18N REQUEST_SUSPEND_FROM_RELLIST = new I18N("REQUEST.SUSPEND_FROM_RELLIST");
   public static final I18N REQUEST_TESTER_KEY = new I18N("REQUEST.TESTER_KEY");
   public static final I18N REQUEST_TYPE = new I18N("REQUEST.TYPE");
   public static final I18N REQUESTBRANCH_RCS_BRANCH_KEY = new I18N("REQUESTBRANCH.RCS_BRANCH_KEY");
   public static final I18N REQUESTBRANCH_REQUEST_KEY = new I18N("REQUESTBRANCH.REQUEST_KEY");
   public static final I18N REQUEST_INCIDENT_INCIDENT_KEY = new I18N("REQUEST_INCIDENT.INCIDENT_KEY");
   public static final I18N REQUEST_INCIDENT_REQUEST_KEY = new I18N("REQUEST_INCIDENT.REQUEST_KEY");
   public static final I18N TAB_INCIDENT_ATTACHMENT = new I18N("TAB.INCIDENT_ATTACHMENT");
   public static final I18N TAB_INCIDENT_COMMON = new I18N("TAB.INCIDENT_COMMON");
   public static final I18N TAB_INCIDENT_REQUEST = new I18N("TAB.INCIDENT_REQUEST");
   public static final I18N TAB_INCIDENT_TIMESPENT = new I18N("TAB.INCIDENT_TIMESPENT");
   public static final I18N TESTER = new I18N("TESTER");
   public static final I18N TIMESPENT_BUDGET_KEY = new I18N("TIMESPENT.BUDGET_KEY");
   public static final I18N TIMESPENT_CATEGORY = new I18N("TIMESPENT.CATEGORY");
   public static final I18N TIMESPENT_CLEARED = new I18N("TIMESPENT.CLEARED");
   public static final I18N TIMESPENT_DATEREPORTED = new I18N("TIMESPENT.DATEREPORTED");
   public static final I18N TIMESPENT_EMPLOYEE_KEY = new I18N("TIMESPENT.EMPLOYEE_KEY");
   public static final I18N TIMESPENT_HISTORY = new I18N("TIMESPENT.HISTORY");
   public static final I18N TIMESPENT_INCIDENTBILLABLE_KEY = new I18N("TIMESPENT.INCIDENTBILLABLE_KEY");
   public static final I18N TIMESPENT_PKEY = new I18N("TIMESPENT.PKEY");
   public static final I18N TIMESPENT_SUMMARY = new I18N("TIMESPENT.SUMMARY");
   public static final I18N TIMESPENT_TIMESPENT = new I18N("TIMESPENT.TIMESPENT");
   public static final I18N TIMESPENT_TIMESPENTIFBROWSER = new I18N("TIMESPENT.TIMESPENTIFBROWSER");
	 
  protected I18N(String key)
  {
    this.key = key;
    map.put(key, this);
  }
  
  public final String get(Context context, Object[] args)
  {
     String localized = get(context);
     return MessageFormat.format(localized, args);
  }

  public final String get(Context context)
  {
     return de.tif.jacob.i18n.I18N.getLocalized(this.key, context, context.getLocale());
  }
  
  public final String get(Context context, Locale locale, Object[] args)
  {
     String localized = get(context, locale);
     return MessageFormat.format(localized, args);
  }

  public final String get(Context context, Locale locale)
  {
     return de.tif.jacob.i18n.I18N.getLocalized(this.key, context, locale);
  }
  
  public static String get(Context context, String key)
  {
    return ((I18N) map.get(key)).get(context);
  }

  public final String getKey()
  {
     return this.key;
  }
}
