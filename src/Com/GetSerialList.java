package Com;



import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;


/**
 *@author: xsx
 *@date:2015��10��27������4:22:58
 *
 *@file name:GetSerialList.java
 */
public class GetSerialList {
	
	private static CommPortIdentifier portId;
	private static Enumeration<CommPortIdentifier> portList;
	
	private static ArrayList<String> mySerialList = new ArrayList<String>(); 
	
	
	/*
     * �оٵ����Ͽ��õĴ��ں�
     * ��ӡ��com1��com2������
     */
    private void SearchAllComms(){
        
    	mySerialList.clear();
    	
    	//��õ����ϵ�ÿһ���˿ڣ��������ںͲ��ڵ�
    	portList = CommPortIdentifier.getPortIdentifiers();		
        // iterate through the ports.
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            
            //�ж��Ƿ�Ϊ����
            if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	mySerialList.add(portId.getName());
            }
        }
    }
    
    public static ArrayList<String> SearchAllSerials(){
    	mySerialList.clear();
    	
    	//��õ����ϵ�ÿһ���˿ڣ��������ںͲ��ڵ�
    	portList = CommPortIdentifier.getPortIdentifiers();		
        // iterate through the ports.
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            
            //�ж��Ƿ�Ϊ����
            if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	mySerialList.add(portId.getName());
            }
        }
        
        return mySerialList;
    }
/*    
    public static String[] getMySerialList() {
    	String[] my = new String[mySerialList.size()];
    	
    	SearchAllComms();
    	
    	for (int i=0; i<mySerialList.size(); i++) {
			my[i] = mySerialList.get(i);
		}
		return my;
	}*/
    
}
