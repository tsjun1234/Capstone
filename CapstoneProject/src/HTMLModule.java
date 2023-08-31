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
					bodyTag.add(new TagObject(selectedTag,p.x,p.y,attribute,content));
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
			if(type.equals("Free")) {
				new FreeDialog(application.frame, type, module);
			}
			else {
				new TagsDialog(application.frame,type,tags.get(type), module);
			}
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
			String[]Text= {"<h1>","<h2>","<h3>","<h4>","<h5>","<h6>","<p>","<b>","<i>"
					,"<strong>","<mark>","<em>","<ins>","<del>","<s>","<u>","<sup>","<sub>","<small>","<br>","<hr>","<abbr>","<wbr>","<blockquote>","<q>"
					,"<dfn>","<pre>","<var>","<samp>","<kbd>","<code>","<ruby>","<rp>","<rt>"};
			tags.put("Text", Text);
			String[]List= {"<ul>","<li>","<ol>","<dl>","<dt>","<dd>"};
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
			Set<String> keys=tags.keySet();
			Iterator<String> it=keys.iterator();
			while(it.hasNext()) {
				String key=it.next();
				Arrays.sort(tags.get(key));
			}
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
			result="<"+tmp+">"+content+"</"+tmp+">";
		return result;
	}
}

//태그 목록 대화상자를 정의함
class TagsDialog extends JDialog{
	//전역 속성
	String[]Global= {"","accesskey","class","contenteditable","data-*","dir","draggable","hidden","id","lang","spellcheck","style","tabindex","title","translate"};
	String[]Event= {"",
			"onafterprint","onbeforeprint","onbeforeunload","onerror","onhashchange","onload","onmessage","onoffline","ononline","onpagehide","onpageshow"
			,"onpopstate","onresize","onstorage","onunload"
			,"onblur","onchange","oncontextmenu","onfocus","oninput","oninvalid","onreset","onsearch","onselect","onsubmit"
			,"onkeydown","onkeypress","onkeyup"
			,"onclick","ondblclick","onmousedown","onmousemove","onmouseout","onmouseover","onmouseup","onwheel"
			,"ondrag","ondragend","ondragenter","ondragleave","ondragover","ondragstart","ondrop","onscroll"
			,"oncopy","oncut","onpaste"
			,"onabort","oncanplay","oncanplaythrough","oncuechange","ondurationchange","onemptied","onended","onerror","onloadeddata","onloadedmetadata","onloadstart"
			,"onpause","onplay","onplaying","onprogress","onratechange","onseeked","onseeking","onstalled","onsuspend","ontimeupdate","onvolumechange","onwaiting"
			,"ontoggle"};
	HashMap<String, String[]> attributes=new HashMap<String, String[]>();
	
