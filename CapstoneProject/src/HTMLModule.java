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
					bodyTag.add(new TagObject(selectedTag,p.x,p.y,"",""));
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
			new TagsDialog(application.frame,type+"�±� ���",tags.get(type), module);
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
			result="<"+tmp+"></"+tmp+">";
		return result;
	}
}

//�±� ��� ��ȭ���ڸ� ������
class TagsDialog extends JDialog{
	HashMap<String, String[]> attributes=new HashMap<String, String[]>();
	//��ȭ���ڸ� ����� �ش��ϴ� �±� ��ϸ�ŭ ��ư�� ����
	//��ư�� Ŭ���� � �±׸� �����ߴ��� HTML����� selectedTag�� ���� �� ����
	public TagsDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		int length=list.length;
		setLayout(new GridLayout(((length-5<=0)?1:length/5+1), 5));
		setBounds(20,100,800,200);
		makeAttributeMap();
		//��ϵ鿡 ���� ��ư�� ����� �׼Ǹ����ʸ� �߰�
		JButton[]buttons=new JButton[list.length];
		for(int i=0;i<length;i++) {
			buttons[i]=new JButton(list[i]);
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tmp=e.getActionCommand();
					module.selectedTag=tmp;
					//�Ӽ� ��ȭ���� ��� �� �Ӽ����� ����
					new SelectAttributeDialog(frame, tmp+"�Ӽ� ����", attributes.get(tmp), module);
					//��Ұ� ���õǾ��ٸ� �ٽ� �±׼���
					if(module.attribute.equals("cancel")) {
						return;
					}
					//��� �±��� ��� ������ �ʿ�����Ƿ� head�±װ� �ƴѰ�쿡�� �����Է�â ���
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
	void makeAttributeMap() {
		String[]meta= {"charset","content","http-equiv","name"};
		attributes.put("<meta>", meta);
		String[]title= {};
		attributes.put("<title>", title);
	}
}

//�Ӽ� ����
class SelectAttributeDialog extends JDialog{
	public SelectAttributeDialog(JFrame frame, String title, String[] list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,150);
		
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
