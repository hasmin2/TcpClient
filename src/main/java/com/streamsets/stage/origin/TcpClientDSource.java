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
        label = "TcpClient Origin",
        description = "Adapter for TCP connection that listens data from the IP and Port",
        icon = "default.png",
        execution = ExecutionMode.STANDALONE,
        recordsByRef = true,
        onlineHelpRefUrl = ""
)
@ConfigGroups(value = Groups.class)
@GenerateResourceBundle
public class TcpClientDSource extends TcpClientSource {

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            defaultValue = "localhost",
            label = "IP",
            displayPosition = 10,
            group = "Ethernet",
            description = "IP Address for the TcpConnect"
    )
    public String ipAddress;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "4001",
            label = "Port",
            displayPosition = 10,
            group = "Ethernet",
            description = "Port number for the TcpConnect (1~65535 value only)",
            min = 1,
            max = 65535
    )
    public int port;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "1000",
            label = "Max Batch Number",
            displayPosition = 10,
            group = "Communications",
            description = "Max Batch size for records",
            min = 0,
            max = 65535
    )
    public int maxBatchSize;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.MODEL,
            defaultValue = "",
            label = "CharacterSet",
            displayPosition = 10,
            group = "Communications",
            description = "Define CharacterSets for the Tcp incoming data"
    )
    @ValueChooserModel(TcpCharacterSetChooserValues.class)
    public TcpCharacterSet tcpCharacterSet = TcpCharacterSet.AUTO;

    /** {@inheritDoc} */
    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    @Override
    public int getPort() {
        return port;
    }
    @Override
    public int getMaxBatchSize() {
        return maxBatchSize;
    }
    @Override
    public TcpCharacterSet gettcpCharacterSet(){
        return tcpCharacterSet;
    }

}
