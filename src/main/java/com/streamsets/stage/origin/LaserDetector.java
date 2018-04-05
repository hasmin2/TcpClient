package com.streamsets.stage.origin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class LaserDetector {
    private String hostName, characterSet;
    private int port;
    private byte [] session = new byte []{0x65 , 0x00 , 0x04 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x01 , 0x00 , 0x02 , 0x00 , 0x00 , 0x00 , (byte) 0x85, 0x01 , 0x00 , 0x00 , 0x00 , 0x00 , 0x01 , 0x00 , 0x00 , 0x00};
    private byte [] sendRRData = new byte[] {0x6f, 0x00, 0x18, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x00, 0x00, (byte) 0x85, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xb2, 0x00, 0x08, 0x00, 0x0e, 0x03, 0x20, 0x23, 0x24, 0x01, 0x30, 0x0a};

    LaserDetector(String hostName, int port, String characterSet) {
        this.hostName = hostName;
        this.port = port;
        this.characterSet = characterSet;
    }
    String getValue(){
        StringBuilder outputData = new StringBuilder();
        try (Socket socket = new Socket(hostName, port)) {
            InputStream input = socket.getInputStream();
            InputStreamReader reader;
            if(characterSet.equals("AUTO")){ reader = new InputStreamReader(input); }
            else { reader = new InputStreamReader(input, characterSet); }
            OutputStream output = socket.getOutputStream();
            output.write(session);
            int character;
            int idx=0;
            while ((character = reader.read()) != -1) {
                idx++;
                if(idx==8){
                    sendRRData[7]= (byte) character;
                    break;
                }
            }
            output.write(sendRRData);
            for (idx=0;idx<68;idx++) {
                character = reader.read();
                if(64 <= idx){ outputData.insert(0, Integer.toHexString(character)); }
            }
            //System.out.println(Integer.parseInt(String.valueOf(outputData),16)/1000);
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        } finally {
            return outputData.toString();
        }

    }

}
