/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.rest.history;

import org.apache.guacamole.net.auth.ConnectionRecord;

/**
 * A connection record which may be exposed through the REST endpoints.
 */
public class APIConnectionRecord extends APIActivityRecord {

    /**
     * The identifier of the connection associated with this record.
     */
    private final String connectionIdentifier;

    /**
     * The identifier of the connection associated with this record.
     */
    private final String connectionName;

    /**
     * The identifier of the sharing profile associated with this record.
     */
    private final String sharingProfileIdentifier;

    /**
     * The identifier of the sharing profile associated with this record.
     */
    private final String sharingProfileName;


    private String recordingPath;
    private String recordingName;
    private String typescriptPath;
    private String typescriptName;
    private String exts;
    private String cmpUserId;
    private String guacadAddress;
    private String tenantId;

    /**
     * Creates a new APIConnectionRecord, copying the data from the given
     * record.
     *
     * @param record
     *     The record to copy data from.
     */
    public APIConnectionRecord(ConnectionRecord record) {
        super(record);
        this.connectionIdentifier = record.getConnectionIdentifier();
        this.connectionName = record.getConnectionName();
        this.sharingProfileIdentifier = record.getSharingProfileIdentifier();
        this.sharingProfileName = record.getSharingProfileName();
        this.cmpUserId = record.getCmpUserId();
        this.recordingPath = record.getRecordingPath();
        this.recordingName = record.getRecordingName();
        this.typescriptName = record.getTypescriptName();
        this.typescriptPath = record.getTypescriptPath();
        this.exts = record.getExts();
        this.guacadAddress = record.getGuacadAddress();
        this.tenantId = record.getTenantId();
    }

    /**
     * Returns the identifier of the connection associated with this
     * record.
     *
     * @return
     *     The identifier of the connection associated with this record.
     */
    public String getConnectionIdentifier() {
        return connectionIdentifier;
    }

    /**
     * Returns the name of the connection associated with this record.
     *
     * @return
     *     The name of the connection associated with this record.
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * Returns the identifier of the sharing profile associated with this
     * record. If the connection was not being used via a sharing profile, this
     * will be null.
     *
     * @return
     *     The identifier of the sharing profile associated with this record,
     *     or null if no sharing profile was used.
     */
    public String getSharingProfileIdentifier() {
        return sharingProfileIdentifier;
    }

    /**
     * Returns the name of the sharing profile associated with this record. If
     * the connection was not being used via a sharing profile, this will be
     * null.
     *
     * @return
     *     The name of the sharing profile associated with this record, or null
     *     if no sharing profile was used.
     */
    public String getSharingProfileName() {
        return sharingProfileName;
    }

    public String getRecordingPath() {
        return recordingPath;
    }

    public void setRecordingPath(String recordingPath) {
        this.recordingPath = recordingPath;
    }

    public String getRecordingName() {
        return recordingName;
    }

    public void setRecordingName(String recordingName) {
        this.recordingName = recordingName;
    }

    public String getTypescriptPath() {
        return typescriptPath;
    }

    public void setTypescriptPath(String typescriptPath) {
        this.typescriptPath = typescriptPath;
    }

    public String getTypescriptName() {
        return typescriptName;
    }

    public void setTypescriptName(String typescriptName) {
        this.typescriptName = typescriptName;
    }

    public String getExts() {
        return exts;
    }

    public void setExts(String exts) {
        this.exts = exts;
    }

    public String getCmpUserId() {
        return cmpUserId;
    }

    public void setCmpUserId(String cmpUserId) {
        this.cmpUserId = cmpUserId;
    }

    public String getGuacadAddress() {
        return guacadAddress;
    }

    public void setGuacadAddress(String guacadAddress) {
        this.guacadAddress = guacadAddress;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
