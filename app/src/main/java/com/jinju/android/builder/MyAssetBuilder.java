package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.MyAsset;

public class MyAssetBuilder {

	public static MyAsset build(JSONObject jsonObject) throws JSONException {
		MyAsset myAsset = new MyAsset();
		myAsset.setCurrentBalance(jsonObject.optLong("currentBalance"));
		myAsset.setCurrentPosition(jsonObject.optLong("currentPosition"));
		myAsset.setNetAssets(jsonObject.optLong("netAssets"));
		myAsset.setCanUseBalance(jsonObject.optLong("canUseBalance"));
		myAsset.setPositionFrezen(jsonObject.optLong("positionFrezen"));
		myAsset.setOtherFrezen(jsonObject.optLong("otherFrezen"));
		myAsset.setTotalPosition(jsonObject.optLong("totalPosition"));
		myAsset.setFrozenBalance(jsonObject.optLong("frozenBalance"));
		return myAsset;
	}

}