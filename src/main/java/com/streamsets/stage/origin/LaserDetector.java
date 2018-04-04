package com.streamsets.stage.origin;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class LaserDetector {
    private String hostName, characterSet;
    private int port;
    private byte [] session = new byte []{0x65 , 0x00 , 0x04 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x01 , 0x00 , 0x02 , 0x00 , 0x00 , 0x00 , (byte) 0x85, 0x01 , 0x00 , 0x00 , 0x00 , 0x00 , 0x01 , 0x00 , 0x00 , 0x00};
    byte [] sendRRData = new byte[] {0x6f, 0x00, 0x18, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x00, 0x00, (byte) 0x85, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xb2, 0x00, 0x08, 0x00, 0x0e, 0x03, 0x20, 0x23, 0x24, 0x01, 0x30, 0x0a};
    private InputStreamReader reader;
    private OutputStream output;

    LaserDetector(String hostName, int port, String characterSet) throws IOException {
        this.hostName = hostName;
        this.port = port;
        this.characterSet = characterSet;
    }

    byte [] makeConnect() throws IOException {
        Socket socket = new Socket(hostName, port);
        InputStream input = socket.getInputStream();
        reader = new InputStreamReader(input, "iso8859-1");
        output = socket.getOutputStream();
        return session;
    }

    byte[] initSession(byte[] session) throws IOException {
        output.write(session);
        int character;
        int idx=0;
        while ((character = reader.read()) != -1) {
            idx++;
            if(idx==8){
                sendRRData[7]= (byte) character;
                return sendRRData;
            }
        }
        return null;
    }

    String getValue(byte[] sendRRData) throws IOException {
        output.write(sendRRData);
        StringBuilder outputData = new StringBuilder();
        int idx=0;
        int character;
        for (idx=0;idx<68;idx++) {
            character = reader.read();
            if(64 <= idx && 68 >= idx){ outputData.insert(0, Integer.toHexString(character)); }
            if(68<idx){ break; }
        }
        System.out.println(Integer.parseInt(String.valueOf(outputData),16)/1000);
        return null;
    }


}