	//대화상자를 만들고 해당하는 태그 목록만큼 버튼을 생성
	//버튼을 클릭시 어떤 태그를 선택했는지 HTML모듈의 selectedTag에 저장 후 종료
	public TagsDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		int length=list.length;
		setLayout(new GridLayout(((length-5<=0)?1:length/5+1), 5));
		setBounds(20,100,800,200);
		makeAttributeMap(title, list);
		//목록들에 대한 버튼을 만들고 액션리스너를 추가
		JButton[]buttons=new JButton[list.length];
		for(int i=0;i<length;i++) {
			buttons[i]=new JButton(list[i]);
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tmp=e.getActionCommand();
					module.selectedTag=tmp;
					String[]tmpList= attributes.get(tmp);
					if(!(title.equals("<svg>"))) {
						tmpList=concat(Global,tmpList);
					}
					new SetTagDialog(frame, tmp.substring(1,tmp.length()-1), Global,Event,attributes.get(tmp),module);
					//속성선택창 출력
					//new SelectAttributeDialog(frame, tmp+"속성 설정", concat(Global,attributes.get(tmp)), module);
					//취소가 선택되었다면 다시 태그선택
					if(module.attribute.equals("cancel")) {
						return;
					}
					//몇몇 태그의 경우 내용이 필요없으므로 아닌경우에만 내용입력창 출력
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
	void makeAttributeMap(String category, String[] tagList) {
		for(int i=0;i<tagList.length;i++) {
			System.out.println(tagList[i]);
		}
		switch(category) {
		//헤드 관련 태그 속성
		case "Head":
			String[]meta= {"charset","content","http-equiv","name"};
			attributes.put("<meta>", meta);
			String[]title= {};
			attributes.put("<title>", title);
			String[]link= {"crossorigin","href","hreflang","media","referrerpolicy","rel","sizes","title","type"};
			link=concat(Event, link);
			attributes.put("<link>", link);
			String[]style= {"media","type"};
			style=concat(Event, style);
			attributes.put("<style>", style);
			String[]script={"async","crossorigin","defer","integrity","nomodule","referrerpolicy","src","type"};
			attributes.put("<script>", script);
		//글 관련 태그 속성
		case "Text":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<ins>")||tagList[i].equals("<del>")) {
					String[]tmp2={"cite","datetime"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<blockquote>")||tagList[i].equals("<q>")) {
					String[]tmp2={"cite"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//리스트 관련 태그
		case "List":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<li>")) {
					String[]tmp2={"value"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<ol>")) {
					String[]tmp2={"start","reversed","type"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//링크/이미지 관련 태그
		case "Link/Image":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<a>")) {
					String[]tmp2={"download","href","hreflang","media","ping","referrerpolicy","rel","target","type"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<img>")) {
					String[]tmp2={"alt","crossorigin","height","ismap","loading","longdesc","referrerpolicy","sizes","src","srcset","usemap","width"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<progress>")) {
					String[]tmp2={"max","value"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("svg")) {
					continue;
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//테이블 관련 태그
		case "Table":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<col>")||tagList[i].equals("<colgroup>")) {
					String[]tmp2={"span"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<td>")) {
					String[]tmp2={"colspan","headers","rowspan"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<th>")) {
					String[]tmp2={"colspan","headers","rowspan","abbr","scope"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//형식 관련 태그
		case "Form":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<button>")) {
					String[]tmp2={"autofocus","disabled","form","formaction","formenctype","formmethod","formnovalidate","formtarget","name","type","value"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<fieldset>")) {
					String[]tmp2={"disabled","form","name"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<form>")) {
					String[]tmp2={"accept-charset","action","autocomplete","enctype","method","name","novalidate","rel","target"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<input>")) {
					String[]tmp2={"accept","alt","autocomplete","autofocus","checked","dirname","disabled","form","formaction","formenctype",
							"formmethod","formnovalidate","formtarget","height","list","max","maxlength","min","minlength","multiple","name",
							"pattern","placeholder","readonly","required","size","src","step","type","value","width"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<label>")) {
					String[]tmp2={"for","form"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<output>")) {
					String[]tmp2={"for","form","name"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<select>")) {
					String[]tmp2={"autofocus","disabled","form","multiple","name","required","size"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<textarea>")) {
					String[]tmp2={"autofocus","cols","dirname","disabled","form","maxlength","name","placeholder","readonly","required","rows","wrap"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//멀티미디어 관련 태그
		case "Multimedia":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<audio>")) {
					String[]tmp2={"autoplay","controls","loop","muted","preload","src"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<canvas>")) {
					String[]tmp2={"height","width"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<video>")) {
					String[]tmp2={"autoplay","controls","height","loop","muted","poster","preload","src","width"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//임베디드 관련 태그
		case "Embeded":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<embeded>")) {
					String[]tmp2={"height","src","type","width"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<object>")) {
					String[]tmp2={"data","form","height","name","type","typemustmatch","usemap","width"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//프레임 관련 태그
		case "Frame":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<iframe>")) {
					String[]tmp2={"allow","allowfullscreen","allowpaymentrequest","height","loading","name","referrerpolicy","sandbox","src","srcdoc","width"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//기타 태그
		case "Other":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				if(tagList[i].equals("<base>")) {
					String[]tmp2={"href","target"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<script>")) {
					String[]tmp2={"async","crossorigin","defer","integrity","nomodule","referrerpolicy","src","type"};
					tmp=concat(tmp,tmp2);
					attributes.put(tagList[i], tmp);
					continue;
				}
				if(tagList[i].equals("<style>")) {
					String[]tmp2={"media","type"};
					tmp=concat(tmp,tmp2);
				}
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//의미 관련 태그 속성
		case "Semantic":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//사용자가 자유롭게 입력
		case "Free":
			String[] tmp= {""};
			attributes.put("<Free>", tmp);
			break;
		}
		
	}
	
	//문자열 배열 두 개를 받아 합친 다음 사전식으로 정렬하는 함수
	String[]concat(String[]arr1,String[]arr2){
		String[]tmp=new String[arr1.length+arr2.length];
		int i=0;
		for(int j=0;j<arr1.length;j++, i++) {
			tmp[i]=arr1[j];
		}
		for(int j=0;j<arr2.length;j++, i++) {
			tmp[i]=arr2[j];
		}
		Arrays.sort(tmp);
		return tmp;
	}
}

class SetTagDialog extends JDialog{
	static String[]empty={""};
	public SetTagDialog(JFrame frame, String title, String[]Global, String[]Event, String[]list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,550);
		module.attribute="";
		module.content="";
		
		//안내패널
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("<"+title+"></"+title+">");
		tagPanel.add(tagLabel);
		
		//속성패널
		JPanel attributePanel=new JPanel();
		JPanel BoxPanel=new JPanel();
		JLabel attributeLabel=new JLabel("속성과 값을 입력");
		JLabel attributeTmp=new JLabel("");
		JLabel selected=new JLabel("");
		JComboBox<String>attributeGlobalBox=new JComboBox<String>(Global);
		attributeGlobalBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JComboBox<String>attributeEventBox=new JComboBox<String>(Event);
		attributeEventBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JComboBox<String>attributeBox=new JComboBox<String>(list);
		attributeBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		final JTextField attributeText=new JTextField("여기에 속성 값 입력");
		attributeText.setSize(20, 10);
		JButton addAttributeButton=new JButton("추가");
		addAttributeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attributeTmp.setText(attributeTmp.getText()+" "+selected.getText()+"=\'"+attributeText.getText()+"\'");
			}
		});
		
		BoxPanel.add(attributeGlobalBox);
		BoxPanel.add(attributeEventBox);
		BoxPanel.add(attributeBox);
		BoxPanel.setLayout(new GridLayout(3,1));
		
		attributePanel.add(attributeLabel);
		attributePanel.add(BoxPanel);
		attributePanel.add(attributeText);
		attributePanel.add(addAttributeButton);
		attributePanel.setLayout(new GridLayout(4,1));
		
		//내용패널
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("태그 사이에 들어갈 내용 입력");
		JLabel contentTmp=new JLabel("");
		final JTextField content=new JTextField("여기에 내용 입력");
		content.setSize(20, 10);
		content.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {	
				contentTmp.setText(content.getText());
				tagLabel.setText("<"+title+attributeTmp.getText()+">"+contentTmp.getText()+"</"+title+">");
			}
			public void keyReleased(KeyEvent e) {}
		});
		contentPanel.setLayout(new GridLayout(2,1));
		contentPanel.add(contentLabel);
		contentPanel.add(content);
		
		//태그의 완성을 결정하는 패널
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("확인");
		JButton cancelButton=new JButton("취소");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.selectedTag=module.attribute=module.content="";
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
		
		GridBagLayout grid=new GridBagLayout();
		GridBagConstraints c=new GridBagConstraints();
		setLayout(grid);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=1;
		c.gridheight=1;
		grid.setConstraints(tagPanel, c);
		add(tagPanel);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=2;
		c.gridheight=1;
		grid.setConstraints(attributePanel, c);
		add(attributePanel);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=3;
		c.gridheight=1;
		grid.setConstraints(contentPanel, c);
		add(contentPanel);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=4;
		c.gridheight=1;
		grid.setConstraints(buttonPanel, c);
		add(buttonPanel);
		
		setVisible(true);
		pack();
	}
}

//속성 선택
class SelectAttributeDialog extends JDialog{
	public SelectAttributeDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,150);
		module.attribute="";
		
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
		module.content="";
		
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
				dispose();
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

//내용 입력
class FreeDialog extends JDialog{
	public FreeDialog(JFrame frame, String title, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,250);
		module.attribute="";
		module.content="";
		
		//안내패널
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("사용할 태그를 입력(<>제외)");
		final JTextField tag=new JTextField("여기에 태그 입력");
		tag.setSize(20, 10);
		tagPanel.add(tagLabel);
		tagPanel.add(tag);
		
		//속성패널
		JPanel attributePanel=new JPanel();
		JLabel attributeLabel=new JLabel("속성과 값을 입력");
		final JTextField attribute=new JTextField("ex)name='홍길동'");
		attribute.setSize(20, 10);
		attributePanel.add(attributeLabel);
		attributePanel.add(attribute);
		
		//내용패널
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("태그 사이에 들어갈 내용 입력");
		final JTextField content=new JTextField("여기에 내용 입력");
		content.setSize(20, 10);
		contentPanel.add(contentLabel);
		contentPanel.add(content);
		
		//실제 동작을 수행할 패널
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("확인");
		JButton cancelButton=new JButton("취소");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.selectedTag=module.attribute=module.content="";
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
		
		setLayout(new GridLayout(4,1));
		add(tagPanel);
		add(attributePanel);
		add(contentPanel);
		add(buttonPanel);
		
		setVisible(true);
		pack();
	}
}