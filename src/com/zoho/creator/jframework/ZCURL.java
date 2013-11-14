// $Id$
package com.zoho.creator.jframework;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



public class ZCURL {
	
	private static List<NameValuePair> getParamsWithOwner(String appOwner) {
		if(appOwner == null) {
			throw new RuntimeException("App Owner Cannot be null"); //No I18N
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("zc_ownername", appOwner));//No I18N
		params.addAll(getDefaultParams());
		return params;
	}
	
	private static List<NameValuePair> getDefaultParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("authtoken", ZOHOCreator.getZohoUser().getAuthToken()));//No I18N
		////System.out.println("paramsss"+params);
		params.add(new BasicNameValuePair("scope", "creatorapi"));//No I18N
		return params;
	}
	
	private static List<NameValuePair> getParamsWithOwnerAndXMLString(String appOwner, String xmlString) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("xmlString", xmlString));//No I18N
		return params;
	}
	
	public static String getFileUploadURL(String filepath, String appOwner, String appLinkName, String viewLinkName) {
		return serverURL() + "/DownloadFileFromMig.do?filepath=/" + filepath + "&sharedBy=" + appOwner + "&appLinkName=" + appLinkName + "&viewLinkName=" + viewLinkName + "&authtoken=" + ZOHOCreator.getZohoUser().getAuthToken() + "&scope=creatorapi";  //No I18N
	}

	public static String getImageURL(String filePath) {
		return serverURL() + filePath + "&authtoken=" + ZOHOCreator.getZohoUser().getAuthToken() + "&scope=creatorapi";  //No I18N
	}
	
	static URLPair customActionURL(String appLinkName, String viewLinkName, long customActionId, String appOwner, List<Long> recordIDs) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		for(int i=0; i<recordIDs.size(); i++) {
			Long recId = recordIDs.get(i);
			params.add(new BasicNameValuePair("functiongroup", recId+"")); //No I18N
		}
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + viewLinkName + "/OnExecute/" + customActionId + "/", params); //No I18N
	}
	
	static URLPair xmlWriteURL(String appOwner, String xmlString) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("XMLString", xmlString));//No I18N
		return new URLPair(serverURL() + "/api/xml/write", params); //No I18N
	}
	
	static URLPair navigationListURL() {
		return new URLPair(serverURL() + "/api/xml/allappscategory/", getDefaultParams()); //No I18N
	}
	
	static URLPair appListURL() {
		return new URLPair(serverURL() + "/api/xml/applications/", getDefaultParams()); //No I18N
	}
	
	static URLPair sharedAppListURL(Long groupid) {
		List<NameValuePair> params = getDefaultParams();
		if(groupid != null) {
			params.add(new BasicNameValuePair("groupid", groupid+""));//No I18N
		}
		return new URLPair(serverURL() + "/api/xml/sharedapplications/", params); //No I18N
	}
	
	static URLPair workSpaceAppListURL(String workSpaceOwner) {
		if(workSpaceOwner == null) {
			throw new RuntimeException("Workspace Owner Cannot be null"); //No I18N
		}
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("workSpaceOwner", workSpaceOwner));//No I18N
		return new URLPair(serverURL() + "/api/xml/workspaceapplications/", params); //No I18N
	}

	static URLPair sectionMetaURL(String appLinkName, String appOwner) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/sections/", getParamsWithOwner(appOwner)); //No I18N
	}

	static URLPair viewURL(String appLinkName, String viewLinkName, String appOwner) {
		return new URLPair (serverURL() + "/api/mobile/xml/" + appLinkName + "/view/" + viewLinkName + "/", //No I18N
				getParamsWithOwner(appOwner)); //No I18N
	}

	static URLPair htmlViewURL(String appLinkName, String viewLinkName, String appOwner) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("viewLinkName", viewLinkName));//No I18N
		return new URLPair (serverURL() + "/showHtmlViewApi.do", params); //No I18N
	}
	
	static URLPair fileUploadURL(String appOwner) {
		return new URLPair(serverURL() + "/api/xml/fileupload/", getParamsWithOwner(appOwner));//No I18N

	}

	
	
	static URLPair formMetaURL(String appLinkName, String formLinkName, String appOwner, String viewLinkName, Long recordLinkId, int formType, String refAppLinkName, String refFormLinkName, String refFieldName, Date calSelectedStartDate,Date calSelectedEndDate) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("metaData","complete"));//No I18N
		params.add(new BasicNameValuePair("zcRefValue","true"));//No I18N
		if(recordLinkId != null) {
			return new URLPair(serverURL() + "/api/"+appOwner+"/xml/" + appLinkName + "/view/"+ viewLinkName + "/record/" + recordLinkId + "/edit/", params);//No I18N
		} else {
			if(viewLinkName != null) {
				params.add(new BasicNameValuePair("viewLinkName", viewLinkName));//No I18N
			}
			if(refAppLinkName != null && refFormLinkName != null && refFieldName != null) {
				if(ZOHOCreator.isPersonalApps())
				{
				params.add(new BasicNameValuePair("refAppLinkName", refAppLinkName));//No I18N
				params.add(new BasicNameValuePair("refFormLinkName", refFormLinkName));//No I18N
				params.add(new BasicNameValuePair("refFieldName", refFieldName));//No I18N
				System.out.println("inside ifffff");
				}
				else
				{
					int lookupCount = ZOHOCreator.getBaseLookUPField().getLookUpCount();
									  List<ZCField> zcfields = ZOHOCreator.getBaseLookUPField().getListOfZCFieldForLookup();
									  System.out.println("inside elseee"+zcfields.size());
					for(int i=1;i<zcfields.size();i++)
					{
						ZCField zcField = zcfields.get(i-1);
						ZCForm zcForm = zcField.getBaseForm();
						params.add(new BasicNameValuePair("zc_childappname_"+(i), zcForm.getAppLinkName()));//No I18N
						params.add(new BasicNameValuePair("zc_childformname_"+(i),zcForm.getComponentLinkName()));//No I18N
						params.add(new BasicNameValuePair("zc_childlabelname_"+(i),zcField.getFieldName()));//No I18N
					}
					params.add(new BasicNameValuePair("zc_childappname_"+lookupCount, refAppLinkName));//No I18N
					params.add(new BasicNameValuePair("zc_childformname_"+lookupCount, refFormLinkName));//No I18N
					params.add(new BasicNameValuePair("zc_childlabelname_"+lookupCount, refFieldName));//No I18N
					params.add(new BasicNameValuePair("zc_lookupCount",lookupCount+""));	
				}
			}
			if(calSelectedStartDate != null) {			
				////System.out.println(calSelectedStartDate+"start"+calSelectedEndDate+"end");
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(calSelectedStartDate);
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(calSelectedEndDate);
				params.add(new BasicNameValuePair("dateJsonObject", "{\"startDate\":{\"day\":" + startDateCal.get(Calendar.DAY_OF_MONTH) + ",\"month\":" + startDateCal.get(Calendar.MONTH) + ",\"year\":" + startDateCal.get(Calendar.YEAR) + ",\"hours\":"+ startDateCal.get(Calendar.HOUR_OF_DAY)  +",\"minutes\":"+ startDateCal.get(Calendar.MINUTE) +",\"seconds\":" + startDateCal.get(Calendar.SECOND) +"}," +  //No I18N
																	"\"endDate\":{\"day\":" +endDateCal.get(Calendar.DAY_OF_MONTH) + ",\"month\":" + endDateCal.get(Calendar.MONTH) + ",\"year\":" + endDateCal.get(Calendar.YEAR) + ",\"hours\":"+ endDateCal.get(Calendar.HOUR_OF_DAY)  +",\"minutes\":"+ endDateCal.get(Calendar.MINUTE) +",\"seconds\":" + endDateCal.get(Calendar.SECOND) + "}};"));//No I18N
			}
			params.add(new BasicNameValuePair("formAccessType", String.valueOf(formType)));//No I18N
			////System.out.println("Viewlinknameeee"+viewLinkName);
			return new URLPair(serverURL() + "/api/"+appOwner+"/xml/" + appLinkName + "/" +"form/"+ formLinkName + "/fields/", params);//No I18N	
		}
		
		////System.out.println("urlllll"+serverURL() + "/api/"+appOwner+"/xml/" + appLinkName + "/" +"form/"+ formLinkName + "/fields/"+params);

	}
	
	static URLPair lookupChoices(String appLinkName, String formLinkName, String appOwner,String lookupFieldName, int startIndex, String searchString, String subformComponent) {
		List<NameValuePair> params = getDefaultParams();
			params.add(new BasicNameValuePair("limit", 50 + ""));
			params.add(new BasicNameValuePair("appendRows", "true"));
			params.add(new BasicNameValuePair("startindex", startIndex + ""));
			params.add(new BasicNameValuePair("zcRefValue", true+""));
		if(searchString != null && !"".equals(searchString)) {
			params.add(new BasicNameValuePair("searchValue", searchString));
		}
		if(subformComponent != null) {
			params.add(new BasicNameValuePair("subformcomponent", subformComponent));
		}
		
		return new URLPair(serverURL() + "/api/"+appOwner+"/xml/" + appLinkName + "/" +"form/"+ formLinkName +"/lookup/"+lookupFieldName+ "/options/", params);//No I18N
	}
	

	
	static URLPair formEditOnLoad(String appLinkName, String formLinkName, String appOwner, String xmlString,Long recordLinkId,String viewLinkName) {
		List<NameValuePair> params=getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
	    params.add(new BasicNameValuePair("linkNameBased", "true"));
		params.add(new BasicNameValuePair("recType",String.valueOf(ZCForm.VIEW_EDIT_FORM)));
		params.add(new BasicNameValuePair("pkValue",String.valueOf(recordLinkId-2)));
		if(viewLinkName!=null)
		{
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
		//return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnEditLoad/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair subFormOnUser(String appLinkName, String formLinkName, String subFormFieldLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params,boolean isFormula,String viewLinkName) {
		params.addAll(getDefaultParams());
		if(isFormula) {
			params.add(new BasicNameValuePair("isFormula", "true"));
			params.add(new BasicNameValuePair("isSubForm", "true"));
		}
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
	    params.add(new BasicNameValuePair("linkNameBased", "true"));
	    params.add(new BasicNameValuePair("fieldName", fieldLinkName));
	    
	    params.add(new BasicNameValuePair("subformFieldName", subFormFieldLinkName));
	   params.add(new BasicNameValuePair("subfcname","SF(SubForm).FD(t::row_1).SV("+subFormFieldLinkName+")"));
	   params.add(new BasicNameValuePair("rowseqid","t::row_1"));
	   if(viewLinkName!=null)
		{
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));
		}
	   ////System.out.println("formlinknameee   "  +formLinkName+"    fieldName  "+subFormFieldLinkName + "  fieldname  "+ fieldLinkName +"  subFormfieldlinkname  "+ subFormFieldLinkName);
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
		//return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/SubOnUser/" + subFormFieldLinkName + "/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}


