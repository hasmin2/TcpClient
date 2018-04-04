/**
 * Copyright 2015 StreamSets Inc.
 * <p>
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.stage.origin;

import com.streamsets.pipeline.api.BatchMaker;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.BaseSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This source is an example and does not actually read from anywhere.
 * It does however, generate generate a simple record with one field.
 */
public abstract class CraneDataClientSource extends BaseSource {
    public abstract CraneDataCharacterSet getLoadcellCharacterSet();
    public abstract int getLoadcellStartCharacter();
    public abstract int getLoadcellEndCharacter();
    public abstract CraneDataCharacterSet getXPosCharacterSet();
    public abstract boolean hasXPosition();
    public abstract boolean hasYPosition();
    public abstract String getLoadcellIPAddress();
    public abstract int getLoadcellPort();
    public abstract int getMaxBatchSize();
    /**
     * Gives access to the UI configuration of the stage provided by the {@link CraneDataClientDSource} class.
     */
    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();

        /*if (getConfig().equals("invalidValue")) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.SAMPLE.name(), "config", Errors.SAMPLE_00, "Here's what's wrong..."
                    )
            );
        }*/

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        return issues;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Clean up any open resources.
        super.destroy();
    }

    /** {@inheritDoc} */
    @Override
    public String produce(String lastSourceOffset, int maxBatchSize, BatchMaker batchMaker) throws StageException {
        // Offsets can vary depending on the data source. Here we use an integer as an example only.
        long nextSourceOffset = 0;
        if (lastSourceOffset != null) {
            nextSourceOffset = Long.parseLong(lastSourceOffset);
        }

        int numRecords = 0;
        maxBatchSize=getMaxBatchSize();
        // TODO: As the developer, implement your logic that reads from a data source in this method.
        // Create records and add to batch. Records must have a string id. This can include the source offset
        // or other metadata to help uniquely identify the record itself.
        boolean isAutoCharacter = false;
        if(getLoadcellCharacterSet().toString().equals("AUTO")){ isAutoCharacter = true; }
        int startCharacter = getLoadcellStartCharacter();
        int endCharacter = getLoadcellEndCharacter();
        while (numRecords < maxBatchSize) {
            Record record = getContext().createRecord(String.valueOf(nextSourceOffset));
            Map<String, Field> map = new HashMap<>();
            //try (Socket socket = new Socket(getLoadcellIPAddress(), getLoadcellPort())) {
            try {
                LoadCell lc801 = new LoadCell(getLoadcellIPAddress(), getLoadcellPort(), getLoadcellCharacterSet().toString());
                if(hasXPosition()){
                    LaserDetector xPos801 = new LaserDetector(getLoadcellIPAddress(), getLoadcellPort(),getXPosCharacterSet().toString());
                }
                lc801.makeConnect();
                String resValue = lc801.getValue(getLoadcellStartCharacter(), getLoadcellEndCharacter());
                System.out.println(resValue);
                /*InputStream input = socket.getInputStream();
                InputStreamReader reader;
                if (isAutoCharacter) { reader = new InputStreamReader(input); }
                else{ reader = new InputStreamReader(input, getLoadcellCharacterSet().toString()); }
                int character;
                StringBuilder data = new StringBuilder();
                boolean readFlag = false;
                boolean readStart = false;
                while ((character = reader.read()) != -1) {
                    if (startCharacter == character && !readStart) {
                        readFlag = true;
                        readStart = true;
                        continue;
                    }
                    if (endCharacter == character && readStart) {
                        data.deleteCharAt(data.length()-1);
                        map.put("inputData", Field.create(String.valueOf(data)));
                        record.set(Field.create(map));
                        batchMaker.addRecord(record);
                        break;
                    }
                    if (readFlag) {
                        data.append(character);
                        data.append(",");
                    }
                }*/
                ++nextSourceOffset;
                ++numRecords;
            }
            catch (UnknownHostException ex) { System.out.println("Server not found: " + ex.getMessage()); }
            catch (IOException ex) { System.out.println("I/O error: " + ex.getMessage()); }
        }
        return String.valueOf(nextSourceOffset);
    }


}
