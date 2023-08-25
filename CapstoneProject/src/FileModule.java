import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FileModule{
	//�⺻��ο� ����
	String dest="c:\\Users\\User\\Desktop";
	String name="���� ����";
	String ext="html";
	String contents="";
	App application;
	public FileModule(App app) {
		application=app;
	}

	//��������� HTML���ϳ����� ������
	public void saveHTML(HTMLModule html) {
		String fileFilters[][]= {{"HTML Files","html"},{"CSS Files","css"},{"JavaScriptFiles","js"}};
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("�� �̸����� ����");
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
	
	//����ڷκ��� �ּҿ� Ȯ����, ������ �޾Ƽ� �ش� ��ġ�� ������ ����
	public void WriteFile(String dest, String name, String ext, String contents) {
		FileWriter fout=null;
		try {
			fout=new FileWriter(dest+"\\"+name+"."+ext);
			fout.write(contents, 0, contents.length());
			fout.close();
		}catch(IOException e) {
			System.out.println(e);
			System.out.println("���� ���� ����");
		}
	}
}