import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import Com.GetSerialList;
import Com.Serial_Driver;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class MianFram extends JFrame implements ActionListener, Runnable {

	private JPanel contentPane;
	private JPanel contentPane2;
	private JButton OpenSerialButton;
	private JComboBox SerialList;
	private JComboBox<String> printtype;
	private JButton myJButton;
	private JLabel mYjLabel;
	private JLabel label1;
	private JTextField textField1;
	private JLabel label2;
	private JTextField textField2;
	private JLabel label3;
	private JTextField textField3;
	private JLabel label4;
	private JTextField textField4;
	private JLabel label5;
	private JTextField textField5;
	private Serial_Driver mySerial_Driver = new Serial_Driver();
	private JFileChooser myJFileChooser = new JFileChooser(".");
	private String GB_FilePath = null;
	private JButton StartPrint;
	private boolean isWorking = false;
	private ArrayBlockingQueue<Boolean> TXBlockQueue = new ArrayBlockingQueue<Boolean>(1);
	private Thread myThread;
	
	private Integer startsheetnum = 1;
	private Integer startlinenum = 1;
	private Integer printfnum = 10000;
	private String danwei="";
	private Integer xiaoshu = 0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MianFram GB_MianFram = new MianFram();
		GB_MianFram.MianFram_Init();
		
		GB_MianFram.fun();
		
	}

	/**
	 * Create the frame.
	 */
	public void MianFram_Init() {
		
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBounds(0, 0, 650, 300);
		this.add(contentPane);
		
		JLabel label = new JLabel("ѡ�񴮿�:");
		label.setBounds(10, 10, 70, 15);
		contentPane.add(label);
		
		SerialList = new JComboBox();
		SerialList.setBounds(92, 7, 72, 21);
		contentPane.add(SerialList);
		SerialList.setModel(new DefaultComboBoxModel(GetSerialList.SearchAllSerials().toArray()));
		
		OpenSerialButton = new JButton("�򿪴���");
		OpenSerialButton.addActionListener(this);
		OpenSerialButton.setBounds(195, 6, 93, 23);
		contentPane.add(OpenSerialButton);
		
		myJButton = new JButton("<HTML><U>ѡ���ļ�</U></HTML>");
		myJButton.setBounds(300, 0, 100, 30);
		myJButton.addActionListener(this);
		myJButton.setContentAreaFilled(false);
		myJButton.setBorderPainted(false);
		contentPane.add(myJButton);
		
		printtype = new JComboBox<>(new String[]{"Ŧ����","����"});
		printtype.setBounds(450, 10, 100, 23);
		contentPane.add(printtype);
		
		label1 = new JLabel("��ʼ��������");
		label1.setBounds(0, 40, 100, 30);
		contentPane.add(label1);
		
		textField1 = new JTextField(10);
		textField1.setText(""+startsheetnum);
		textField1.setBounds(100, 40, 100, 30);
		contentPane.add(textField1);
		
		label2 = new JLabel("��ӡ��ʼ�кţ�");
		label2.setBounds(0, 70, 100, 30);
		contentPane.add(label2);
		
		textField2 = new JTextField(10);
		textField2.setText(""+startlinenum);
		textField2.setBounds(100, 70, 100, 30);
		contentPane.add(textField2);
		
		label3 = new JLabel("��ӡ��Ŀ��");
		label3.setBounds(0, 100, 100, 30);
		contentPane.add(label3);
		
		textField3 = new JTextField(10);
		textField3.setText(""+printfnum);
		textField3.setBounds(100, 100, 100, 30);
		contentPane.add(textField3);
		
		label4 = new JLabel("��λ��");
		label4.setBounds(0, 130, 100, 30);
		contentPane.add(label4);
		
		textField4 = new JTextField(10);
		textField4.setText(""+danwei);
		textField4.setBounds(100, 130, 100, 30);
		contentPane.add(textField4);
		
		label5 = new JLabel("С���������");
		label5.setBounds(200, 130, 100, 30);
		contentPane.add(label5);
		
		textField5 = new JTextField(10);
		textField5.setText(""+danwei);
		textField5.setBounds(300, 130, 100, 30);
		contentPane.add(textField5);
		
		StartPrint = new JButton("��ʼ��ӡ");
		StartPrint.setBounds(0, 160, 100, 50);
		StartPrint.setEnabled(false);
		StartPrint.addActionListener(this);
		contentPane.add(StartPrint);
		
		mYjLabel = new JLabel();
		mYjLabel.setBounds(150, 100, 100, 50);
		contentPane.add(mYjLabel);
		// �����ļ�������
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("xls");
		filter.setDescription("����ļ�(*.xls)");
		myJFileChooser.setCurrentDirectory(new File("C:\\Users\\Administrator\\Desktop"));
		myJFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		myJFileChooser.addChoosableFileFilter(filter);
		myJFileChooser.setAcceptAllFileFilterUsed(false);	
		
		this.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 300);
		this.setVisible(true);
		this.setTitle("xsx_kair");
		
		myThread = new Thread(this);
		myThread.start();
	}
	
	public void fun(){
	Boolean startstate = null;
		while(true){
			try {
				startstate = TXBlockQueue.poll(50, TimeUnit.MILLISECONDS);
				if(startstate!= null){
					if(startstate){
						StartPrint.setText("���ڴ�ӡ");
						StartPrint.setEnabled(false);
						isWorking = true;
					}
					else {
						StartPrint.setText("��ʼ��ӡ");
						StartPrint.setEnabled(true);
						isWorking = false;
					}
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(myJButton)){
			int userchose = myJFileChooser.showOpenDialog(this);

			if(userchose == JFileChooser.APPROVE_OPTION){
				GB_FilePath = myJFileChooser.getSelectedFile().getPath();
				if(!GB_FilePath.endsWith(".xls"))
					GB_FilePath = null;
			}
		}
		else if(e.getSource().equals(OpenSerialButton)){
			if(e.getActionCommand().equals("�򿪴���")){
				mySerial_Driver.setPortName(SerialList.getSelectedItem().toString());
				if(mySerial_Driver.OpenOneSerial()){
					OpenSerialButton.setText("�رմ���");
					SerialList.setEnabled(false);
					StartPrint.setEnabled(true);
				}
				else
					JOptionPane.showMessageDialog(this, "��ʧ��", "����", JOptionPane.ERROR_MESSAGE, null);
			}else {
				if(mySerial_Driver.isIslife()){
					mySerial_Driver.CloseComm();
					OpenSerialButton.setText("�򿪴���");
					SerialList.setEnabled(true);
					StartPrint.setEnabled(false);
				}
			}
		}
		else if(e.getSource().equals(StartPrint)){
			
			try {
				startsheetnum = Integer.valueOf(textField1.getText());
				startlinenum = Integer.valueOf(textField2.getText());
				printfnum = Integer.valueOf(textField3.getText());
				danwei = textField4.getText();
				xiaoshu = Integer.valueOf(textField5.getText());
			} catch (Exception e2) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "��������", "����", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			if(xiaoshu > 4)
			{
				JOptionPane.showMessageDialog(this, "��������", "����", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			try {
				TXBlockQueue.offer(true , 50, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void SaveData_Fun(){
		Integer num = 0;
		String string;
		int i=0, j=0,k;
		String str1, str2;
		
		if(GB_FilePath == null)
			return;

		Workbook  wb = null;
		Sheet mysheet = null;
		InputStream instream = null;
		
		try {
			instream = new FileInputStream(GB_FilePath);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			JOptionPane.showConfirmDialog(null, "�ļ��򿪴���������");
			return;
		}
		try {
			wb = Workbook.getWorkbook(instream);
			mysheet = wb.getSheet(startsheetnum-1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			JOptionPane.showConfirmDialog(null, "�ļ��򿪴���������");
			return;
		}
			
		int myrow = 0;

		Cell Cell = null;
		
		mySerial_Driver.PutChar((byte) 0x1b);
		mySerial_Driver.PutChar((byte) 0x20);
		mySerial_Driver.PutChar((byte) 0x03);
		mySerial_Driver.PutChar((byte) 0x0d);
			
		for(i=startlinenum; i<printfnum+startlinenum; i++)
		{
			if(printtype.getSelectedItem().equals("Ŧ����")){
				try {
					Cell = mysheet.getCell(4,i);
					NumberCell temp = (NumberCell)Cell;

					mySerial_Driver.PutStr("------------------------");
					mySerial_Driver.PutChar((byte) 0x0d);
					mySerial_Driver.PutStr("�人Ŧ��������Ƽ��ɷ����޹�˾");
					mySerial_Driver.PutChar((byte) 0x0d);
					
					mySerial_Driver.PutStr("��ƷID: "+Cell.getContents());
					mySerial_Driver.PutChar((byte) 0x0d);
					
					num += 1;
					
				} catch (Exception e) {
					// TODO: handle exception
					break;
				}
				
				mySerial_Driver.PutStr("������Ŀ: "+mysheet.getName());
				mySerial_Driver.PutChar((byte) 0x0d);

				mySerial_Driver.PutStr("���Խ��: ");
				Cell = mysheet.getCell(2,i);
				if(Cell.getType() == CellType.NUMBER){
					NumberCell temp = (NumberCell)Cell;
					Double datDouble = temp.getValue();
					BigDecimal aBigDecimal = new BigDecimal(datDouble).setScale(2, BigDecimal.ROUND_HALF_UP);
					datDouble = aBigDecimal.doubleValue();
					
					if(xiaoshu == 0)
						mySerial_Driver.PutStr(String .format("%.0f",datDouble));
					else if(xiaoshu == 1)
						mySerial_Driver.PutStr(String .format("%.1f",datDouble));
					else if(xiaoshu == 2)
						mySerial_Driver.PutStr(String .format("%.2f",datDouble));
					else if(xiaoshu == 3)
						mySerial_Driver.PutStr(String .format("%.3f",datDouble));
					else if(xiaoshu == 4)
						mySerial_Driver.PutStr(String .format("%.4f",datDouble));
				}
				else {
					string = Cell.getContents();
					mySerial_Driver.PutStr(string);
				}
				mySerial_Driver.PutStr(" "+danwei);
				mySerial_Driver.PutChar((byte) 0x0d);

				
				mySerial_Driver.PutStr("ʱ��:");
			
				DateCell dCell = (DateCell) mysheet.getCell(3,i);

				SimpleDateFormat     matter1     =     new     SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");    
				matter1.setTimeZone(TimeZone.getTimeZone( "GMT "));       
				string = matter1.format(dCell.getDate());

				mySerial_Driver.PutStr(string);
				mySerial_Driver.PutChar((byte) 0x0d);
				
				mySerial_Driver.PutStr("------------------------");
				mySerial_Driver.PutChar((byte) 0x0d);	
			}
			else if(printtype.getSelectedItem().equals("����")){
				mySerial_Driver.PutStr("WONDFO S/N J001067");
				mySerial_Driver.PutChar((byte) 0x0a);
				mySerial_Driver.PutStr("Version 3.23");
				mySerial_Driver.PutChar((byte) 0x0d);
				mySerial_Driver.PutChar((byte) 0x0d);
				
				try {
					Cell = mysheet.getCell(4,i);

					mySerial_Driver.PutStr("ID: "+Cell.getContents());
					mySerial_Driver.PutChar((byte) 0x0d);
					
				} catch (Exception e) {
					// TODO: handle exception
					break;
				}
				
		
				mySerial_Driver.PutStr("USER: USER013");
				mySerial_Driver.PutChar((byte) 0x0a);
				
				mySerial_Driver.PutStr("RESULT: "+mysheet.getName()+" ");
				Cell = mysheet.getCell(2,i);
				if(Cell.getType() == CellType.NUMBER){
					NumberCell temp = (NumberCell)Cell;
					Double datDouble = temp.getValue();
					BigDecimal aBigDecimal = new BigDecimal(datDouble).setScale(2, BigDecimal.ROUND_HALF_UP);
					datDouble = aBigDecimal.doubleValue();
					
					if(xiaoshu == 0)
						mySerial_Driver.PutStr(String .format("%.0f",datDouble));
					else if(xiaoshu == 1)
						mySerial_Driver.PutStr(String .format("%.1f",datDouble));
					else if(xiaoshu == 2)
						mySerial_Driver.PutStr(String .format("%.2f",datDouble));
					else if(xiaoshu == 3)
						mySerial_Driver.PutStr(String .format("%.3f",datDouble));
					else if(xiaoshu == 4)
						mySerial_Driver.PutStr(String .format("%.4f",datDouble));
				}
				else {
					string = Cell.getContents();
					mySerial_Driver.PutStr(string);
				}
				mySerial_Driver.PutStr(" "+danwei);
				mySerial_Driver.PutChar((byte) 0x0d);
				
				DateCell dCell = (DateCell) mysheet.getCell(3,i);
				SimpleDateFormat     matter1     =     new     SimpleDateFormat( "dd MMM yyyy", Locale.ENGLISH);
				matter1.setTimeZone(TimeZone.getTimeZone( "GMT "));
				mySerial_Driver.PutStr("DATE: " + matter1.format(dCell.getDate()));
				mySerial_Driver.PutChar((byte) 0x0a);
				
				//������������ĸ�ʽ  
				SimpleDateFormat matter2 = new SimpleDateFormat( "HH:mm"); 
				matter2.setTimeZone(TimeZone.getTimeZone( "GMT "));
				mySerial_Driver.PutStr("TIME: " + matter2.format(dCell.getDate()));
				mySerial_Driver.PutChar((byte) 0x0d);
				
				Cell = mysheet.getCell(7, i);
				mySerial_Driver.PutStr("LOT: "+Cell.getContents());
				mySerial_Driver.PutChar((byte) 0x0d);
				
				Cell = mysheet.getCell(8, i);
				mySerial_Driver.PutStr("EXP. "+Cell.getContents());
				mySerial_Driver.PutChar((byte) 0x0d);
				mySerial_Driver.PutChar((byte) 0x0d);
				mySerial_Driver.PutStr("END OF DATA");
				mySerial_Driver.PutChar((byte) 0x0d);
				mySerial_Driver.PutChar((byte) 0x0d);
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(isWorking){
				SaveData_Fun();
				
				try {
					TXBlockQueue.offer(false , 50, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				isWorking = false;
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

//����FileFilter�����࣬����ʵ���ļ����˹���
class ExtensionFileFilter extends FileFilter
{
	private String description;
	private ArrayList<String> extensions = new ArrayList<>();
	// �Զ��巽������������ļ���չ��
	public void addExtension(String extension)
	{
		if (!extension.startsWith("."))
		{
			extension = "." + extension;
			extensions.add(extension.toLowerCase());
		}
	}
	// �������ø��ļ��������������ı�
	public void setDescription(String aDescription)
	{
		description = aDescription;
	}
	// �̳�FileFilter�����ʵ�ֵĳ��󷽷������ظ��ļ��������������ı�
	public String getDescription()
	{
		return description;
	}
	// �̳�FileFilter�����ʵ�ֵĳ��󷽷����жϸ��ļ��������Ƿ���ܸ��ļ�
	public boolean accept(File f)
	{
		// ������ļ���·�������ܸ��ļ�
		if (f.isDirectory()) return true;
		// ���ļ���תΪСд��ȫ��תΪСд��Ƚϣ����ں����ļ�����Сд��
		String name = f.getName().toLowerCase();
		// �������пɽ��ܵ���չ���������չ����ͬ�����ļ��Ϳɽ��ܡ�
		for (String extension : extensions)
		{
			if (name.endsWith(extension))
			{
				return true;
			}
		}
		return false;
	}
}
