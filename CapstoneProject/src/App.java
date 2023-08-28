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
	App application=this;
	//���� ������ ����
	public App() {
		frame=new JFrame();
		frame.setTitle("���� ���� - �� ������ ������");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
		frame.add(mainPanel);
		frame.setVisible(true);
		fileModule=null;
		htmlModule=null;
	}
	
	//���α׷������ �޴��ٸ� ����� �κ�
	void createMenu() {
		//�޴��� ����
		JMenuBar bar=new JMenuBar();
		
		//Ÿ�Ժ��� �޴� ����
		String [] titles= {"����", "HTML", "CSS", "JavaScript", "ȯ�漳��"};
		JMenu []bars=new JMenu[titles.length];
		for(int i=0;i<bars.length;i++) {
			bars[i]=new JMenu(titles[i]);
			bar.add(bars[i]);
		}
		
		//������ ����
		MenuActionListener listener=new MenuActionListener();
		
		//Ÿ�Ժ��� ����޴� ����
		//���� �޴�
		String [] tmp= {"�� ����", "���� �б�", "���� ����", "����"};
		JMenuItem[]tmps=new JMenuItem[tmp.length];
		char[]tmps2= {'N', 'O', 'S', 'X'};
		for(int i=0;i<tmp.length;i++) {
			MenuItems.add(tmp[i]);
			tmps[i]=new JMenuItem(tmp[i]);
			tmps[i].addActionListener(listener);
			tmps[i].setAccelerator(KeyStroke.getKeyStroke(tmps2[i],Event.CTRL_MASK));
			bars[0].add(tmps[i]);
		}
		
		//�޴��ٸ� ����
		frame.setJMenuBar(bar);
	}
	
	void makeNewTask() {
		fileModule=new FileModule(application);
		htmlModule=new HTMLModule(application);
	}
	
	//�޴����� �׼Ǹ����� ���õ� �޴������ۿ� ���� �ٸ��� �ൿ
	class MenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String type=e.getActionCommand();
			System.out.println(type);
			
			//���� �޴��� ����޴��� ���õ�
			//�� ������ ���� �� ����ϴ� �Լ�
			if(type.equals(MenuItems.get(0))) {
				if(htmlModule!=null) {
					int val=JOptionPane.showConfirmDialog(null,"������ �ۼ��ϴ� ������ �ֽ��ϴ�. ���������ʰ� ���� �ۼ��Ͻðڽ��ϱ�?","���â",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
					if(val==1)
						return;
				}
				makeNewTask();
				htmlModule.makeNewHTML(frame, mainPanel);
			}
			//������ �о���� �� ����ϴ� �Լ�
			if(type.equals(MenuItems.get(1))) {
				makeNewTask();
				htmlModule.makeNewHTML(frame, mainPanel);
				htmlModule.loadHTML(fileModule);
			}
			//������ ������ �� ����ϴ� �Լ�
			if(type.equals(MenuItems.get(2))) {
				fileModule.saveHTML(htmlModule);
			}
			//�۾��� ������ �� ����ϴ� �Լ�
			if(type.equals(MenuItems.get(3))) {
				
			}
		}
	}
	
}
