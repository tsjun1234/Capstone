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
		this.width=width;
		this.height=height;
	}
	//position이 absolute면 절대위치에 해당하는 값으로 설정된 style값 반환 아니라면 위치를 조정
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
	//속성값중 검사를 위해 분리함
	String checkAttribute(String check) {
		String[]tmp=tagOption.split(" ");
		for(int i=0;i<tmp.length;i++) {
			if(tmp[i].indexOf('=')!=-1) {
				//속성값을 검사함
				String attribute=tmp[i].substring(0, tmp[i].indexOf('='));
				//찾고자 하는 속성을 찾으면 해당 속성의 값을 반환
				if(check.equals(attribute)) {
					String val=tmp[i].substring(tmp[i].indexOf('\'')+1,tmp[i].length()-1);
					return val;
				}
			}
		}
		return "";
	}
	//속성목록에서 찾고자 하는 속성을 찾아 원하는 값으로 수정
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
	//클릭된 위치가 태그 안에 있는지 확인
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
	//클래스에 저장되어 있는 태그값을 기초로 값을 반환함
	String getTag() {
		String tmp=tag.substring(1,tag.length()-1);
		return "<"+tmp+tagOption+">"+content+"</"+tmp+">";
	}
}