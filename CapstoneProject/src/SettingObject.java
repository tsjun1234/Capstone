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
		//ī�װ� �߰�(0~1��)
		HTMLSetting.put(tmp[0].substring(1,tmp[0].length()-1), tmpList);
		line+=2;
		//�� ī�װ��� �Ҵ�Ǵ� �±� �߰�(2~11)
		//2������ �������̹Ƿ� +1
		line+=1;
		for(int i=0;i<HTMLSetting.get("Tag Category").length;i++) {
			tmpList=tmp[line].split(",");
			HTMLSetting.put(HTMLSetting.get("Tag Category")[i],tmpList);
			line+=1;
		}
		//�� �±��� ���� ����(���߿� �߰�)
		//HTML�� �⺻����(CSS��? ���߿� �Ǵ�)
		while(true) {
			System.out.println(tmp[line]);
			line++;
			if(line==tmp.length)
				break;
		}
	}
}
