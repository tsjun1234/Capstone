import java.awt.Point;
import java.util.Vector;

//���Ͽ� ����� �±׸� Ŭ����ȭ
class TagObject{
	String tag;
	int x, y;
	int width, height;
	String tagOption;
	String result;
	String content;
	TagObject Parent;
	Vector<TagObject>childrens=new Vector<TagObject>();
	//Ŭ���� ������
	TagObject(){
		this("<body>","","");
	}
	TagObject(String tag, String tagOption, String content){
		this(tag, new Point(0,0), new Point(0,0), tagOption, content, 0, 0);
	}
	TagObject(String tag, Point s, Point e, String tagOption, String content, int width, int height){
		this.tag=tag;
		this.x=s.x;
		this.y=s.y;
		this.width=width;
		this.height=height;
		this.tagOption=tagOption;
		this.content=content;
		if((width==0&&height==0)) {
			setSize(e.x-s.x, e.y-s.y);
		}
		if(!checkAttribute("style").equals("")) {
			String style=checkPosition(checkAttribute("style"));
			System.out.println(style);
			//this.tagOption=" style='"+style+"' "+tagOption;
			modifyAttribute("style",style);
		}
	}
	//�θ� ����
	void setParent(TagObject p) {
		Parent=p;
		p.addChild(this);
	}
	//�ڽ� ��Ͽ� �߰�
	void addChild(TagObject c) {
		childrens.add(c);
	}
	//���̿� �ʺ� ����
	void setSize(int width, int height) {
		this.width=width;
		this.height=height;
	}
	//position�� absolute�� ������ġ�� �ش��ϴ� ������ ������ style�� ��ȯ �ƴ϶�� ��ġ�� ����
	String checkPosition(String style) {
		String tmp=style;
		String[]tmpList=style.split(";");
		for(int i=0;i<tmpList.length;i++) {
			if(style.substring(style.indexOf(':')+1, style.indexOf(';')).equals("absolute")) {
				tmp=style+"left:"+x+"px;top:"+y+"px;width:"+width+"px;height:"+height+"px;";
				return tmp;
			}
		}
		return tmp;
	}
	//�Ӽ����� �˻縦 ���� �и���
	String checkAttribute(String check) {
		String[]tmp=tagOption.split(" ");
		for(int i=0;i<tmp.length;i++) {
			if(tmp[i].indexOf('=')!=-1) {
				//�Ӽ����� �˻���
				String attribute=tmp[i].substring(0, tmp[i].indexOf('='));
				//ã���� �ϴ� �Ӽ��� ã���� �ش� �Ӽ��� ���� ��ȯ
				if(check.equals(attribute)) {
					String val=tmp[i].substring(tmp[i].indexOf('\'')+1,tmp[i].length()-1);
					return val;
				}
			}
		}
		return "";
	}
	//�Ӽ���Ͽ��� ã���� �ϴ� �Ӽ��� ã�� ���ϴ� ������ ����
		void modifyAttribute(String name, String attr) {
			String[]tmp=tagOption.split(" ");
			String tmpOption="";
			for(int i=0;i<tmp.length;i++) {
				if(tmp[i].indexOf('=')!=-1) {
					String attribute=tmp[i].substring(0,tmp[i].indexOf('='));
					if(name.equals(attribute)) {
						tmpOption+=" "+name+"='"+attr+"'";
					}
					else {
						tmpOption+=tmp[i];
					}
				}
			}
			tagOption=tmpOption;
		}
	//Ŭ���� ��ġ�� �±� �ȿ� �ִ��� Ȯ��
	TagObject checkLocation(int x, int y) {
		for(int i=0;i<childrens.size();i++) {
			TagObject tmp=childrens.get(i).checkLocation(x, y);
			if(tmp!=null)
				return tmp;
		}
		if((x>this.x)&&(x<this.x+width)&&(y>this.y)&&(y<this.y+height)) {
			return this;
		}
		return null;
	}
	//Ŭ������ ����Ǿ� �ִ� �±װ��� ���ʷ� ���� ��ȯ��
	String getTag() {
		String tmp=tag.substring(1,tag.length()-1);
		return "<"+tmp+tagOption+">"+content+"</"+tmp+">";
	}
}