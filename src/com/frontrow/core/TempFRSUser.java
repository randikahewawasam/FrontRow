package com.frontrow.core;

public class TempFRSUser {

	public String tempCmpId;
	public String tempMobile;
	public String tempPwd;

	public static TempFRSUser instance;

	public static TempFRSUser getSharedInstance(){
		if(instance == null){
			instance = new TempFRSUser();
		}
		return instance;
	}

	public String getTempCmpId() {
		return tempCmpId;
	}

	public void setTempCmpId(String tempCmpId) {
		this.tempCmpId = tempCmpId;
	}

	public String getTempMobile() {
		return tempMobile;
	}

	public void setTempMobile(String tempMobile) {
		this.tempMobile = tempMobile;
	}

	public String getTempPwd() {
		return tempPwd;
	}

	public void setTempPwd(String tempPwd) {
		this.tempPwd = tempPwd;
	}
}
