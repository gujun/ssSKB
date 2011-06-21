package com.cari.voip.keyboard.soft.model;

public class sms {
	/*	" CREATE TABLE sms_ok (\n"
	"    chat VARCHAR(255),\n"
	"    sms_to VARCHAR(255),\n"
	"    proto  VARCHAR(255),\n"
	"    sms_from VARCHAR(255),\n"
	"    subject VARCHAR(255),\n"
	"    body  VARCHAR(1023),\n"
	"    sms_type  VARCHAR(255),\n"
	"    hint  VARCHAR(255),\n"
	"    start_epoch INTEGER,\n"
	"    start_stamp  TIMESTAMP\n"
	");\n";*/
	public String chat;
	public String sms_to;
	public String proto;
	public String sms_from;
	public String body;

	public String sms_type;
	
	public String start_stamp;
	
	public sms(){}
	
	
	
}
