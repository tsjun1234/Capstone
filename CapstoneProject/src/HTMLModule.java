import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class HTMLModule {
	App application;
	HTMLModule module=this;
	JFrame frame;
	JPanel mainPanel;
	String selected = "";
	HashMap<String, String[]> tags=new HashMap<String,String[]>();
	Vector<TagObject> headTag=new Vector<TagObject>();
	Vector<TagObject> bodyTag=new Vector<TagObject>();
	public HTMLModule(App app) {
		application=app;
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
		JPanel objects=new CanvasPanel();
		
		//배경색을 설정함
		tools.setBackground(Color.gray);
		objects.setBackground(Color.white);
		
		//도구창설정
		tools.setFloatable(false);
		String [] toolsLabel= {"Head","Text","List","Link/Image","Table","Form","Multimedia","Embeded","Frame","Other","Semantic","Free"};
		JButton[] toolsButtons=new JButton[toolsLabel.length];
		tools.setLayout(new GridLayout(1, toolsLabel.length, 0, 0));
		ToolActionListener toolListener=new ToolActionListener();
		for(int i=0;i<toolsLabel.length;i++) {
			toolsButtons[i]=new JButton(toolsLabel[i]);
			toolsButtons[i].addActionListener(toolListener);
			tools.add(toolsButtons[i]);
		}
		makeTagMap();
		
		//그림판 설정
		JScrollPane scrollPane=new JScrollPane(objects, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10,10,720,360);
		
		mainPanel.add(tools,"North");
		mainPanel.add(scrollPane,"Center");
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//파일에 저장된 태그들을 불러와 그림판에 그림
	public void loadHTML() {
		
	}
	
	//현재까지의 태그들을 파일에 저장함
	public String getHTMLTags() {
		String result="";
		for(int i=0;i<headTag.size();i++) {
			result=result+headTag.get(i).getTag();
		}
		for(int i=0;i<bodyTag.size();i++) {
			result=result+bodyTag.get(i).getTag();
		}
		return result;
	}
	
	//캔버스 역할을 할 패널
	class CanvasPanel extends JPanel{
		CanvasPanel Canvas=this;
		Vector<Point> Start=new Vector<Point>();
		Vector<Point> End=new Vector<Point>();
		Point Drag;
		public CanvasPanel() {
			CanvasMouseListener mouseListener=new CanvasMouseListener();
			Canvas.addMouseListener(mouseListener);
			Canvas.addMouseMotionListener(mouseListener);
		}
		
		//리스너
		class CanvasMouseListener implements MouseMotionListener, MouseListener{
			//시작위치를 Start벡터에 저장
			public void mousePressed(MouseEvent e) {
				Point p=e.getPoint();
				Start.add(p);
				Canvas.repaint();
			}
			//종료위치를 End에 저장후 bodyTag에 추가
			public void mouseReleased(MouseEvent e) {
				Point p=e.getPoint();
				End.add(p);
				//태그가 선택되어있는 경우에만 bodyTag에 추가
				if(selected.charAt(0)=='<') {
					bodyTag.add(new TagObject(selected,p.x,p.y,""));
				}
				//현재까지의 bodyTag내용 출력(디버깅)
				for(int i=0;i<bodyTag.size();i++) {
					bodyTag.get(i).makeTag();
					System.out.println(bodyTag.get(i).getTag());
				}
				Canvas.repaint();
			}
			//드래그과정을 그림으로 출력
			public void mouseDragged(MouseEvent e) {
				Drag=e.getPoint();
				Canvas.repaint();
			}
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseMoved(MouseEvent e) {}
		}
		
		public void paintComponent(Graphics g) {
			//이전 화면 지우기
			super.paintComponent(g);
			//화면 그리기
			if(Start.size()!=0) {
				//현재 드래그 하는 중이 아니면 전체를 출력
				if(Start.size()==End.size()) {
					for(int i=0;i<Start.size();i++) {
						g.drawLine(Start.get(i).x, Start.get(i).y, End.get(i).x, End.get(i).y);
					}
				}
				//현재 드래그 하는 중이므로 전체와 생성중인 태그 출력
				else{
					int i;
					for(i=0;i<Start.size()-1;i++) {
						g.drawLine(Start.get(i).x, Start.get(i).y, End.get(i).x, End.get(i).y);
					}
					if(Drag!=null) {
						g.drawLine(Start.get(i).x, Start.get(i).y, Drag.x, Start.get(i).y);
						g.drawLine(Start.get(i).x, Start.get(i).y, Start.get(i).x, Drag.y);
						g.drawLine(Start.get(i).x, Drag.y, Drag.x, Drag.y);
						g.drawLine(Drag.x, Start.get(i).y, Drag.x, Drag.y);
					}
				}
				
			}
		}
	}
	
	//태그 종류를 선택했을 시 해당 종류에 맞는 대화상자를 생성하는 리스너
	class ToolActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String type=e.getActionCommand();
			TagsDialog dialog=new TagsDialog(application.frame,type+"태그",tags.get(type), module);
			dialog.setVisible(true);
		}
	}
	
	//태그 사전을 만드는 함수
	void makeTagMap() {
			String[]Head= {"<meta>","<title>","<link>","<style>","<script>"};
			tags.put("Head", Head);
			String[]Text= {"<hgroup>","<h1>","<h2>","<h3>","<h4>","<h5>","<h6>","<p>","<b>","<i>"
					,"<strong>","<strike>","<mark>","<em>","<ins>","<del>","<s>","<u>","<sup>","<sub>","<small>","<br>","<hr>","<abbr>","<wbr>","<blockquote>","<q>"
					,"<dfn>","<pre>","<var>","<samp>","<kbd>","<code>","<ruby>","<rp>","<rt>","<rb>","<rtc>"};
			tags.put("Text", Text);
			String[]List= {"<ul>","<li>","<ol>","<dl>","<dt>","<dd>","<menu>"};
			tags.put("List", List);
			String[]LinkImage= {"<a>","<img>","<svg>","<progress>","<picture>"};
			tags.put("Link/Image", LinkImage);
			String[]Table= {"<table>","<tr>","<td>","<th>","<caption>","<colgroup>","<col>","<thead>","<tbody>","<tfoot>"};
			tags.put("Table",Table);
			String[]Form= {"<form>","<input>","<textarea>","<button>","<output>","<datalist>","<select>","<fieldset>","<label>"};
			tags.put("Form", Form);
			String[]Multimedia= {"<video>","<audio>","<canvas>"};
			tags.put("Multimedia", Multimedia);
			String[]Embeded= {"<object>","<embeded>"};
			tags.put("Embeded", Embeded);
			String[]Frame= {"<iframe>"};
			tags.put("Frame", Frame);
			String[]Other= {"<base>","<style>","<script>","<div>","<span>","<figure>","<figcaption>"};
			tags.put("Other", Other);
			String[]Semantic= {"<header>","<nav>","<main>","<article>","<section>","<aside>","<footer>",};
			tags.put("Semantic", Semantic);
			String[]Free= {};
			tags.put("Free", Free);
		}

}
//파일에 저장될 태그를 클래스화
class TagObject{
	String tag;
	int x, y;
	String tagOption;
	String result;
	TagObject(){
		this("<div>",0,0,"");
	}
	TagObject(String tag, int x, int y, String tagOption){
		this.tag=tag;
		this.x=x;
		this.y=y;
		this.tagOption=tagOption;
	}
	void makeTag() {
		String tmp=tag.substring(1,tag.length()-1);
		result="<"+tmp+" "+tagOption+">"+"</"+tmp+">";
	}
	String getTag() {return result;}
}

//태그 대화상자를 정의함
class TagsDialog extends JDialog{
	//대화상자를 만들고 해당하는 태그 목록만큼 버튼을 생성
	//버튼을 클릭시 어떤 태그를 선택했는지 HTML모듈의 selected에 저장 후 종료
	public TagsDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		int length=list.length;
		setLayout(new GridLayout(((length-5<=0)?1:length/5+1), 5));
		setBounds(20,100,800,200);
		JButton[]buttons=new JButton[list.length];
		for(int i=0;i<length;i++) {
			buttons[i]=new JButton(list[i]);
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					module.selected=e.getActionCommand();
					dispose();
				}
			});
			add(buttons[i]);
		}
		pack();
	}
}
