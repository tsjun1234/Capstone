import java.awt.Point;
import java.util.Vector;

//파일에 저장될 태그를 클래스화
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
	//클래스 생성자
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
	//부모를 지정
	void setParent(TagObject p) {
		Parent=p;
		p.addChild(this);
	}
	//자식 목록에 추가
	void addChild(TagObject c) {
		childrens.add(c);
	}
	//높이와 너비 설정
	void setSize(int width, int height) {
		if(display)
			return;
		this.width=width;
		this.height=height;
	}
	//속성값중 검사를 위해 분리함
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
	//클릭된 위치가 태그 안에 있는지 확인
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
	//클래스에 저장되어 있는 태그값을 기초로 값을 반환함
	String getTag() {
		String tmp=tag.substring(1,tag.length()-1);
		return "<"+tmp+tagOption+">"+content+"</"+tmp+">";
	}
}