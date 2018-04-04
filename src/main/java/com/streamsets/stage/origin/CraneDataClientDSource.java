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

import com.streamsets.pipeline.api.*;

@StageDef(
        version = 1,
        label = "CraneData Origin",
        description = "CraneData Getter for TCP connection that listens data from the IP and Port",
        icon = "default.png",
        execution = ExecutionMode.STANDALONE,
        recordsByRef = true,
        onlineHelpRefUrl = ""
)
@ConfigGroups(value = Groups.class)
@GenerateResourceBundle
public class CraneDataClientDSource extends CraneDataClientSource {
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.BOOLEAN,
            defaultValue = "false",
            label = "has X Axis Laser Position Detector?",
            displayPosition = 10,
            group = "Connectivity",
            description = "Check if the Crane has X Laser Position Detector"
    )
    public boolean hasXPosition;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.BOOLEAN,
            defaultValue = "false",
            label = "has Y Axis Laser Position Detector?",
            displayPosition = 10,
            group = "Connectivity",
            description = "Check if the Crane has Y Axis Laser Position Detector"
    )
    public boolean hasYPosition;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "1",
            label = "Max Batch Number",
            displayPosition = 10,
            group = "Connectivity",
            description = "Max Batch size for records",
            min = 0,
            max = 65535
    )
    public int maxBatchSize;


    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            defaultValue = "localhost",
            label = "IP",
            displayPosition = 10,
            group = "Loadcell",
            description = "IP Address for the HostConnect"
    )
    public String LoadcellIpAddress;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "4001",
            label = "Port",
            displayPosition = 10,
            group = "Loadcell",
            description = "Port number for the TcpConnect (1~65535 value only)",
            min = 1,
            max = 65535
    )
    public int LoadcellPort;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.MODEL,
            defaultValue = "",
            label = "CharacterSet",
            displayPosition = 10,
            group = "LoadCell",
            description = "Define CharacterSets for incoming data"
    )
    @ValueChooserModel(CraneDataCharacterSetChooserValues.class)
    public CraneDataCharacterSet loadcellCharacterSet = CraneDataCharacterSet.AUTO;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "",
            label = "Start Character",
            displayPosition = 10,
            group = "Loadcell",
            description = "Start Character(in Integer) for each Packet",
            min = 0,
            max = 65535
    )
    public int LoadcellStartCharacter;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "",
            label = "End Character",
            displayPosition = 10,
            group = "Loadcell",
            description = "End Character(in Integer) for each Packet",
            min = 0,
            max = 65535
    )
    public int LoadcellEndCharacter;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.MODEL,
            defaultValue = "",
            label = "Laser meter CharacterSet",
            displayPosition = 10,
            group = "XPosition",
            description = "Define CharacterSets for XPostion incoming data"
    )

    @ValueChooserModel(CraneDataCharacterSetChooserValues.class)
    public CraneDataCharacterSet xPosCharacterSet = CraneDataCharacterSet.AUTO;

    /** {@inheritDoc} */
    @Override
    public boolean hasXPosition() { return hasXPosition; }
    @Override
    public boolean hasYPosition() { return hasYPosition; }
    @Override
    public String getLoadcellIPAddress() { return LoadcellIpAddress; }
    @Override
    public int getLoadcellPort() { return LoadcellPort; }
    @Override
    public int getMaxBatchSize() { return maxBatchSize; }
    @Override
    public CraneDataCharacterSet getLoadcellCharacterSet(){ return loadcellCharacterSet; }
    @Override
    public int getLoadcellStartCharacter() { return LoadcellStartCharacter; }
    @Override
    public int getLoadcellEndCharacter() { return LoadcellEndCharacter; }
    @Override
    public CraneDataCharacterSet getXPosCharacterSet() { return xPosCharacterSet; }
}