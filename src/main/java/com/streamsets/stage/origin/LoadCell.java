package com.streamsets.stage.origin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class LoadCell {
    private String hostName, characterSet;
    private int port;
    private InputStreamReader reader;
    private Socket socket;
    LoadCell(String hostName, int port, String characterSet){
        this.hostName = hostName;
        this.port = port;
        this.characterSet = characterSet;
    }

    void makeConnect() throws IOException {
        socket = new Socket(hostName, port);
        InputStream input = socket.getInputStream();
        if(characterSet.equals("AUTO")){reader = new InputStreamReader(input);}
        else {reader = new InputStreamReader(input, characterSet);}
    }

    String getValue(int startCharacter, int endCharacter) throws IOException {
        int character;
        StringBuilder data = new StringBuilder();
        boolean readFlag = false;
        boolean readStart = false;
        while ((character = reader.read()) != -1) {
        //while (true) {
          //  character = reader.read();
            if (startCharacter == character && !readStart) {
                readFlag = true;
                readStart = true;
                continue;
            }
            if (endCharacter == character && readStart) {
                data.deleteCharAt(data.length() - 1);
                return String.valueOf(data);
            }
            if (readFlag) {
                data.append(character);
                data.append(",");
            }
        }
        return "";
    }
    void closeConnect() throws IOException {
        reader.close();
        socket.close();
    }
}





