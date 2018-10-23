package com.bps.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {
//	public static void createFile(File file, String content){
//		FileWriter groupFileWriter = null;
//		BufferedWriter groupBufferedWriter = null;
//		try{
//			file.createNewFile();
//			groupFileWriter = new FileWriter(file);
//			groupBufferedWriter = new BufferedWriter(groupFileWriter);
//			groupBufferedWriter.write(content);
//			groupBufferedWriter.flush();
//		}catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//			if(groupFileWriter != null){
//				try {
//					groupFileWriter.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if(groupBufferedWriter != null){
//				try {
//					groupBufferedWriter.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	public static void createFile(String path, String content){
		OutputStreamWriter osw = null;
		BufferedWriter groupBufferedWriter = null;
		try{
			osw = new OutputStreamWriter(new FileOutputStream(path, true),"GBK");
			groupBufferedWriter = new BufferedWriter(osw);
			groupBufferedWriter.write(content);
			groupBufferedWriter.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(osw != null){
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(groupBufferedWriter != null){
				try {
					groupBufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
