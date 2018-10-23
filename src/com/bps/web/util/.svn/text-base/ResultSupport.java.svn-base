package com.bps.web.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author weixiong.dengwx@aliyun-inc.com
 * @since：2011-6-2 下午01:34:21
 */
public class ResultSupport implements Result {
	private static final long serialVersionUID = 3976733653567025460L;
	private boolean success = true;
	private String errMsg = "";
	private Map models = new HashMap(4);
	private String defaultModelKey;

	/**
	 * 创建一个result。
	 */
	public ResultSupport() {
	}

	/**
	 * 创建一个result。
	 * 
	 * @param success
	 *            是否成功
	 */
	public ResultSupport(boolean success) {
		this.success = success;
	}

	/**
	 * 请求是否成功。
	 * 
	 * @return 如果成功，则返回<code>true</code>
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置请求成功标志。
	 * 
	 * @param success
	 *            成功标志
	 */
	public Result setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * 取得默认model对象的key。
	 * 
	 * @return 默认model对象的key
	 */
	public String getDefaultModelKey() {
		return defaultIfEmpty(defaultModelKey, DEFAULT_MODEL_KEY);
	}

	/**
	 * 取得model对象。
	 * 
	 * <p>
	 * 此调用相当于<code>getModels().get(DEFAULT_MODEL_KEY)</code>。
	 * </p>
	 * 
	 * @return model对象
	 */
	public Object getDefaultModel() {
		return models.get(getDefaultModelKey());
	}

	/**
	 * 设置model对象。
	 * 
	 * <p>
	 * 此调用相当于<code>getModels().put(DEFAULT_MODEL_KEY, model)</code>。
	 * </p>
	 * 
	 * @param model
	 *            model对象
	 */
	public Result setDefaultModel(Object model) {
		setDefaultModel(DEFAULT_MODEL_KEY, model);
		return this;
	}

	/**
	 * 设置model对象。
	 * 
	 * <p>
	 * 此调用相当于<code>getModels().put(key, model)</code>。
	 * </p>
	 * 
	 * @param key
	 *            字符串key
	 * @param model
	 *            model对象
	 */
	public Result setDefaultModel(String key, Object model) {
		defaultModelKey = defaultIfEmpty(key, DEFAULT_MODEL_KEY);
		models.put(key, model);
		return this;
	}

	/**
	 * 取得所有model对象。
	 * 
	 * @return model对象表
	 */
	public Map getModels() {
		return models;
	}

	/**
	 * 转换成字符串的表示。
	 * 
	 * @return 字符串表示
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Result {\n");
		buffer.append("    success    = ").append(success).append(",\n");
		buffer.append("    models     = {");

		if (models.isEmpty()) {
			buffer.append("}\n");
		} else {
			buffer.append("\n");

			for (Iterator i = models.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				Object key = entry.getKey();
				Object value = entry.getValue();

				buffer.append("        ").append(key).append(" = ");

				if (value != null) {
					buffer.append("(").append(value.getClass().getName())
							.append(") ");
				}

				buffer.append(value);

				if (i.hasNext()) {
					buffer.append(",");
				}

				buffer.append("\n");
			}

			buffer.append("    }\n");
		}

		buffer.append("}");

		return buffer.toString();
	}

	public Result FAILED(String errMsg) {
		this.success = false;
		this.errMsg = errMsg;
		return this;
	}

	public Result SUCEESS() {
		this.success = true;
		return this;
	}

	public Result FAILED() {
		FAILED(ValidatorUtil.ERROR_SYSTEM);
		return this;
	}

	public String getErrMsg() {
		return this.errMsg;
	}
	

	private static String defaultIfEmpty(String str, String defaultStr) {
		return ((str == null) || (str.length() == 0)) ? defaultStr : str;
	}


}
