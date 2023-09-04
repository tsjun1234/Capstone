import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//���� �Է�
class FreeDialog extends JDialog{
	public FreeDialog(JFrame frame, String title, HTMLModule module) {
		super(frame,title, true);
		setBounds(20,100,300,250);
		module.attribute="";
		module.content="";
		module.width=0;
		module.height=0;
		
		//�ȳ��г�
		JPanel tagPanel=new JPanel();
		JLabel tagLabel=new JLabel("����� �±׸� �Է�(<>����)");
		final JTextField tag=new JTextField("���⿡ �±� �Է�");
		tag.setSize(20, 10);
		tagPanel.add(tagLabel);
		tagPanel.add(tag);
		tagPanel.setLayout(new GridLayout(2,1));
		
		//�Ӽ��г�
		JPanel attributePanel=new JPanel();
		JLabel attributeLabel=new JLabel("�Ӽ��� ���� �Է�");
		final JTextField attribute=new JTextField("ex)name='ȫ�浿'");
		attribute.setSize(20, 10);
		attributePanel.add(attributeLabel);
		attributePanel.add(attribute);
		attributePanel.setLayout(new GridLayout(2,1));
		
		//�����г�
		JPanel contentPanel=new JPanel();
		JLabel contentLabel=new JLabel("�±� ���̿� �� ���� �Է�");
		final JTextField content=new JTextField("���⿡ ���� �Է�");
		content.setSize(20, 10);
		contentPanel.add(contentLabel);
		contentPanel.add(content);
		contentPanel.setLayout(new GridLayout(2,1));
		
		//���� ������ ������ �г�
		JPanel buttonPanel=new JPanel();
		JButton addButton=new JButton("Ȯ��");
		JButton cancelButton=new JButton("���");
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