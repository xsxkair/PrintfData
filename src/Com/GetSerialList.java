package Com;



import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;


/**
 *@author: xsx
 *@date:2015年10月27日下午4:22:58
 *
 *@file name:GetSerialList.java
 */
public class GetSerialList {
	
	private static CommPortIdentifier portId;
	private static Enumeration<CommPortIdentifier> portList;
	
	private static ArrayList<String> mySerialList = new ArrayList<String>(); 
	
	
	/*
     * 列举电脑上可用的串口号
     * 打印：com1，com2。。。
     */
    private void SearchAllComms(){
        
    	mySerialList.clear();
    	
    	//获得电脑上的每一个端口，包括串口和并口等
    	portList = CommPortIdentifier.getPortIdentifiers();		
        // iterate through the ports.
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            
            //判断是否为串口
            if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	mySerialList.add(portId.getName());
            }
        }
    }
    
    public static ArrayList<String> SearchAllSerials(){
    	mySerialList.clear();
    	
    	//获得电脑上的每一个端口，包括串口和并口等
    	portList = CommPortIdentifier.getPortIdentifiers();		
        // iterate through the ports.
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            
            //判断是否为串口
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
