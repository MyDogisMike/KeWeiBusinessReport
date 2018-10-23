package com.bps.service.dianying;

import com.bps.exception.BaseException;

public interface VoiceFileService {
	String getFFile(String agentId,String sex)throws BaseException;
	String getMFile(String agentId,String sex)throws BaseException;
}
