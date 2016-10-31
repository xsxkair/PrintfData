package Com;


import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;



public class Serial_Driver implements Runnable {
	static CommPortIdentifier portId;
	private String portName = null;
	
	static SerialPort serialPort;
	static InputStream inputStream;
	static OutputStream outputStream;
	
	private Thread readThread;
	
	private ArrayBlockingQueue<Byte> TXBlockQueue = new ArrayBlockingQueue<Byte>(100);
	private ArrayBlockingQueue<Byte> RXBlockQueue = new ArrayBlockingQueue<Byte>(100);
	
	boolean islife = false;
	
	public Serial_Driver() {
		
    }
	
    public Serial_Driver(String portname) {
    	this.portName = portname;
    }
    
    public boolean OpenOneSerial() {
    	
    	if(portName == null){
    		System.out.println("¿Õ´®¿Ú");
    		return false;
    	}

		try {
			portId = CommPortIdentifier.getPortIdentifier(this.portName);
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			return false;
		}
			
		try {
			serialPort = (SerialPort)portId.open("Serial_Communication", 2000);
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			return false;
		}

		try {
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}

				
		serialPort.notifyOnDataAvailable(true);
				
		try {
			serialPort.setSerialPortParams(9600,
			SerialPort.DATABITS_8,
			SerialPort.STOPBITS_1,
			SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		islife = true;
		
		readThread = new Thread(this);
		readThread.start();
		
		
		return true;

	}

    public void CloseComm(){
    	
    	try {
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	islife = false;
    	serialPort.removeEventListener();
    	serialPort.close();
    }
  
    public void run() {
    	
    	Byte Cchar = 0;
    	
    	while(islife){
    		try {
    			while(inputStream.available() > 0) {
                    RXBlockQueue.offer((byte) inputStream.read());
                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error1");
			}
    		
    		try {	
    			
				Cchar = TXBlockQueue.poll(50, TimeUnit.MILLISECONDS);
				if(Cchar != null){
					try {	
						outputStream.write(Cchar);	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("error2");
					}
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error2");
			}
    	}
    }

	public ArrayBlockingQueue<Byte> getTXBlockQueue() {
		return TXBlockQueue;
	}

	public ArrayBlockingQueue<Byte> getRXBlockQueue() {
		return RXBlockQueue;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}
	
	public boolean PutChar(byte data){
		try {
			return TXBlockQueue.offer(data , 50, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean PutStr(byte[] buf) {
		for(int i=0; i<buf.length; i++){
			try {
				if(!TXBlockQueue.offer(buf[i] , 50, TimeUnit.MILLISECONDS))
					return false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean PutStr(String str) {

		for(int i=0; i<str.getBytes().length; i++){
			try {
				if(!TXBlockQueue.offer(str.getBytes()[i] , 50, TimeUnit.MILLISECONDS))
					return false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean GetStr(byte[] buf , int len) {
		Byte Cchar = 0;	
		int i=0;
		
		for(i=0; i<len; i++){
			
			try {
				Cchar = RXBlockQueue.poll(50, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Cchar != null){
				buf[i] = Cchar;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	public Byte GetChar() {
		Byte Cchar = 0;	

		try {
			Cchar = RXBlockQueue.poll(50, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Cchar;
	}

	public boolean isIslife() {
		return islife;
	}

	public void setIslife(boolean islife) {
		this.islife = islife;
	}


}
