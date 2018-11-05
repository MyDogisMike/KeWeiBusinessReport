package com.bps.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bps.dal.object.bps.NewNumberMonitor;

public class FileUtil<T> {
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
	
	public void createFile(String path, String title, String[] headers, Collection<T> dataset, List<String> usefulFields){
		FileOutputStream out = null;
		ExcelExport<T> ex = new ExcelExport<T>();
		try{
			out = new FileOutputStream(path);
			HSSFWorkbook workbook = ex.exportExcel(title, headers, dataset, usefulFields);
			workbook.write(out);
			workbook.close();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
