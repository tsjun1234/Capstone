import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FileModule{
	//기본경로와 형식
	String dest="c:\\Users\\User\\Desktop";
	String name="제목 없음";
	String ext="html";
	String contents="";
	App application;
	public FileModule(App app) {
		application=app;
	}

	//현재까지의 HTML파일내용을 저장함
	public void saveHTML(HTMLModule html) {
		String fileFilters[][]= {{"HTML Files","html"},{"CSS Files","css"},{"JavaScriptFiles","js"}};
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("새 이름으로 저장");
		jfc.setCurrentDirectory(new File(dest));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		for(int i=0;i<fileFilters.length;i++) 
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(fileFilters[i][0],fileFilters[i][1]));
		int val=jfc.showSaveDialog(null);
		jfc.setFileFilter(new FileNameExtensionFilter(fileFilters[0][0],fileFilters[0][1]));
		if(val!=JFileChooser.APPROVE_OPTION)
			return;
		System.out.println(jfc.getFileFilter().getDescription());
		File file=jfc.getSelectedFile();
		System.out.println(file.getParent());
		String result;
		String body=html.getHTMLTags();
		result="<html><head></head><body>"+body+"</body></html>";
		WriteFile(file.getParent(), file.getName(), ext, result);
	}
	
	//사용자로부터 주소와 확장자, 내용을 받아서 해당 위치에 파일을 생성
	public void WriteFile(String dest, String name, String ext, String contents) {
		FileWriter fout=null;
		try {
			fout=new FileWriter(dest+"\\"+name+"."+ext);
			fout.write(contents, 0, contents.length());
			fout.close();
		}catch(IOException e) {
			System.out.println(e);
			System.out.println("파일 쓰기 오류");
		}
	}
}