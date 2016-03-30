package com.newideasoft.qualityinspectorhelper;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Administrator
 * 用户权限信息：可以浏览的产品，可以浏览的岗位，产品可以是多种，每种产品可以对应多个岗位
 */
public class UserInfo {
	private String phoneNumber;
	private String name;
	private ArrayList<String> products;
	private ArrayList<Map<String,ArrayList<String>>> station;
	public UserInfo(String phoneNumber, String name, ArrayList<String> products,
			ArrayList<Map<String, ArrayList<String>>> station) {
		super();
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.products = products;
		this.station = station;
	}
	public UserInfo() {
		super();
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<String> products) {
		this.products = products;
	}
	public ArrayList<Map<String, ArrayList<String>>> getStation() {
		return station;
	}
	public void setStation(ArrayList<Map<String, ArrayList<String>>> station) {
		this.station = station;
	}
	@Override
	public String toString() {
		return "UserInfo [phoneNumber=" + phoneNumber + ", name=" + name + ", products=" + products + ", station="
				+ station + "]";
	}
	
}
