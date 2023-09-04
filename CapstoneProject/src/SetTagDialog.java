import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//�Ӽ��� ������ ���� �Է¹޴� ��ȭ����
class SetTagDialog extends JDialog{
	public SetTagDialog(JFrame frame, String title, String[]Global, String[]Event, String[]list, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,550);
		module.attribute="";
		module.content="";
		module.width=0;
		module.height=0;
		
		//�ȳ��г� ����
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("<"+title+"></"+title+">");
		
		//�Ӽ��г� ����
		JPanel attributePanel=new JPanel();
		JPanel BoxPanel=new JPanel();
		JLabel attributeLabel=new JLabel("�Ӽ��� ���� �Է�");
		JLabel attributeTmp=new JLabel("");
		JLabel selected=new JLabel("");
		JLabel []boxList=new JLabel[3];
		boxList[0]=new JLabel("�۷ι� �Ӽ�");
		boxList[1]=new JLabel("�̺�Ʈ �Ӽ�");
		boxList[2]=new JLabel("�±� ���� �Ӽ�");
		JComboBox<String>attributeGlobalBox=new JComboBox<String>(Global);
		JComboBox<String>attributeEventBox=new JComboBox<String>(Event);
		JComboBox<String>attributeBox=new JComboBox<String>(list);
		final JTextField attributeText=new JTextField("");
		
		//�����г� ����
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("�±� ���̿� �� ���� �Է�");
		JLabel contentTmp=new JLabel("");
		final JTextField content=new JTextField("");
		JLabel previewLabel=new JLabel("�̸�����");
		JPanel previewPanel=new JPanel();
		JLabel preview=new JLabel("");
		
		//Ȯ������г� ����
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("Ȯ��");
		JButton cancelButton=new JButton("���");
		
		//�ȳ��г� ����
		tagPanel.add(tagLabel);
		
		//�Ӽ��г� ����
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
		JButton addAttributeButton=new JButton("�߰�");
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
		
		attributePanel.add(attributeLabel);
		attributePanel.add(BoxPanel);
		attributePanel.add(attributeText);
		attributePanel.add(addAttributeButton);
		attributePanel.setLayout(new GridLayout(4,1));
		
		//�����г� ����
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
		
		//Ȯ������г� ����
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module.attribute=attributeTmp.getText();
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
		
		//��ü ���� ����
		GridBagLayout grid=new GridBagLayout();
		GridBagConstraints c=new GridBagConstraints();
		setLayout(grid);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=1;
		c.gridheight=1;
		grid.setConstraints(tagPanel, c);
		add(tagPanel);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=2;
		c.gridheight=1;
		grid.setConstraints(attributePanel, c);
		add(attributePanel);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=3;
		c.gridheight=1;
		grid.setConstraints(contentPanel, c);
		add(contentPanel);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=4;
		c.gridheight=1;
		grid.setConstraints(buttonPanel, c);
		add(buttonPanel);
		
		setVisible(true);
		pack();
	}
}