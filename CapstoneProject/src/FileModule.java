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
	String fileFilters[][]= {{"HTML Files","html"},{"CSS Files","css"},{"JavaScript Files","js"}};
	public FileModule(App app) {
		application=app;
	}
	
	//���ϸ�� Ȯ���ڸ� �ҷ����� �б�ȭ�� ���
	//���
	//tmp[0]:������ ��ġ
	//tmp[1]:������ �̸�
	//tmp[2]:������ Ȯ����
	//tmp[0]�� null�̸� �ߴ� �Ǵ� ��ҵ�
	String[]showLoadFile(){
		String []tmp=new String[3];
		tmp[0]="null";
		//���� �б�ȭ�� ��� �� ���� ����
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("���� �б�");
		jfc.setCurrentDirectory(new File(dest));
		for(int i=0;i<fileFilters.length;i++) 
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(fileFilters[i][0],fileFilters[i][1]));
		//html, css, js�� �ƴѰ�� ���� �ݺ�
		while(true) {
			//���� �б� ȭ���� ���
			int val=jfc.showOpenDialog(null);
			File file=jfc.getSelectedFile();
			//������ ���������� ���õ��� ���� or �ߴ� ��
			if(val!=JFileChooser.APPROVE_OPTION)
				return tmp;
			//Ȯ���ڰ� html, css, js�� ���õǾ��� ��� Ȯ���ڸ� �Ҵ�, �ƴ϶�� ��� ������ ����̹Ƿ� ���� ó��
			boolean flag=false;
			String extenstion="";
			for(int i=0;i<fileFilters.length;i++) {
				if(fileFilters[i][0].equals(jfc.getFileFilter().getDescription())) {
					extenstion=fileFilters[i][1];
					flag=true;
					break;
				}
			}
			//��� ���Ϸ� �б�� Ȯ���ڸ� ����
			String name=file.getName();
			if(!flag) {
				int extval=file.getName().lastIndexOf(".");
				extenstion=file.getName().substring(extval+1);
				name=name.substring(0,extval);
			}
			//Ȯ���ڰ� html, css, js�� �ƴѰ�� ���â ����� �ٽ� ����, Ȯ���ڰ� �´ٸ� ���� ��ȯ �� ����
			if((extenstion.equals(fileFilters[0][1]))||(extenstion.equals(fileFilters[1][1]))||(extenstion.equals(fileFilters[2][1]))) {
				tmp[0]=file.getParent();
				tmp[1]=file.getName();
				tmp[2]=extenstion;
				break;
			}
			else {
				JOptionPane.showMessageDialog(null,"html, css, js�� �ƴ� Ȯ�����Դϴ�. �ٽ� �������ּ���.","Ȯ���� ���",JOptionPane.WARNING_MESSAGE);
			}
		}
		return tmp;
	}
	
	//��θ� �޾� �ش��ϴ� ������ �о����
	//���
	//�� Ȯ���ڿ� �˸��� String ��ü �迭
	public String[] LoadFile() {
		//���� ���� ��ġ�� ������
		String[]tmp=new String[3];
		tmp=showLoadFile();
		//Ȯ���ڿ� ���� ��ȯ�ϴ� String ��ü �迭 ����
		switch(tmp[2]) {
		case"html":
			//html�� ũ�� head�� body�� �����Ǿ�����
			String []result=new String[2];
			String tmp2=ReadFile(tmp[0],tmp[1]);
			System.out.println(tmp2);
			//�����б⿡�� ������ �߻����� ��
			if(tmp2.equals("null")) {
				return tmp;
			}
			//head�±� ���̸� �߶�
			int start=tmp2.indexOf("<head>")+6;
			int end=tmp2.indexOf("</head>");
			result[0]=tmp2.substring(start, end);
			//body�±� ���̸� �߶�
			start=tmp2.indexOf("<body>")+6;
			end=tmp2.indexOf("</body>");
			result[1]=tmp2.substring(start, end);
			System.out.println(result[0]);
			System.out.println(result[1]);
			return result;
		case"css":
			break;
		case"js":
			break;
		}
		return tmp;
	}
	
	//���ϸ�� Ȯ���ڸ� �����ϴ� ����ȭ�� ���
	//���
	//tmp[0]:������ ��ġ
	//tmp[1]:������ �̸�
	//tmp[2]:������ Ȯ����
	//tmp[0]�� null�̸� �ߴ� �Ǵ� ��ҵ�
	String[]showSaveFile(){
		String []tmp=new String[3];
		tmp[0]="null";
		//���� ����ȭ�� ��� �� ���� ����
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("�� �̸����� ����");
		jfc.setCurrentDirectory(new File(dest));
		for(int i=0;i<fileFilters.length;i++) 
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(fileFilters[i][0],fileFilters[i][1]));
		//���� ����ȭ���� ���
		int val=jfc.showSaveDialog(null);
		File file=jfc.getSelectedFile();
		//������ ���������� ���õ��� ���� or �ߴܵ�
		if(val!=JFileChooser.APPROVE_OPTION)
			return tmp;
		//Ȯ���ڰ� html, css, js�� ���õǾ��� ��� Ȯ���ڸ� �Ҵ�, �ƴ϶�� ��� ������ ����̹Ƿ� ���� ó��
		boolean flag=false;
		String extenstion="";
		for(int i=0;i<fileFilters.length;i++) {
			if(fileFilters[i][0].equals(jfc.getFileFilter().getDescription())) {
				extenstion=fileFilters[i][1];
				flag=true;
				break;
			}
		}
		//��� ���Ϸ� ����� Ȯ���ڸ� ����
		String name=file.getName();
		if(!flag) {
			int extval=file.getName().lastIndexOf(".");
			//Ȯ���ڰ� �ִٸ� Ȯ���ڸ� ����
			if(extval!=-1) {
				extenstion=file.getName().substring(extval+1);
				name=name.substring(0,extval);
			}
			//Ȯ���ڰ� ���ٸ� ������ �⺻ Ȯ���ڷ� ����(html)
			else {
				extenstion=ext;
			}
		}
		//Ȯ���ڰ� html, css, js�� �ƴ�, ���â ���
		if(!((extenstion.equals(fileFilters[0][1]))||(extenstion.equals(fileFilters[1][1]))||(extenstion.equals(fileFilters[2][1])))) {
			int var=JOptionPane.showConfirmDialog(null,"html, css, js�� �ƴ� Ȯ�����Դϴ�. ��� �����Ͻðڽ��ϱ�?","Ȯ���� ���",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if(var==1)
				return tmp;
		}
		tmp[0]=file.getParent();
		tmp[1]=name;
		tmp[2]=extenstion;
		return tmp;
	}

	//��������� HTML���ϳ����� ������
	//�Է�
	//html:���ݱ��� �ۼ��� html���
	//���
	//WriteFile�Լ� �������� ���ϻ���
	public void saveHTML(HTMLModule html) {
		//���ϸ�� Ȯ���ڸ� �޾ƿ�
		String []tmp=showSaveFile();
		//�ߴܵǾ��ٸ� ���
		if(tmp[0].equals("null"))
			return;
		//html���κ��� �±׵��� �޾ƿ� ��ġ�� ���� �Ҵ��Ͽ� ������
		String result;
		String []arr=html.getHTMLTags();
		result="<html><head>"+arr[0]+"</head><body>"+arr[1]+"</body></html>";
		WriteFile(tmp[0], tmp[1], tmp[2], result);
	}
	
	//����ڷκ��� �ּҿ� Ȯ����, ������ �޾Ƽ� �ش� ��ġ�� ������ ����
	//�Է�
	//dest:���� ������
	//name:���� �̸�
	//ext:Ȯ����
	//contents:������ ����
	//���
	//�ش���ġ�� ���� ����
	void WriteFile(String dest, String name, String ext, String contents) {
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
	
	//����ڷκ��� �ּҿ� Ȯ����, ������ �޾Ƽ� �ش� ��ġ�� ������ ����
	//�Է�
	//dest:�б� ������
	//name:���� �̸�
	//ext:Ȯ����
	//���
	//���� ���� ��ȯ
	String ReadFile(String dest, String name) {
		FileReader fin=null;
		String result="";
		try {
			fin=new FileReader(dest+"\\"+name);
			int c;
			while((c=fin.read())!=-1) {
				result=result+(char)c;
			}
		}catch(IOException e) {
			System.out.println("����¿���");
			result="null";
		}
		return result;
	}
}