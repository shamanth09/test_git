package com.mrkinnoapps.myordershopadmin.bean.constant;

public enum MeltingAndSeal {

	MRK_92("MRK 916", 92), GB_KDM_90("GB KDM", 90), GK_KDM_88("GK KDM", 88), GM_KDM_84(
			"GM KDM", 84), MG_KDM_85("MG KDM", 85), GP_KDM_80("GP KDM", 80), MH_78(
			"MH", 78), GH_KDM_75("GH KDM", 75);

	private final String seal;
	private final int melting;

	private MeltingAndSeal(String seal, int melting) {
		this.seal = seal;
		this.melting = melting;
	}

	public String getSeal() {
		return this.seal;
	}

	public int getMelting() {
		return this.melting;
	}

	public String getMeltingAndSeal() {
		return this.melting + " (" + this.seal.replace("_", " ") + ")";
	}
}
