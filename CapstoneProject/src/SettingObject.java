import java.util.*;

public class SettingObject {
	HashMap<String, String[]> defaultSetting=new HashMap<String,String[]>();
	HashMap<String, String[]> HTMLSetting=new HashMap<String, String[]>();
	App application;
	SettingObject(App app){
		application=app;
		String tmp=application.fileModule.ReadFile("c:\\Users\\User\\Desktop\\setting\\default.txt");
		setDefaultSetting(tmp);
	}
	void setDefaultSetting(String config) {
		String[]tmp=config.split("\n");
		System.out.println(tmp[1]);
		setHTMLSetting(tmp[1]);
	}
	void setHTMLSetting(String config) {
		int line=0;
		String []tmp=application.fileModule.ReadFile(config).split("\n");
		String []tmpList=tmp[1].split(",");
		//카테고리 추가(0~1줄)
		HTMLSetting.put(tmp[0].substring(1,tmp[0].length()-1), tmpList);
		line+=2;
		//각 카테고리에 할당되는 태그 추가(2~11)
		//2쨰줄은 구분줄이므로 +1
		line+=1;
		for(int i=0;i<HTMLSetting.get("Tag Category").length;i++) {
			tmpList=tmp[line].split(",");
			HTMLSetting.put(HTMLSetting.get("Tag Category")[i],tmpList);
			line+=1;
		}
		//각 태그의 대한 설명(나중에 추가)
		//HTML의 기본설정(CSS쪽? 나중에 판단)
		while(true) {
			System.out.println(tmp[line]);
			line++;
			if(line==tmp.length)
				break;
		}
	}
}
