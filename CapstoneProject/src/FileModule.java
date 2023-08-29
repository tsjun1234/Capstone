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
	String fileFilters[][]= {{"HTML Files","html"},{"CSS Files","css"},{"JavaScript Files","js"}};
	public FileModule(App app) {
		application=app;
	}
	
	//파일명과 확장자를 불러오는 읽기화면 출력
	//출력
	//tmp[0]:파일의 위치
	//tmp[1]:파일의 이름
	//tmp[2]:파일의 확장자
	//tmp[0]이 null이면 중단 또는 취소됨
	String[]showLoadFile(){
		String []tmp=new String[3];
		tmp[0]="null";
		//파일 읽기화면 출력 전 필터 설정
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("파일 읽기");
		jfc.setCurrentDirectory(new File(dest));
		for(int i=0;i<fileFilters.length;i++) 
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(fileFilters[i][0],fileFilters[i][1]));
		//html, css, js가 아닌경우 무한 반복
		while(true) {
			//파일 읽기 화면을 출력
			int val=jfc.showOpenDialog(null);
			File file=jfc.getSelectedFile();
			//파일이 정상적으로 선택되지 않음 or 중단 됨
			if(val!=JFileChooser.APPROVE_OPTION)
				return tmp;
			//확장자가 html, css, js중 선택되었을 경우 확장자를 할당, 아니라면 모든 파일인 경우이므로 따로 처리
			boolean flag=false;
			String extenstion="";
			for(int i=0;i<fileFilters.length;i++) {
				if(fileFilters[i][0].equals(jfc.getFileFilter().getDescription())) {
					extenstion=fileFilters[i][1];
					flag=true;
					break;
				}
			}
			//모든 파일로 읽기시 확장자를 추출
			String name=file.getName();
			if(!flag) {
				int extval=file.getName().lastIndexOf(".");
				extenstion=file.getName().substring(extval+1);
				name=name.substring(0,extval);
			}
			//확장자가 html, css, js가 아닌경우 경고창 출력후 다시 선택, 확장자가 맞다면 값을 반환 후 종료
			if((extenstion.equals(fileFilters[0][1]))||(extenstion.equals(fileFilters[1][1]))||(extenstion.equals(fileFilters[2][1]))) {
				tmp[0]=file.getParent();
				tmp[1]=file.getName();
				tmp[2]=extenstion;
				break;
			}
			else {
				JOptionPane.showMessageDialog(null,"html, css, js가 아닌 확장자입니다. 다시 선택해주세요.","확장자 경고",JOptionPane.WARNING_MESSAGE);
			}
		}
		return tmp;
	}
	
	//경로를 받아 해당하는 파일을 읽어들임
	//출력
	//각 확장자에 알맞은 String 객체 배열
	public String[] LoadFile() {
		//읽을 파일 위치를 가져옴
		String[]tmp=new String[3];
		tmp=showLoadFile();
		//확장자에 따라서 반환하는 String 객체 배열 생성
		switch(tmp[2]) {
		case"html":
			//html은 크게 head와 body로 구성되어있음
			String []result=new String[2];
			String tmp2=ReadFile(tmp[0],tmp[1]);
			System.out.println(tmp2);
			//파일읽기에서 오류가 발생했을 때
			if(tmp2.equals("null")) {
				return tmp;
			}
			//head태그 사이를 잘라냄
			int start=tmp2.indexOf("<head>")+6;
			int end=tmp2.indexOf("</head>");
			result[0]=tmp2.substring(start, end);
			//body태그 사이를 잘라냄
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
	
	//파일명과 확장자를 지정하는 저장화면 출력
	//출력
	//tmp[0]:파일의 위치
	//tmp[1]:파일의 이름
	//tmp[2]:파일의 확장자
	//tmp[0]이 null이면 중단 또는 취소됨
	String[]showSaveFile(){
		String []tmp=new String[3];
		tmp[0]="null";
		//파일 저장화면 출력 전 필터 설정
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("새 이름으로 저장");
		jfc.setCurrentDirectory(new File(dest));
		for(int i=0;i<fileFilters.length;i++) 
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(fileFilters[i][0],fileFilters[i][1]));
		//파일 저장화면을 출력
		int val=jfc.showSaveDialog(null);
		File file=jfc.getSelectedFile();
		//파일이 정상적으로 선택되지 않음 or 중단됨
		if(val!=JFileChooser.APPROVE_OPTION)
			return tmp;
		//확장자가 html, css, js중 선택되었을 경우 확장자를 할당, 아니라면 모든 파일인 경우이므로 따로 처리
		boolean flag=false;
		String extenstion="";
		for(int i=0;i<fileFilters.length;i++) {
			if(fileFilters[i][0].equals(jfc.getFileFilter().getDescription())) {
				extenstion=fileFilters[i][1];
				flag=true;
				break;
			}
		}
		//모든 파일로 저장시 확장자를 추출
		String name=file.getName();
		if(!flag) {
			int extval=file.getName().lastIndexOf(".");
			//확장자가 있다면 확장자를 추출
			if(extval!=-1) {
				extenstion=file.getName().substring(extval+1);
				name=name.substring(0,extval);
			}
			//확장자가 없다면 설정된 기본 확장자로 변경(html)
			else {
				extenstion=ext;
			}
		}
		//확장자가 html, css, js가 아님, 경고창 출력
		if(!((extenstion.equals(fileFilters[0][1]))||(extenstion.equals(fileFilters[1][1]))||(extenstion.equals(fileFilters[2][1])))) {
			int var=JOptionPane.showConfirmDialog(null,"html, css, js가 아닌 확장자입니다. 계속 진행하시겠습니까?","확장자 경고",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if(var==1)
				return tmp;
		}
		tmp[0]=file.getParent();
		tmp[1]=name;
		tmp[2]=extenstion;
		return tmp;
	}

	//현재까지의 HTML파일내용을 저장함
	//입력
	//html:지금까지 작성된 html모듈
	//출력
	//WriteFile함수 실행으로 파일생성
	public void saveHTML(HTMLModule html) {
		//파일명과 확장자를 받아옴
		String []tmp=showSaveFile();
		//중단되었다면 취소
		if(tmp[0].equals("null"))
			return;
		//html모듈로부터 태그들을 받아와 위치에 각각 할당하여 저장함
		String result;
		String []arr=html.getHTMLTags();
		result="<html><head>"+arr[0]+"</head><body>"+arr[1]+"</body></html>";
		WriteFile(tmp[0], tmp[1], tmp[2], result);
	}
	
	//사용자로부터 주소와 확장자, 내용을 받아서 해당 위치에 파일을 생성
	//입력
	//dest:생성 목적지
	//name:파일 이름
	//ext:확장자
	//contents:파일의 내용
	//출력
	//해당위치에 파일 생성
	void WriteFile(String dest, String name, String ext, String contents) {
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
	
	//사용자로부터 주소와 확장자, 내용을 받아서 해당 위치의 파일을 읽음
	//입력
	//dest:읽기 목적지
	//name:파일 이름
	//ext:확장자
	//출력
	//읽은 값을 반환
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
			System.out.println("입출력오류");
			result="null";
		}
		return result;
	}
}