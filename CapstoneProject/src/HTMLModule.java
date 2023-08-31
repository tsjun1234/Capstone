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
	
	//���� HTML������ �ۼ��ϱ����� �غ�
	public void makeNewHTML(JFrame frame, JPanel mainPanel) {
		mainPanel.removeAll();
		this.frame=frame;
		this.mainPanel=mainPanel;
		makeTagMap();
		
		createHTMLInterface();
	}
	
	//HTML���� �������̽��� ȣ����
	void createHTMLInterface() {
		//ȭ���� ����â�� �׸������� ����
		JToolBar tools=new JToolBar();
		JPanel objects=new CanvasPanel();
		
		//������ ������
		tools.setBackground(Color.gray);
		objects.setBackground(Color.white);
		
		//����â����
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
		
		//�׸��� ����
		JScrollPane scrollPane=new JScrollPane(objects, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10,10,720,360);
		
		mainPanel.add(tools,"North");
		mainPanel.add(scrollPane,"Center");
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//���Ͽ� ����� �±׵��� �ҷ��� �׸��ǿ� �׸�
	public void loadHTML(FileModule fileModule) {
		String []tmp=fileModule.LoadFile();
	}
	
	//��������� �±׵��� ���Ͽ� ������
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
	
	//ĵ���� ������ �� �г�
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
		
		//������
		class CanvasMouseListener implements MouseMotionListener, MouseListener{
			//������ġ�� Start���Ϳ� ����
			public void mousePressed(MouseEvent e) {
				Point p=e.getPoint();
				Start.add(p);
				Canvas.repaint();
			}
			//������ġ�� End�� ������ bodyTag�� �߰�
			public void mouseReleased(MouseEvent e) {
				Point p=e.getPoint();
				End.add(p);
				//�±װ� ���õǾ��ִ� ��쿡�� bodyTag�� �߰�
				if(selectedTag.charAt(0)=='<') {
					bodyTag.add(new TagObject(selectedTag,p.x,p.y,attribute,content));
				}
				//��������� bodyTag���� ���(�����)
				for(int i=0;i<bodyTag.size();i++) {
					System.out.println(bodyTag.get(i).getTag());
				}
				Canvas.repaint();
			}
			//�巡�װ����� �׸����� ���
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
			//���� ȭ�� �����
			super.paintComponent(g);
			//ȭ�� �׸���
			if(Start.size()!=0) {
				//���� �巡�� �ϴ� ���� �ƴϸ� ��ü�� ���
				if(Start.size()==End.size()) {
					for(int i=0;i<Start.size();i++) {
						g.drawLine(Start.get(i).x, Start.get(i).y, End.get(i).x, End.get(i).y);
					}
				}
				//���� �巡�� �ϴ� ���̹Ƿ� ��ü�� �������� �±� ���
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
	
	//�±� ������ �������� �� �ش� ������ �´� ��ȭ���ڸ� �����ϴ� ������
	class ToolActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String type=e.getActionCommand();
			if(type.equals("Free")) {
				new FreeDialog(application.frame, type, module);
			}
			else {
				new TagsDialog(application.frame,type,tags.get(type), module);
			}
			//��� �±��� ��� ��ġ ������ �ʿ�����Ƿ� �ٷ� �±׸�Ͽ� �߰�
			if(type.equals("Head")) {
				headTag.add(new TagObject(selectedTag, attribute, content));
			}
		}
	}
	
	//�±� ������ ����� �Լ�
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

//���Ͽ� ����� �±׸� Ŭ����ȭ
class TagObject{
	String tag;
	int x, y;
	String tagOption;
	String result;
	String content;
	TagObject Parent;
	TagObject[]childerens;
	//Ŭ���� ������
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
	//Ŭ������ ����Ǿ� �ִ� �±װ��� ���ʷ� ���� ��ȯ��
	String getTag() {
		String tmp=tag.substring(1,tag.length()-1);
		if(tagOption!="")
			result="<"+tmp+" "+tagOption+">"+content+"</"+tmp+">";
		else
			result="<"+tmp+">"+content+"</"+tmp+">";
		return result;
	}
}

//�±� ��� ��ȭ���ڸ� ������
class TagsDialog extends JDialog{
	//���� �Ӽ�
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
	
	//��ȭ���ڸ� ����� �ش��ϴ� �±� ��ϸ�ŭ ��ư�� ����
	//��ư�� Ŭ���� � �±׸� �����ߴ��� HTML����� selectedTag�� ���� �� ����
	public TagsDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		int length=list.length;
		setLayout(new GridLayout(((length-5<=0)?1:length/5+1), 5));
		setBounds(20,100,800,200);
		makeAttributeMap(title, list);
		//��ϵ鿡 ���� ��ư�� ����� �׼Ǹ����ʸ� �߰�
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
					//�Ӽ�����â ���
					//new SelectAttributeDialog(frame, tmp+"�Ӽ� ����", concat(Global,attributes.get(tmp)), module);
					//��Ұ� ���õǾ��ٸ� �ٽ� �±׼���
					if(module.attribute.equals("cancel")) {
						return;
					}
					//��� �±��� ��� ������ �ʿ�����Ƿ� �ƴѰ�쿡�� �����Է�â ���
					if(tmp.equals("<br>")||tmp.equals("<img>")||tmp.equals("<meta>")||tmp.equals("<link>")||tmp.equals("<input>")||tmp.equals("<hr>")) {
						dispose();
						return;
					}
					//���� ��ȭ���� ��� �� ������ ����
					new ContentDialog(frame,"���� �Է�",module);
					//��Ұ� �Ǿ��ٸ� ����
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
	//�Ӽ������� ����� �Լ�
	void makeAttributeMap(String category, String[] tagList) {
		for(int i=0;i<tagList.length;i++) {
			System.out.println(tagList[i]);
		}
		switch(category) {
		//��� ���� �±� �Ӽ�
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
		//�� ���� �±� �Ӽ�
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
		//����Ʈ ���� �±�
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
		//��ũ/�̹��� ���� �±�
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
		//���̺� ���� �±�
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
		//���� ���� �±�
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
		//��Ƽ�̵�� ���� �±�
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
		//�Ӻ���� ���� �±�
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
		//������ ���� �±�
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
		//��Ÿ �±�
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
		//�ǹ� ���� �±� �Ӽ�
		case "Semantic":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {};
				tmp=concat(tmp,Event);
				attributes.put(tagList[i], tmp);
			}
			break;
		//����ڰ� �����Ӱ� �Է�
		case "Free":
			String[] tmp= {""};
			attributes.put("<Free>", tmp);
			break;
		}
		
	}
	
	//���ڿ� �迭 �� ���� �޾� ��ģ ���� ���������� �����ϴ� �Լ�
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
		
		//�ȳ��г�
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("<"+title+"></"+title+">");
		tagPanel.add(tagLabel);
		
		//�Ӽ��г�
		JPanel attributePanel=new JPanel();
		JPanel BoxPanel=new JPanel();
		JLabel attributeLabel=new JLabel("�Ӽ��� ���� �Է�");
		JLabel attributeTmp=new JLabel("");
		JLabel selected=new JLabel("");
		JComboBox<String>attributeGlobalBox=new JComboBox<String>(Global);
		attributeGlobalBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JComboBox<String>attributeEventBox=new JComboBox<String>(Event);
		attributeEventBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JComboBox<String>attributeBox=new JComboBox<String>(list);
		attributeBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		final JTextField attributeText=new JTextField("���⿡ �Ӽ� �� �Է�");
		attributeText.setSize(20, 10);
		JButton addAttributeButton=new JButton("�߰�");
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
		
		//�����г�
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("�±� ���̿� �� ���� �Է�");
		JLabel contentTmp=new JLabel("");
		final JTextField content=new JTextField("���⿡ ���� �Է�");
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
		
		//�±��� �ϼ��� �����ϴ� �г�
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("Ȯ��");
		JButton cancelButton=new JButton("���");
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

//�Ӽ� ����
class SelectAttributeDialog extends JDialog{
	public SelectAttributeDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,150);
		module.attribute="";
		
		//�Ӽ� ���� ����Ʈ �г�
		JPanel comboPanel=new JPanel();
		comboPanel.setLayout(new FlowLayout());
		JLabel label=new JLabel(title,JLabel.CENTER);
		JComboBox<String>attributeBox=new JComboBox<String>(list);
		attributeBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		comboPanel.add(label);
		comboPanel.add(attributeBox);
		
		//�Ӽ����� �Է¹ް� ��������� �Ӽ����� ����ϴ� �г�
		JPanel textPanel=new JPanel();
		final JTextField text=new JTextField("���⿡ �Ӽ� �� �Է�");
		text.setSize(50, 20);
		JLabel now=new JLabel("");
		now.setHorizontalAlignment(JLabel.CENTER);
		textPanel.setLayout(new BorderLayout());
		textPanel.add(text, BorderLayout.NORTH);
		textPanel.add(now, BorderLayout.SOUTH);
		
		//������ ������ ��ư�� �ִ� �г�
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("�߰�");
		JButton endButton=new JButton("Ȯ��");
		JButton cancelButton=new JButton("���");
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

//���� �Է�
class ContentDialog extends JDialog{
	public ContentDialog(JFrame frame, String title, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,150);
		module.content="";
		
		//�ȳ��г�
		JPanel labelPanel=new JPanel();
		JLabel label=new JLabel("���� �Է�");
		labelPanel.add(label);
		
		//������ �Է¹��� �ʵ�
		final JTextField text=new JTextField("���⿡ ���� �Է�");
		text.setSize(20, 10);
		
		//���� ������ ������ �г�
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("Ȯ��");
		JButton cancelButton=new JButton("���");
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

//���� �Է�
class FreeDialog extends JDialog{
	public FreeDialog(JFrame frame, String title, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,250);
		module.attribute="";
		module.content="";
		
		//�ȳ��г�
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("����� �±׸� �Է�(<>����)");
		final JTextField tag=new JTextField("���⿡ �±� �Է�");
		tag.setSize(20, 10);
		tagPanel.add(tagLabel);
		tagPanel.add(tag);
		
		//�Ӽ��г�
		JPanel attributePanel=new JPanel();
		JLabel attributeLabel=new JLabel("�Ӽ��� ���� �Է�");
		final JTextField attribute=new JTextField("ex)name='ȫ�浿'");
		attribute.setSize(20, 10);
		attributePanel.add(attributeLabel);
		attributePanel.add(attribute);
		
		//�����г�
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("�±� ���̿� �� ���� �Է�");
		final JTextField content=new JTextField("���⿡ ���� �Է�");
		content.setSize(20, 10);
		contentPanel.add(contentLabel);
		contentPanel.add(content);
		
		//���� ������ ������ �г�
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("Ȯ��");
		JButton cancelButton=new JButton("���");
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