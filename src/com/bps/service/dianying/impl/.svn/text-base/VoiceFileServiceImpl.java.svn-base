package com.bps.service.dianying.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bps.dal.dao.dianying.VoiceFileDao;
import com.bps.dal.object.dianying.VoiceFile;
import com.bps.exception.BaseException;
import com.bps.service.dianying.VoiceFileService;
import com.bps.util.DateUtil;
import com.bps.util.RedisUtil;

public class VoiceFileServiceImpl implements  VoiceFileService  {
	@Resource
	private VoiceFileDao voiceFileDao;
	@Resource
	private RedisUtil redisUtil;

	/* (non-Javadoc)
	 * @see com.bps.service.dianying.VoiceFileService#getFManagerFile()
	 */
	@Override
	public String getFFile(String agentId,String sex) throws BaseException {
		// TODO Auto-generated method stub
		String jsonData="{\"ec\":\"0\",\"em\":\"\",\"cd\":[";
		String redisFManagerValue=redisUtil.getJedis().get(RedisUtil.F_MANAGER_FILE);//取redis中管理员上传的语音
		boolean falg1=false;
		boolean falg2=false;
		if(redisFManagerValue==null || "".equals(redisFManagerValue)){
			HashMap<String, Object> parMap=new HashMap<String, Object>();
			parMap.put("voice_level", "1");
			parMap.put("voice_type", sex);
			List<VoiceFile>  FvoicefileList = voiceFileDao.getVoiceFileList(parMap);//查询数据库中管理员上传的语音
			String FVoiceFileJson="";
			if(!FvoicefileList.isEmpty() && FvoicefileList.size()>0){
				FVoiceFileJson="{\"mode\": \"manager\",\"url\":\""+FvoicefileList.get(0).getVoice_file_path()+"\",\"list\":[";
				for (int i = 0; i < FvoicefileList.size(); i++) {
					VoiceFile M=FvoicefileList.get(i);
					if(i==(FvoicefileList.size()-1)){
						FVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"}";
					}else{
						FVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"},";
					}
				}
				FVoiceFileJson+="]}";
				falg1=true;
				jsonData+=FVoiceFileJson;
				redisUtil.getJedis().set(RedisUtil.F_MANAGER_FILE,FVoiceFileJson);
			}
		}else{
			falg1=true;
			jsonData+=redisFManagerValue;
		}
		
		String redisFGroupValue=redisUtil.getJedis().get(RedisUtil.F_GROUP_FILE);//取redis中业务组长上传的语音
		if("".equals(redisFGroupValue) || redisFGroupValue==null){
			HashMap<String, Object> parMap=new HashMap<String, Object>();
			parMap.put("voice_level", "2");
			parMap.put("voice_type", "F");
			List<VoiceFile>  FvoicefileList = voiceFileDao.getVoiceFileList(parMap);//查询数据库中管理员上传的语音
			String FVoiceFileJson="";
			if(!FvoicefileList.isEmpty() && FvoicefileList.size()>0){
				FVoiceFileJson="{\"mode\": \"group\",\"url\":\""+FvoicefileList.get(0).getVoice_file_path()+"\",\"list\":[";
				for (int i = 0; i < FvoicefileList.size(); i++) {
					VoiceFile M=FvoicefileList.get(i);
					if(i==(FvoicefileList.size()-1)){
						FVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"}";
					}else{
						FVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"},";
					}
				}
				FVoiceFileJson+="]}";
				falg2=true;
				if(falg1){
					jsonData+=","+FVoiceFileJson;
				}else{
					jsonData+=FVoiceFileJson;
				}
				redisUtil.getJedis().set(RedisUtil.F_GROUP_FILE,FVoiceFileJson);
			}
		}else{
			if(falg1){
				jsonData+=","+redisFGroupValue;
			}else{
				jsonData+=redisFGroupValue;
			}
		}
		
		if(agentId!=null && !"".equals(agentId)){
			String redisAgentIdValue=redisUtil.getJedis().hget(RedisUtil.AGENT_FILE,agentId);//取redis中营销员上传的定制语音
			if("".equals(redisAgentIdValue) || redisAgentIdValue==null){
				HashMap<String, Object> parMap=new HashMap<String, Object>();
				parMap.put("voice_level", "3");
				parMap.put("upload_user_id", agentId);
				List<VoiceFile>  AgentVoicefileList = voiceFileDao.getVoiceFileList(parMap);//查询数据库中管理员上传的语音
				String AgentVoiceFileJson="";
				if(!AgentVoicefileList.isEmpty() && AgentVoicefileList.size()>0){
					AgentVoiceFileJson="{\"mode\": \"agent\",\"url\":\""+AgentVoicefileList.get(0).getVoice_file_path()+"\",\"list\":[";
					for (int i = 0; i < AgentVoicefileList.size(); i++) {
						VoiceFile M=AgentVoicefileList.get(i);
						if(i==(AgentVoicefileList.size()-1)){
							AgentVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"}";
						}else{
							AgentVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"},";
						}
					}
					AgentVoiceFileJson+="]}";
					if(falg1 || falg2){
						jsonData+=","+AgentVoiceFileJson;
					}else{
						jsonData+=AgentVoiceFileJson;
					}
					redisUtil.getJedis().hset(RedisUtil.AGENT_FILE,agentId,AgentVoiceFileJson);
				}
			}else{
				if(falg1 || falg2){
					jsonData+=","+redisAgentIdValue;
				}else{
					jsonData+=redisAgentIdValue;
				}
			}
		}
		jsonData+="]}";
		return jsonData;
	}

	
	@Override
	public String getMFile(String agentId,String sex) throws BaseException {
		// TODO Auto-generated method stub
		String jsonData="{\"ec\":\"0\",\"em\":\"\",\"cd\":[";
		String redisMManagerValue=redisUtil.getJedis().get(RedisUtil.M_MANAGER_FILE);//取redis中管理员上传的语音
		boolean falg1=false;
		boolean falg2=false;
		if(redisMManagerValue==null || "".equals(redisMManagerValue)){
			HashMap<String, Object> parMap=new HashMap<String, Object>();
			parMap.put("voice_level", "1");
			parMap.put("voice_type", sex);
			List<VoiceFile>  MvoicefileList = voiceFileDao.getVoiceFileList(parMap);//查询数据库中管理员上传的语音
			String MVoiceFileJson="";
			if(!MvoicefileList.isEmpty() && MvoicefileList.size()>0){
				MVoiceFileJson="{\"mode\": \"manager\",\"url\":\""+MvoicefileList.get(0).getVoice_file_path()+"\",\"list\":[";
				for (int i = 0; i < MvoicefileList.size(); i++) {
					VoiceFile M=MvoicefileList.get(i);
					if(i==(MvoicefileList.size()-1)){
						MVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"}";
					}else{
						MVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"},";
					}
				}
				MVoiceFileJson+="]}";
				falg1=true;
				redisUtil.getJedis().set(RedisUtil.F_MANAGER_FILE,MVoiceFileJson);
				jsonData+=MVoiceFileJson;
			}
		}else{
			falg1=true;
			jsonData+=redisMManagerValue;
		}
		
		String redisMGroupValue=redisUtil.getJedis().get(RedisUtil.M_GROUP_FILE);//取redis中业务组长上传的语音
		if("".equals(redisMGroupValue) || redisMGroupValue==null){
			HashMap<String, Object> parMap=new HashMap<String, Object>();
			parMap.put("voice_level", "2");
			parMap.put("voice_type", "M");
			List<VoiceFile>  MvoicefileList = voiceFileDao.getVoiceFileList(parMap);//查询数据库中管理员上传的语音
			String MVoiceFileJson="";
			if(!MvoicefileList.isEmpty() && MvoicefileList.size()>0){
				MVoiceFileJson="{\"mode\": \"group\",\"url\":\""+MvoicefileList.get(0).getVoice_file_path()+"\",\"list\":[";
				for (int i = 0; i < MvoicefileList.size(); i++) {
					VoiceFile M=MvoicefileList.get(i);
					if(i==(MvoicefileList.size()-1)){
						MVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"}";
					}else{
						MVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"},";
					}
				}
				MVoiceFileJson+="]}";
				falg2=true;
				if(falg1){
					jsonData+=","+MVoiceFileJson;
				}else{
					jsonData+=MVoiceFileJson;
				}
				redisUtil.getJedis().set(RedisUtil.F_GROUP_FILE,MVoiceFileJson);
			}
		}else{
			if(falg1){
				jsonData+=","+redisMGroupValue;
			}else{
				jsonData+=redisMGroupValue;
			}
		}
		
		if(agentId!=null && !"".equals(agentId)){
			String redisAgentIdValue=redisUtil.getJedis().hget(RedisUtil.AGENT_FILE,agentId);//取redis中营销员上传的定制语音
			if("".equals(redisAgentIdValue) || redisAgentIdValue==null){
				HashMap<String, Object> parMap=new HashMap<String, Object>();
				parMap.put("voice_level", "3");
				parMap.put("upload_user_id", agentId);
				List<VoiceFile>  AgentVoicefileList = voiceFileDao.getVoiceFileList(parMap);//查询数据库中管理员上传的语音
				String AgentVoiceFileJson="";
				if(!AgentVoicefileList.isEmpty() && AgentVoicefileList.size()>0){
					AgentVoiceFileJson="{\"mode\": \"agent\",\"url\":\""+AgentVoicefileList.get(0).getVoice_file_path()+"\",\"list\":[";
					for (int i = 0; i < AgentVoicefileList.size(); i++) {
						VoiceFile M=AgentVoicefileList.get(i);
						if(i==(AgentVoicefileList.size()-1)){
							AgentVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"}";
						}else{
							AgentVoiceFileJson+="{\"id\":\""+M.getId()+"\",\"voice_file_name\":\""+M.getVoice_file_name()+"\"},";
						}
					}
					AgentVoiceFileJson+="]}";
					if(falg1 || falg2){
						jsonData+=","+AgentVoiceFileJson;
					}else{
						jsonData+=AgentVoiceFileJson;
					}
					redisUtil.getJedis().hset(RedisUtil.AGENT_FILE,agentId,AgentVoiceFileJson);
				}
			}else{
				if(falg1 || falg2){
					jsonData+=","+redisAgentIdValue;
				}else{
					jsonData+=redisAgentIdValue;
				}
			}
		}
		jsonData+="]}";
		return jsonData;
	}
}
