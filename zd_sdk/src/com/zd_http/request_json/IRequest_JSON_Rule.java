package com.zd_http.request_json;

import java.util.List;

import org.json.JSONObject;

/**
 * 抽像出的一个带规则的匹配接口
 * 数据匹配验证,默认的处理验证规则
 * @author lixifeng
 *
 */
public interface IRequest_JSON_Rule extends IRequest_JSON{
	/**
	 * 本接口要求实现的一个数据验证功能
	 */
	public int RuleData(	List listOld,List listNew);
}