//	static URLPair fieldOnUser(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, String xmlString) {
//		//return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnUser/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
//		return new URLPair(serverURL() + "/generateJSAPI.do?sharedBy="+appOwner+"&appLinkName=" + appLinkName  +"&formLinkName="+ formLinkName+"&fieldName"+fieldLinkName+"&linkNameBased=true", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
//	}

	static URLPair fieldOnUser(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params, boolean isFormula,String viewLinkName) {
	////System.out.println("onUserinput triggered");
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));
		params.add(new BasicNameValuePair("linkNameBased", "true"));
		if(isFormula) {
			params.add(new BasicNameValuePair("isFormula", "true"));
		}
		if(viewLinkName!=null)
		{
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
	}
	
	static URLPair formOnLoad(String appLinkName, String formLinkName, String appOwner, String xmlString , String viewLinkName) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
		params.add(new BasicNameValuePair("linkNameBased", "true"));
		if(viewLinkName!=null)
		{
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
	}

	static URLPair buttonOnClick(String appLinkName, String formLinkName, String buttonName, String appOwner,List<NameValuePair> paramsss) {
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
	    params.add(new BasicNameValuePair("linkNameBased", "true"));
		params.add(new BasicNameValuePair("buttonName",buttonName));
//		params.add(new BasicNameValuePair("buttonwftype","51"));
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
		//return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnClick/" + buttonName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair subFormAddRow(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params,String viewLinkName) {
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("SF(SubForm).FD(t::row_"+(ZOHOCreator.getCurrentForm().getField(fieldLinkName).getAddedSubFormEntries().size()+1)+").SV(record::status)","added"));
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));
		params.add(new BasicNameValuePair("linkNameBased", "true"));
		params.add(new BasicNameValuePair("rowactiontype","onaddrow"));
		params.add(new BasicNameValuePair("rowseqid","t::row_"+  (ZOHOCreator.getCurrentForm().getField(fieldLinkName).getAddedSubFormEntries().size()+1)));
		if(viewLinkName!=null)
		{
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
		//return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnAddRow/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair subFormDeleteRow(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params,String viewLinkName) {
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));
		params.add(new BasicNameValuePair("appLinkName", appLinkName));
		params.add(new BasicNameValuePair("formLinkName", formLinkName));
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));
		params.add(new BasicNameValuePair("linkNameBased", "true"));
		params.add(new BasicNameValuePair("rowactiontype","ondeleterow"));
		params.add(new BasicNameValuePair("rowseqid","t::row_"+1));
		if(viewLinkName!=null)
		{
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
		//return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnDeleteRow/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}
	
	static URLPair recordCount(String appOwner, String appLinkName, String viewLinkName) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/recordcount/" + viewLinkName, params); //No I18N
	}

	public static String getLoginUrl() {
		return "https://" + ZOHOCreator.getAccountsURL() + "/login?hide_signup=true&hide_remember=true&scopes=" + ZOHOCreator.getServiceName() + "/creatorapi,ZohoContacts/photoapi&appname=" + ZOHOCreator.getServiceName()  + "&serviceurl=" + serverURL();
	}
	
	// 		signInUrl = "https://accounts.zoho.com/login?hide_signup=true&hide_remember=true&scopes=ZohoCreator/creatorapi&appname=ZohoCreator&serviceurl=https://creator.zoho.com";  //No I18N

	static String getAuthTokenURL(String userName, String password) {
		return  "https://" + ZOHOCreator.getAccountsURL() + "/apiauthtoken/nb/create?SCOPE=" + ZOHOCreator.getServiceName() + "/creatorapi&EMAIL_ID=" + userName + "&PASSWORD=" + password; //No I18N
	}

	static String deleteAuthToken(String authToken) {
		return "https://" + ZOHOCreator.getAccountsURL() + "/apiauthtoken/delete?AUTHTOKEN=" + authToken; //No I18N
	}

	public static String serverURL() {
		return ZOHOCreator.getPrefix() + "://" + ZOHOCreator.getCreatorURL(); //"https://icreator.localzoho.com"; //No I18N
	}

	public static URLPair userPersonalInfoURL() {
		// TODO Auto-generated method stub		
		return new URLPair(serverURL() + "/api/xml/user/", getDefaultParams());
	}
	
	public static String getURLForPersonalPhoto(){
		return "https://contacts.zoho.com/file/download?fs=thumb&t=user&ID=" + ZOHOCreator.getCurrentUserInfo().getId() + "&authtoken=" + ZOHOCreator.getZohoUser().getAuthToken();
	}
}
