import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class HTMLModule {
	App application;
	HTMLModule module=this;
	JFrame frame;
	JPanel mainPanel;
	String selectedTag = "";
	String attribute="";
	String content="";
	int width, height;
	HashMap<String, String[]> HTMLSettings;
	Vector<TagObject> headTag=new Vector<TagObject>();
	Vector<TagObject> bodyTag=new Vector<TagObject>();
	public HTMLModule(App app) {
		application=app;
		HTMLSettings=app.defaultSetting.HTMLSetting;
	}
	
	//���� HTML������ �ۼ��ϱ����� �غ�
	public void makeNewHTML(JFrame frame, JPanel mainPanel) {
		mainPanel.removeAll();
		this.frame=frame;
		this.mainPanel=mainPanel;
		createHTMLInterface();
	}
	
	//HTML���� �������̽��� ȣ����
	void createHTMLInterface() {
		//ȭ���� ����â�� �׸������� ����
		JToolBar tools=new JToolBar();
		JPanel toolPanel=new JPanel();
		JPanel objects=new CanvasPanel();
		
		//������ ������
		tools.setBackground(Color.gray);
		objects.setBackground(Color.white);
		
		//����â����
		tools.setFloatable(false);
		String [] toolsLabel= HTMLSettings.get("Tag Category");
		JButton[] toolsButtons=new JButton[toolsLabel.length];
		tools.setLayout(new GridLayout(1, toolsLabel.length, 0, 0));
		ToolActionListener toolListener=new ToolActionListener();
		for(int i=0;i<toolsLabel.length;i++) {
			toolsButtons[i]=new JButton(toolsLabel[i]);
			toolsButtons[i].addActionListener(toolListener);
			tools.add(toolsButtons[i]);
		}
		toolPanel.setLayout(new BorderLayout());
		toolPanel.add(tools,"North");
		
		//�׸��� ����
		JScrollPane scrollPane=new JScrollPane(objects, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(toolPanel,"North");
		mainPanel.add(scrollPane);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//���Ͽ� ����� �±׵��� �ҷ��� �׸��ǿ� �׸�
	public void loadHTML(FileModule fileModule) {
		String []tmp=fileModule.LoadFile();
	}
	
	//ĵ���� ������ �� �г�
	class CanvasPanel extends JPanel{
		CanvasPanel Canvas=this;
		Point Start=null;
		Point End=null;
		TagObject selectedObject;
		public CanvasPanel() {
			CanvasMouseListener mouseListener=new CanvasMouseListener();
			Canvas.addMouseListener(mouseListener);
			Canvas.addMouseMotionListener(mouseListener);
			bodyTag.add(new TagObject("<body>", new Point(0,0), new Point(0,0), "", "", mainPanel.getWidth(), mainPanel.getHeight()));
			selectedObject=null;
		}
		
		//������
		class CanvasMouseListener implements MouseMotionListener, MouseListener{
			//������ġ�� Start���Ϳ� ����
			public void mousePressed(MouseEvent e) {
				Start=e.getPoint();
				Canvas.repaint();
			}
			//������ġ�� End�� ������ bodyTag�� �߰�
			public void mouseReleased(MouseEvent e) {
				End=e.getPoint();
				//�±װ� ���õǾ��ִ� ��쿡�� bodyTag�� �߰�
				if(selectedTag!="") {
					System.out.println("Ŭ��"+Start.x+" "+Start.y);
					//�ӽ� �±� ��ü�� ����
					TagObject tmpTag=new TagObject(selectedTag, Start, End, module.attribute, module.content, module.width, module.height);
					//�����±װ� �����Ǿ����� �ʴٸ� �ڵ����� body�±׸� �θ��±׷� �߰�
					if(selectedObject==null) {
						selectedObject=bodyTag.get(0);
					}
					System.out.println(selectedObject.tag);
					tmpTag.setParent(selectedObject);
					bodyTag.add(tmpTag);
				}
				//��������� bodyTag���� ���(�����)
				for(int i=0;i<bodyTag.size();i++) {
					System.out.println(bodyTag.get(i).getTag());
				}
				Start=null;
				End=null;
				module.selectedTag="";
				module.attribute="";
				module.content="";
				module.width=module.height=0;
				Canvas.repaint();
			}
			//�巡�װ����� �׸����� ���
			public void mouseDragged(MouseEvent e) {
				End=e.getPoint();
				Canvas.repaint();
			}
			public void mouseClicked(MouseEvent e) {
				Point tmp=e.getPoint();
				selectedObject=bodyTag.get(0).checkLocation(tmp.x, tmp.y);
				System.out.println(selectedObject.tag);
				System.out.println(selectedObject.x);
				System.out.println(selectedObject.y);
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseMoved(MouseEvent e) {}
		}
		
		//���� �����θ� �ӽ� ���
		//�±׿� �°� �׷������� ��ȯ �ʿ�
		public void paintComponent(Graphics g) {
			//���� ȭ�� �����
			super.paintComponent(g);
			//ȭ�� �׸���
			if(bodyTag.size()!=0) {
				for(int i=0;i<bodyTag.size();i++) {
					drawTag(bodyTag.get(i),g);
				}
			}
			if(selectedObject!=null) {
				g.setColor(Color.red);
				drawBox(selectedObject, g);
				g.setColor(Color.black);
			}
			if(Start!=null&&End!=null) {
				g.drawRect(Start.x, Start.y, End.x-Start.x, End.y-Start.y);
			}
		}
	}
	//�±� ������ �������� �� �ش� ������ �´� ��ȭ���ڸ� �����ϴ� ������
	class ToolActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String type=e.getActionCommand();
			if(type.equals("Free")) {
				new FreeDialog(application.frame, type, module);
			}
			else {
				new TagsDialog(application.frame,type,HTMLSettings.get(type), module);
			}
			//��� �±��� ��� ��ġ ������ �ʿ�����Ƿ� �ٷ� �±׸�Ͽ� �߰�
			if(type.equals("Head")) {
				headTag.add(new TagObject(selectedTag, attribute, content));
			}
		}
	}
	//�±׸� �׷���ȭ
	void drawTag(TagObject t, Graphics g) {
		if(t.content!="") {
			drawText(t,g);
			return;
		}
		drawBox(t,g);
	}
	void drawText(TagObject t, Graphics g) {
		FontMetrics fm=g.getFontMetrics();
		g.drawString(t.content, t.x, t.y+t.height-fm.getLeading()-fm.getDescent());
	}
	
	void drawBox(TagObject t, Graphics g) {
		g.drawRect(t.x,t.y,t.width,t.height);
	}
	//��������� �±׵��� ���Ͽ� ������
		public String[] getHTMLTags() {
			//head�� body�±׸� �����ϤŤ� ����
			String []tmp=new String[2];
			tmp[0]=tmp[1]="";
			for(int i=0;i<headTag.size();i++) {
				tmp[0]=tmp[0]+headTag.get(i).getTag();
			}
			for(int i=0;i<bodyTag.size();i++) {
				tmp[1]=tmp[1]+bodyTag.get(i).getTag();
			}
			return tmp;
		}
}






