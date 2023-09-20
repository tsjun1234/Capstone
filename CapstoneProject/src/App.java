import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class App extends JFrame {
	Vector<String> MenuItems=new Vector<String>();
	JFrame frame;
	JPanel mainPanel;
	FileModule fileModule;
	HTMLModule htmlModule;
	SettingObject defaultSetting;
	App application=this;
	//메인 페이지 생성
	public App() {
		frame=new JFrame();
		frame.setTitle("제목 없음 - 웹 페이지 생성기");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		mainPanel=new JPanel();
		frame.add(mainPanel);
		frame.setVisible(true);
		fileModule=new FileModule(application);
		htmlModule=null;
		defaultSetting=new SettingObject(application);
	}
	
	//프로그램상단의 메뉴바를 만드는 부분
	void createMenu() {
		//메뉴바 생성
		JMenuBar bar=new JMenuBar();
		
		//타입별로 메뉴 생성
		String [] titles= {"파일", "HTML", "CSS", "JavaScript", "환경설정"};
		JMenu []bars=new JMenu[titles.length];
		for(int i=0;i<bars.length;i++) {
			bars[i]=new JMenu(titles[i]);
			bar.add(bars[i]);
		}
		
		//리스너 생성
		MenuActionListener listener=new MenuActionListener();
		
		//타입별로 서브메뉴 생성
		//파일 메뉴
		String [] fileMenuItem= {"새 파일", "파일 읽기", "파일 저장", "종료"};
		JMenuItem[]tmps=new JMenuItem[fileMenuItem.length];
		char[]fileMenuAccel= {'N', 'O', 'S', 'X'};
		for(int i=0;i<fileMenuItem.length;i++) {
			MenuItems.add(fileMenuItem[i]);
			tmps[i]=new JMenuItem(fileMenuItem[i]);
			tmps[i].addActionListener(listener);
			tmps[i].setAccelerator(KeyStroke.getKeyStroke(fileMenuAccel[i],Event.CTRL_MASK));
			bars[0].add(tmps[i]);
		}
		
		//HTML 메뉴
		String [] HTMLMenuItem= {"HTML 기본설정"};
		tmps=new JMenuItem[HTMLMenuItem.length];
		char[]HTMLMenuAccel= {};
		for(int i=0;i<HTMLMenuItem.length;i++) {
			MenuItems.add(HTMLMenuItem[i]);
			tmps[i]=new JMenuItem(HTMLMenuItem[i]);
			tmps[i].addActionListener(listener);
			//tmps[i].setAccelerator(KeyStroke.getKeyStroke(HTMLMenuAccel[i],Event.CTRL_MASK));
			bars[1].add(tmps[i]);
		}
		
		//메뉴바를 부착
		frame.setJMenuBar(bar);
	}
	
	void makeNewTask() {
		htmlModule=new HTMLModule(application);
	}
	
	//메뉴바의 액션리스너 선택된 메뉴아이템에 따라 다르게 행동
	class MenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String type=e.getActionCommand();
			System.out.println(type);
			
			//파일 메뉴의 서브메뉴가 선택됨
			//새 파일을 만들 때 사용하는 함수
			if(type.equals(MenuItems.get(0))) {
				if(htmlModule!=null) {
					int val=JOptionPane.showConfirmDialog(null,"이전에 작성하던 파일이 있습니다. 저장하지않고 새로 작성하시겠습니까?","경고창",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
					if(val==1)
						return;
				}
				makeNewTask();
				htmlModule.makeNewHTML(frame, mainPanel);
			}
			//파일을 읽어들일 때 사용하는 함수
			if(type.equals(MenuItems.get(1))) {
				makeNewTask();
				htmlModule.makeNewHTML(frame, mainPanel);
				htmlModule.loadHTML(fileModule);
			}
			//파일을 저장할 때 사용하는 함수
			if(type.equals(MenuItems.get(2))) {
				fileModule.saveHTML(htmlModule);
			}
			//작업을 종료할 때 사용하는 함수
			if(type.equals(MenuItems.get(3))) {
				
			}
			//HTML 기본 환경설정
			if(type.equals(MenuItems.get(4))) {
				
			}
		}
	}
	
}
