## JavaTools

### java tools is made to easily handle queue, thread, network, and byte converter(tools)

thread - this thread was run continuesly as the service is running. you need to command .stopThread() to stop the thread.

queue - this queue is best combination for the thread, so the process has synchronous.

network - easy way of TCP and UDP.

	// inherit BaseThread class
	// implements udpServerListener to the class
	public class UdpServer extends com.mayforever.thread.BaseThread implements 
			com.mayforever.network.udp.ServerListener{
		// declaring udp server
		public com.mayforever.network.udp.UDPServer udpServer = null;
		// declaring queue
		public com.mayforever.queue.Queue<UdpData> udpData = null;
		
		public UdpServer(){
			try{
				// initializing udp server
				udpServer = new com.mayforever.network.udp.UDPServer(15000);
				// add listener to udp server and the listener is this class
				udpServer.addListener(this);
				// initializing queue
				udpData = new com.mayforever.queue.Queue<UdpData>();
			}catch(Exception e){
				System.exit(0);
			}		
		}
		
		public void startUdpServer(){
			// starting the base thread
			this.startThread();
		}
		
		@Override
		public void errorDatagram(Exception e) {
			e.printStackTrace();
			// TODO Auto-generated method stub
		}

		@Override
		public void recievePacket(byte[] data, InetSocketAddress isa) {
			// inserting udp data to the queue
			udpData.add(new UdpData(isa, data));
			// System.out.println(isa + " : " + data);
		}

		@Override
		public void run() {
			// need to sure that the service thread is running
			while(this.getServiceState()==com.mayforever.thread.state.ServiceState.RUNNING){
	            try {
	            	// getting data to queue
	                UdpData data = udpData.get();
	                if (data != null){
	                	// do something to process the data
	                }
	            } catch (InterruptedException ex) {
	            	
	            } 
	        }
		}
	}

	public class App {
	

		public static UdpServer udpServer = null;
		
		
		public static void main(String[] args){
			udpServer = new UdpServer();
			udpServer.startUdpServer();
			
		}
	}

this is the sample of udp data

	public class UdpData {
		private InetSocketAddress isa = null;
		private byte[] data = null;
		
		public UdpData(InetSocketAddress isa, byte[] data){
			this.isa = isa;
			this.data = data;
		}
		
		public InetSocketAddress getIsa() {
			return isa;
		}
		public byte[] getData() {
			return data;
		}
	}

tools - this tools is use in converting int, long, float, double, to byte or vice versa

    public class App {
		public static void main(String[] args) {
				// given sample double 
				double sampleDouble = 1.2541654646d;
				
				// declare , and convert sa
				byte[] bytesForm = com.mayforever.tools.BitConverter.doubleToBytes(sampleDouble, ByteOrder.BIG_ENDIAN);
				// check the result
				System.out.println(Arrays.toString(bytesForm));
				
				// // declare , and convert bytesForm
				double doubleForm = com.mayforever.tools.BitConverter.bytesToDouble(bytesForm, 0, ByteOrder.BIG_ENDIAN);
				// check the result
				System.out.println(doubleForm);
		}
    }

