import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//내용 입력
class FreeDialog extends JDialog{
	public FreeDialog(JFrame frame, String title, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,250);
		module.attribute="";
		module.content="";
		module.width=0;
		module.height=0;
		
		//안내패널
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("사용할 태그를 입력(<>제외)");
		final JTextField tag=new JTextField("여기에 태그 입력");
		tag.setSize(20, 10);
		tagPanel.add(tagLabel);
		tagPanel.add(tag);
		tagPanel.setLayout(new GridLayout(2,1));
		
		//속성패널
		JPanel attributePanel=new JPanel();
		JLabel attributeLabel=new JLabel("속성과 값을 입력");
		final JTextField attribute=new JTextField("ex)name='홍길동'");
		attribute.setSize(20, 10);
		attributePanel.add(attributeLabel);
		attributePanel.add(attribute);
		attributePanel.setLayout(new GridLayout(2,1));
		
		//내용패널
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("태그 사이에 들어갈 내용 입력");
		final JTextField content=new JTextField("여기에 내용 입력");
		content.setSize(20, 10);
		contentPanel.add(contentLabel);
		contentPanel.add(content);
		contentPanel.setLayout(new GridLayout(2,1));
		
		//실제 동작을 수행할 패널
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("확인");
		JButton cancelButton=new JButton("취소");
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