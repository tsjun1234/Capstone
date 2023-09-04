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
	boolean display;
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
		display=false;
		checkAttribute();
		setSize(e.x-s.x, e.y-s.y);
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
		if(display)
			return;
		this.width=width;
		this.height=height;
	}
	//�Ӽ����� �˻縦 ���� �и���
	void checkAttribute() {
		String[]tmp=tagOption.split(" ");
		for(int i=0;i<tmp.length;i++) {
			if(tmp[i].indexOf('=')!=-1) {
				String check=tmp[i].substring(0, tmp[i].indexOf('='));
				System.out.println(check);
				String val=tmp[i].substring(tmp[i].indexOf('\'')+1,tmp[i].length()-1);
				System.out.println(val);
			}
		}
	}
	//Ŭ���� ��ġ�� �±� �ȿ� �ִ��� Ȯ��
	TagObject checkLocation(int x, int y) {
		for(int i=0;i<childrens.size();i++) {
			TagObject tmp=childrens.get(i).checkLocation(x, y);
			if(tmp!=null)
				return tmp;
		}
		if((x>this.x)&&(x<this.x+width)&&(y>this.y)&&(y<this.y+height))
			return this;
		return null;
	}
	//Ŭ������ ����Ǿ� �ִ� �±װ��� ���ʷ� ���� ��ȯ��
	String getTag() {
		String tmp=tag.substring(1,tag.length()-1);
		return "<"+tmp+tagOption+">"+content+"</"+tmp+">";
	}
}