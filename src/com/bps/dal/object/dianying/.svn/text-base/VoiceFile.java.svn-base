/**
 * 
 */
package com.bps.dal.object.dianying;

import java.io.Serializable;
import java.util.Date;

import com.bps.annotation.DBField;
import com.bps.annotation.DBTable;
import com.bps.dal.object.BaseEntity;

/**
 * @author HeJin
 *
 */
//表名字                                                             主键                                                                默认排序列                                            排序方式
@DBTable(tableName = "voice_file",primaryKeyName="id", defaultSort = "id", defaultDir = "desc")
public class VoiceFile extends BaseEntity implements Serializable {
	@DBField
	private Integer id;      //id 自增
	@DBField
	private String voice_file_id;      //录音文件ID（上传人ID+yyyyMMddHHmmss）
	@DBField
	private String voice_file_name;      //录音文件名
	@DBField
	private String voice_file_path;      //录音文件路径(文件服务器绝对路径<服务器上按月创建>)
	@DBField
	private String upload_time;      //上传人时间
	@DBField
	private String upload_user_id;      //上传人ID
	@DBField
	private String voice_type;      //录音男声/女声 1：男声，2：女声
	@DBField
	private String reserve1;      //预留字段1
	@DBField
	private String reserve2;      //预留字段2
	
	public VoiceFile(){}
	
	public VoiceFile(Integer id, String voiceFileId, String voiceFileName,
			String voiceFilePath, String uploadTime, String uploadUserId,
			String voiceType, String reserve1, String reserve2) {
		super();
		this.id = id;
		voice_file_id = voiceFileId;
		voice_file_name = voiceFileName;
		voice_file_path = voiceFilePath;
		upload_time = uploadTime;
		upload_user_id = uploadUserId;
		voice_type = voiceType;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVoice_file_id() {
		return voice_file_id;
	}

	public void setVoice_file_id(String voiceFileId) {
		voice_file_id = voiceFileId;
	}

	public String getVoice_file_name() {
		return voice_file_name;
	}

	public void setVoice_file_name(String voiceFileName) {
		voice_file_name = voiceFileName;
	}

	public String getVoice_file_path() {
		return voice_file_path;
	}

	public void setVoice_file_path(String voiceFilePath) {
		voice_file_path = voiceFilePath;
	}

	public String getUpload_time() {
		return upload_time;
	}

	public void setUpload_time(String uploadTime) {
		upload_time = uploadTime;
	}

	public String getUpload_user_id() {
		return upload_user_id;
	}

	public void setUpload_user_id(String uploadUserId) {
		upload_user_id = uploadUserId;
	}

	public String getVoice_type() {
		return voice_type;
	}

	public void setVoice_type(String voiceType) {
		voice_type = voiceType;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	@Override
	public String toString() {
		return "VoiceFile [id=" + id + ", reserve1=" + reserve1 + ", reserve2="
				+ reserve2 + ", upload_time=" + upload_time
				+ ", upload_user_id=" + upload_user_id + ", voice_file_id="
				+ voice_file_id + ", voice_file_name=" + voice_file_name
				+ ", voice_file_path=" + voice_file_path + ", voice_type="
				+ voice_type + "]";
	}
	
	
}
