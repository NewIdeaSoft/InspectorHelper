package com.newideasoft.qualityinspectorhelper;

public class CategoryInfo {
	private int icon;
	private String name;
	private String intro;
	public CategoryInfo(int icon, String name, String intro) {
		super();
		this.icon = icon;
		this.name = name;
		this.intro = intro;
	}
	public CategoryInfo() {
		super();
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	@Override
	public String toString() {
		return "CategoryInfo [icon=" + icon + ", name=" + name + ", intro=" + intro + "]";
	}
	
}
