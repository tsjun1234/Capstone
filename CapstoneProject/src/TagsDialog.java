import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

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
					new SetTagDialog(frame, tmp.substring(1,tmp.length()-1), Global,Event,attributes.get(tmp),module);
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
		switch(category) {
		//��� ���� �±� �Ӽ�
		case "Head":
			String[]meta= {"","charset","content","http-equiv","name"};
			attributes.put("<meta>", meta);
			String[]title= {};
			attributes.put("<title>", title);
			String[]link= {"","crossorigin","href","hreflang","media","referrerpolicy","rel","sizes","title","type"};
			attributes.put("<link>", link);
			String[]style= {"","media","type"};
			attributes.put("<style>", style);
			String[]script={"","async","crossorigin","defer","integrity","nomodule","referrerpolicy","src","type"};
			attributes.put("<script>", script);
			break;
		//�� ���� �±� �Ӽ�
		case "Text":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
				if(tagList[i].equals("<ins>")||tagList[i].equals("<del>")) {
					String[]tmp2={"cite","datetime"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<blockquote>")||tagList[i].equals("<q>")) {
					String[]tmp2={"cite"};
					tmp=concat(tmp,tmp2);
				}
				attributes.put(tagList[i], tmp);
			}
			break;
		//����Ʈ ���� �±�
		case "List":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
				if(tagList[i].equals("<li>")) {
					String[]tmp2={"value"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<ol>")) {
					String[]tmp2={"start","reversed","type"};
					tmp=concat(tmp,tmp2);
				}
				attributes.put(tagList[i], tmp);
			}
			break;
		//��ũ/�̹��� ���� �±�
		case "Link/Image":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
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
				attributes.put(tagList[i], tmp);
			}
			break;
		//���̺� ���� �±�
		case "Table":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
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
				attributes.put(tagList[i], tmp);
			}
			break;
		//���� ���� �±�
		case "Form":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
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
				attributes.put(tagList[i], tmp);
			}
			break;
		//��Ƽ�̵�� ���� �±�
		case "Multimedia":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
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
				attributes.put(tagList[i], tmp);
			}
			break;
		//�Ӻ���� ���� �±�
		case "Embeded":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
				if(tagList[i].equals("<embeded>")) {
					String[]tmp2={"height","src","type","width"};
					tmp=concat(tmp,tmp2);
				}
				if(tagList[i].equals("<object>")) {
					String[]tmp2={"data","form","height","name","type","typemustmatch","usemap","width"};
					tmp=concat(tmp,tmp2);
				}
				attributes.put(tagList[i], tmp);
			}
			break;
		//������ ���� �±�
		case "Frame":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
				if(tagList[i].equals("<iframe>")) {
					String[]tmp2={"allow","allowfullscreen","allowpaymentrequest","height","loading","name","referrerpolicy","sandbox","src","srcdoc","width"};
					tmp=concat(tmp,tmp2);
				}
				attributes.put(tagList[i], tmp);
			}
			break;
		//��Ÿ �±�
		case "Other":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
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
				attributes.put(tagList[i], tmp);
			}
			break;
		//�ǹ� ���� �±� �Ӽ�
		case "Semantic":
			for(int i=0;i<tagList.length;i++) {
				String[] tmp= {""};
				
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