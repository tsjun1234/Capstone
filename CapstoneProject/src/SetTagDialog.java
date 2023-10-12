import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

//속성과 내용의 값을 입력받는 대화상자
class SetTagDialog extends JDialog{
	public SetTagDialog(JFrame frame, String title, String[]Global, String[]Event, String[]list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,550);
		module.attribute="";
		module.content="";
		module.width=0;
		module.height=0;
		Container c=getContentPane();
		
		//안내패널 변수
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("<"+title+"></"+title+">");
		
		//속성패널 변수
		JPanel attributePanel=new JPanel();
		JPanel BoxPanel=new JPanel();
		JPanel RadioPanel=new JPanel();
		JPanel RadioGroup=new JPanel();
		JLabel attributeLabel=new JLabel("속성과 값을 입력");
		JLabel attributeTmp=new JLabel("");
		JLabel selected=new JLabel("");
		JLabel []boxList=new JLabel[3];
		boxList[0]=new JLabel("글로벌 속성");
		boxList[1]=new JLabel("이벤트 속성");
		boxList[2]=new JLabel("태그 전용 속성");
		JLabel Radio=new JLabel("position 속성 선택");
		JRadioButton []RadioButton=new JRadioButton[5];
		RadioButton[0]=new JRadioButton("static");
		RadioButton[1]=new JRadioButton("relative");
		RadioButton[2]=new JRadioButton("absolute");
		RadioButton[3]=new JRadioButton("fixed");
		RadioButton[4]=new JRadioButton("sticky");
		ButtonGroup RadioButtons=new ButtonGroup();
		JComboBox<String>attributeGlobalBox=new JComboBox<String>(Global);
		JComboBox<String>attributeEventBox=new JComboBox<String>(Event);
		JComboBox<String>attributeBox=new JComboBox<String>(list);
		final JTextField attributeText=new JTextField("");
		
		//내용패널 변수
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("태그 사이에 들어갈 내용 입력");
		JLabel contentTmp=new JLabel("");
		final JTextField content=new JTextField("");
		JLabel previewLabel=new JLabel("미리보기");
		JPanel previewPanel=new JPanel();
		JLabel preview=new JLabel("");
		
		//확인취소패널 변수
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("확인");
		JButton cancelButton=new JButton("취소");
		
		//안내패널 설정
		tagPanel.add(tagLabel);
		
		//속성패널 설정
		attributeGlobalBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		attributeEventBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		attributeBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		attributeText.setSize(20, 10);
		attributeGlobalBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				attributeEventBox.setSelectedItem("");
				attributeBox.setSelectedItem("");
				selected.setText(attributeGlobalBox.getSelectedItem().toString());
			}
		});
		attributeEventBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				attributeGlobalBox.setSelectedItem("");
				attributeBox.setSelectedItem("");
				selected.setText(attributeEventBox.getSelectedItem().toString());
			}
		});
		attributeBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				attributeEventBox.setSelectedItem("");
				attributeGlobalBox.setSelectedItem("");
				selected.setText(attributeBox.getSelectedItem().toString());
			}
		});
		JButton addAttributeButton=new JButton("추가");
		addAttributeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attributeTmp.setText(attributeTmp.getText()+" "+selected.getText()+"=\'"+attributeText.getText()+"\'");
				tagLabel.setText("<"+title+attributeTmp.getText()+">"+contentTmp.getText()+"</"+title+">");
			}
		});
		
		BoxPanel.add(boxList[0]);
		BoxPanel.add(attributeGlobalBox);
		BoxPanel.add(boxList[1]);
		BoxPanel.add(attributeEventBox);
		BoxPanel.add(boxList[2]);
		BoxPanel.add(attributeBox);
		BoxPanel.setLayout(new GridLayout(3,2));
		
		RadioPanel.add(Radio);
		RadioPanel.add(RadioGroup);
		for(int i=0;i<5;i++) {
			RadioButtons.add(RadioButton[i]);
			RadioGroup.add(RadioButton[i]);
		}
		RadioButton[0].setSelected(true);
		RadioPanel.setLayout(new GridLayout(2,1));
		RadioGroup.setLayout(new GridLayout(2,3));
		
		attributePanel.add(attributeLabel);
		attributePanel.add(BoxPanel);
		attributePanel.add(RadioPanel);
		attributePanel.add(attributeText);
		attributePanel.add(addAttributeButton);
		attributePanel.setLayout(new GridLayout(5,1));
		
		//내용패널 설정
		content.setSize(20, 10);
		content.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {	
				contentTmp.setText(content.getText());
				preview.setText(content.getText());
				tagLabel.setText("<"+title+attributeTmp.getText()+">"+contentTmp.getText()+"</"+title+">");
			}
			public void keyReleased(KeyEvent e) {
				contentTmp.setText(content.getText());
				preview.setText(content.getText());
				tagLabel.setText("<"+title+attributeTmp.getText()+">"+contentTmp.getText()+"</"+title+">");
			}
		});
		contentPanel.setLayout(new GridLayout(4,1));
		contentPanel.add(contentLabel);
		contentPanel.add(content);
		contentPanel.add(previewLabel);
		previewPanel.add(preview);
		contentPanel.add(previewPanel);
		
		//확인취소패널 설정
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<5;i++) {
					if(RadioButton[i].isSelected()) {
						module.attribute=" style='position:"+RadioButton[i].getText()+";' "+attributeTmp.getText();
						break;
					}
				}
				module.content=contentTmp.getText();
				module.width=preview.getWidth();
				module.height=preview.getHeight();
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
		
		//전체 구도 설정;
		setLayout(null);
		tagPanel.setBounds(10, 10, 260, 20);
		attributePanel.setBounds(10, 30, 260, 250);
		contentPanel.setBounds(10, 280, 260, 100);
		buttonPanel.setBounds(10, 380, 260, 35);
		add(tagPanel);
		add(attributePanel);
		add(contentPanel);
		add(buttonPanel);
		/*
		 * GridBagLayout grid=new GridBagLayout(); GridBagConstraints c=new
		 * GridBagConstraints(); setLayout(grid); c.fill=GridBagConstraints.VERTICAL;
		 * c.gridy=1; c.gridheight=1; grid.setConstraints(tagPanel, c); add(tagPanel);
		 * c.fill=GridBagConstraints.VERTICAL; c.gridy=2; c.gridheight=1;
		 * grid.setConstraints(attributePanel, c); add(attributePanel);
		 * c.fill=GridBagConstraints.VERTICAL; c.gridy=3; c.gridheight=1;
		 * grid.setConstraints(contentPanel, c); add(contentPanel);
		 * c.fill=GridBagConstraints.VERTICAL; c.gridy=4; c.gridheight=1;
		 * grid.setConstraints(buttonPanel, c); add(buttonPanel);
		 */
		
		setVisible(true);
		pack();
	}
}