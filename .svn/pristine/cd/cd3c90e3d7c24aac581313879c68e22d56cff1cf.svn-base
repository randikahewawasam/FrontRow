package com.frontrow.core;

import java.util.Hashtable;

public class Answers {
	private String answerCode;
	private String answerText;
	private Hashtable<String, String> ansCodeMapping;
	private static Answers instance;

	private Answers getSharedInstance(){
		if(instance == null){
			instance = new Answers();
		}
		return instance;
	}

	public String getAnswerCode() {
		return answerCode;
	}
	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public Hashtable<String, String> getAnsCodeMapping() {
		return ansCodeMapping;
	}
	public void setAnsCodeMapping(Hashtable<String, String> ansCodeMapping) {
		this.ansCodeMapping = ansCodeMapping;
	}


}
