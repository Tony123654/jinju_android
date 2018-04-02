package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Page;

public class PageBuilder {

	public static Page build(JSONObject jsonObject) throws JSONException {
		Page page = new Page();
		page.setPageSize(jsonObject.optInt("pageSize"));
		page.setCurrentPage(jsonObject.optInt("currentPage"));
		page.setTotalPage(jsonObject.optInt("totalPage"));
		return page;
	}

}