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
	
	//새로 HTML파일을 작성하기위한 준비
	public void makeNewHTML(JFrame frame, JPanel mainPanel) {
		mainPanel.removeAll();
		this.frame=frame;
		this.mainPanel=mainPanel;
		createHTMLInterface();
	}
	
	//HTML편집 인터페이스를 호출함
	void createHTMLInterface() {
		//화면을 도구창과 그림판으로 나눔
		JToolBar tools=new JToolBar();
		JPanel toolPanel=new JPanel();
		JPanel objects=new CanvasPanel();
		
		//배경색을 설정함
		tools.setBackground(Color.gray);
		objects.setBackground(Color.white);
		
		//도구창설정
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
		
		//그림판 설정
		JScrollPane scrollPane=new JScrollPane(objects, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(toolPanel,"North");
		mainPanel.add(scrollPane);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//파일에 저장된 태그들을 불러와 그림판에 그림
	public void loadHTML(FileModule fileModule) {
		String []tmp=fileModule.LoadFile();
	}
	
	//캔버스 역할을 할 패널
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
		
		//리스너
		class CanvasMouseListener implements MouseMotionListener, MouseListener{
			//시작위치를 Start벡터에 저장
			public void mousePressed(MouseEvent e) {
				Start=e.getPoint();
				Canvas.repaint();
			}
			//종료위치를 End에 저장후 bodyTag에 추가
			public void mouseReleased(MouseEvent e) {
				End=e.getPoint();
				//태그가 선택되어있는 경우에만 bodyTag에 추가
				if(selectedTag!="") {
					System.out.println("클릭"+Start.x+" "+Start.y);
					//임시 태그 객체를 생성
					TagObject tmpTag=new TagObject(selectedTag, Start, End, module.attribute, module.content, module.width, module.height);
					//따로태그가 설정되어있지 않다면 자동으로 body태그를 부모태그로 추가
					if(selectedObject==null) {
						selectedObject=bodyTag.get(0);
					}
					System.out.println(selectedObject.tag);
					tmpTag.setParent(selectedObject);
					bodyTag.add(tmpTag);
				}
				//현재까지의 bodyTag내용 출력(디버깅)
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
			//드래그과정을 그림으로 출력
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
		
		//현재 선으로만 임시 출력
		//태그에 맞게 그래픽으로 변환 필요
		public void paintComponent(Graphics g) {
			//이전 화면 지우기
			super.paintComponent(g);
			//화면 그리기
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
	//태그 종류를 선택했을 시 해당 종류에 맞는 대화상자를 생성하는 리스너
	class ToolActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String type=e.getActionCommand();
			if(type.equals("Free")) {
				new FreeDialog(application.frame, type, module);
			}
			else {
				new TagsDialog(application.frame,type,HTMLSettings.get(type), module);
			}
			//헤드 태그의 경우 위치 지정이 필요없으므로 바로 태그목록에 추가
			if(type.equals("Head")) {
				headTag.add(new TagObject(selectedTag, attribute, content));
			}
		}
	}
	//태그를 그래픽화
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
	//현재까지의 태그들을 파일에 저장함
		public String[] getHTMLTags() {
			//head와 body태그를 구분하ㅕㅇ 저장
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






