package com.adityawasnik.jui;

public class DefectsList_DataModel
{
	String date="", ticket , action;
	//private byte[] statusImage;
	
	public DefectsList_DataModel(String ticket, String date, String action)
	{
		this.ticket = ticket;
		this.date = date;
		//this.action = action;
		this.action = action;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		action = action;
	}



/*	public byte[] getStatusImage() {
		return statusImage;
	}

	public void setStatusImage(byte[] statusImage) {
		this.statusImage = statusImage;
	}*/

}
