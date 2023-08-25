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
		makeTagMap();
		
		//�׸��� ����
		JScrollPane scrollPane=new JScrollPane(objects, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10,10,720,360);
		
		mainPanel.add(tools,"North");
		mainPanel.add(scrollPane,"Center");
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//���Ͽ� ����� �±׵��� �ҷ��� �׸��ǿ� �׸�
	public void loadHTML() {
		
	}
	
	//��������� �±׵��� ���Ͽ� ������
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
				if(selected.charAt(0)=='<') {
					bodyTag.add(new TagObject(selected,p.x,p.y,""));
				}
				//��������� bodyTag���� ���(�����)
				for(int i=0;i<bodyTag.size();i++) {
					bodyTag.get(i).makeTag();
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
			TagsDialog dialog=new TagsDialog(application.frame,type+"�±�",tags.get(type), module);
			dialog.setVisible(true);
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

//�±� ��ȭ���ڸ� ������
class TagsDialog extends JDialog{
	//��ȭ���ڸ� ����� �ش��ϴ� �±� ��ϸ�ŭ ��ư�� ����
	//��ư�� Ŭ���� � �±׸� �����ߴ��� HTML����� selected�� ���� �� ����
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
