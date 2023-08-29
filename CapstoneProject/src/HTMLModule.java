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
		makeTagMap();
		
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
		
		//그림판 설정
		JScrollPane scrollPane=new JScrollPane(objects, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10,10,720,360);
		
		mainPanel.add(tools,"North");
		mainPanel.add(scrollPane,"Center");
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//파일에 저장된 태그들을 불러와 그림판에 그림
	public void loadHTML(FileModule fileModule) {
		String []tmp=fileModule.LoadFile();
	}
	
	//현재까지의 태그들을 파일에 저장함
	public String[] getHTMLTags() {
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
				if(selectedTag.charAt(0)=='<') {
					bodyTag.add(new TagObject(selectedTag,p.x,p.y,"",""));
				}
				//현재까지의 bodyTag내용 출력(디버깅)
				for(int i=0;i<bodyTag.size();i++) {
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
			new TagsDialog(application.frame,type+"태그 목록",tags.get(type), module);
			//헤드 태그의 경우 위치 지정이 필요없으므로 바로 태그목록에 추가
			if(type.equals("Head")) {
				headTag.add(new TagObject(selectedTag, attribute, content));
			}
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
	String content;
	TagObject Parent;
	TagObject[]childerens;
	//클래스 생성자
	TagObject(){
		this("<div>",0,0,"","");
	}
	TagObject(String tag, String tagOption, String content){
		this(tag, 0, 0, tagOption, content);
	}
	TagObject(String tag, int x, int y, String tagOption, String content){
		this.tag=tag;
		this.x=x;
		this.y=y;
		this.tagOption=tagOption;
		this.content=content;
	}
	//클래스에 저장되어 있는 태그값을 기초로 값을 반환함
	String getTag() {
		String tmp=tag.substring(1,tag.length()-1);
		if(tagOption!="")
			result="<"+tmp+" "+tagOption+">"+content+"</"+tmp+">";
		else
			result="<"+tmp+"></"+tmp+">";
		return result;
	}
}

//태그 목록 대화상자를 정의함
class TagsDialog extends JDialog{
	HashMap<String, String[]> attributes=new HashMap<String, String[]>();
	//대화상자를 만들고 해당하는 태그 목록만큼 버튼을 생성
	//버튼을 클릭시 어떤 태그를 선택했는지 HTML모듈의 selectedTag에 저장 후 종료
	public TagsDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		int length=list.length;
		setLayout(new GridLayout(((length-5<=0)?1:length/5+1), 5));
		setBounds(20,100,800,200);
		makeAttributeMap();
		//목록들에 대한 버튼을 만들고 액션리스너를 추가
		JButton[]buttons=new JButton[list.length];
		for(int i=0;i<length;i++) {
			buttons[i]=new JButton(list[i]);
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tmp=e.getActionCommand();
					module.selectedTag=tmp;
					//속성 대화상자 출력 후 속성값을 저장
					new SelectAttributeDialog(frame, tmp+"속성 설정", attributes.get(tmp), module);
					//취소가 선택되었다면 다시 태그선택
					if(module.attribute.equals("cancel")) {
						return;
					}
					//몇몇 태그의 경우 내용이 필요없으므로 head태그가 아닌경우에만 내용입력창 출력
					if(tmp.equals("<br>")||tmp.equals("<img>")||tmp.equals("<meta>")||tmp.equals("<link>")||tmp.equals("<input>")||tmp.equals("<hr>")) {
						dispose();
						return;
					}
					//내용 대화상자 출력 후 내용을 저장
					new ContentDialog(frame,"내용 입력",module);
					//취소가 되었다면 종료
					if(module.content.equals("cancel")) {
						return;
					}
					dispose();
				}
			});
			add(buttons[i]);
		}
		pack();
		setVisible(true);
	}
	//속성사전을 만드는 함수
	void makeAttributeMap() {
		String[]meta= {"charset","content","http-equiv","name"};
		attributes.put("<meta>", meta);
		String[]title= {};
		attributes.put("<title>", title);
	}
}

//속성 선택
class SelectAttributeDialog extends JDialog{
	public SelectAttributeDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,150);
		
		//속성 선택 리스트 패널
		JPanel comboPanel=new JPanel();
		comboPanel.setLayout(new FlowLayout());
		JLabel label=new JLabel(title,JLabel.CENTER);
		JComboBox<String>attributeBox=new JComboBox<String>(list);
		attributeBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		comboPanel.add(label);
		comboPanel.add(attributeBox);
		
		//속성값을 입력받고 현재까지의 속성값을 출력하는 패널
		JPanel textPanel=new JPanel();
		final JTextField text=new JTextField("여기에 속성 값 입력");
		text.setSize(50, 20);
		JLabel now=new JLabel("");
		now.setHorizontalAlignment(JLabel.CENTER);
		textPanel.setLayout(new BorderLayout());
		textPanel.add(text, BorderLayout.NORTH);
		textPanel.add(now, BorderLayout.SOUTH);
		
		//동작을 구현할 버튼이 있는 패널
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("추가");
		JButton endButton=new JButton("확인");
		JButton cancelButton=new JButton("취소");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				now.setText(now.getText()+" "+attributeBox.getSelectedItem()+"=\""+text.getText()+"\"");
			}
		});
		endButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.attribute=now.getText();
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.attribute="cancel";
				dispose();
			}
		});
		buttonPanel.add(addButton);
		buttonPanel.add(endButton);
		buttonPanel.add(cancelButton);
		
		setLayout(new BorderLayout());
		add(comboPanel, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
		pack();
	}
	
}

//내용 입력
class ContentDialog extends JDialog{
	public ContentDialog(JFrame frame, String title, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,150);
		//안내패널
		JPanel labelPanel=new JPanel();
		JLabel label=new JLabel("내용 입력");
		labelPanel.add(label);
		
		//내용을 입력받을 필드
		final JTextField text=new JTextField("여기에 내용 입력");
		text.setSize(20, 10);
		
		//실제 동작을 수행할 패널
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("확인");
		JButton cancelButton=new JButton("취소");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.content=text.getText();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.content="cancel";
				dispose();
			}
		});
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
		
		setLayout(new BorderLayout());
		add(labelPanel, BorderLayout.NORTH);
		add(text, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		pack();
	}
}
