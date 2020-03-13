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

package org.apache.guacamole.rest.activeconnection;

import java.util.Date;
import org.apache.guacamole.net.auth.ActiveConnection;

/**
 * Information related to active connections which may be exposed through the
 * REST endpoints.
 */
public class APIActiveConnection {

    /**
     * The identifier of the active connection itself.
     */
    private final String identifier;

    /**
     * The identifier of the connection associated with this
     * active connection.
     */
    private final String connectionIdentifier;
    
    /**
     * The date and time the connection began.
     */
    private final Date startDate;

    /**
     * The host from which the connection originated, if known.
     */
    private final String remoteHost;
    
    /**
     * The name of the user who used or is using the connection.
     */
    private final String username;

    /**
     * Whether this active connection may be connected to.
     */
    private final boolean connectable;

    private String cmpUserId;
    private String guacadCloudEntryId;
    private String tenantId;
    private String protocolName;
    private String ipAddress;
    private String vmName;

    /**
     * Creates a new APIActiveConnection, copying the information from the given
     * active connection.
     *
     * @param connection
     *     The active connection to copy data from.
     */
    public APIActiveConnection(ActiveConnection connection) {
        this.identifier           = connection.getIdentifier();
        this.connectionIdentifier = connection.getConnectionIdentifier();
        this.startDate            = connection.getStartDate();
        this.remoteHost           = connection.getRemoteHost();
        this.username             = connection.getUsername();
        this.connectable          = connection.isConnectable();
        this.cmpUserId            = connection.getCmpUserId();
        this.guacadCloudEntryId = connection.getGuacadCloudEntryId();
        this.tenantId             = connection.getTenantId();
        this.protocolName         = connection.getProtocolName();
        this.ipAddress            = connection.getIpAddress();
        this.vmName               = connection.getVmName();
    }

    /**
     * Returns the identifier of the connection associated with this tunnel.
     *
     * @return
     *     The identifier of the connection associated with this tunnel.
     */
    public String getConnectionIdentifier() {
        return connectionIdentifier;
    }
    
    /**
     * Returns the date and time the connection began.
     *
     * @return
     *     The date and time the connection began.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Returns the remote host from which this connection originated.
     *
     * @return
     *     The remote host from which this connection originated.
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Returns the name of the user who used or is using the connection at the
     * times given by this tunnel.
     *
     * @return
     *     The name of the user who used or is using the associated connection.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the identifier of the active connection itself. This is
     * distinct from the connection identifier, and uniquely identifies a
     * specific use of a connection.
     *
     * @return
     *     The identifier of the active connection.
     */
    public String getIdentifier() {
        return identifier;
    }

    /***
     * Returns whether this active connection may be connected to, just as a
     * normal connection.
     *
     * @return
     *     true if this active connection may be connected to, false otherwise.
     */
    public boolean isConnectable() {
        return connectable;
    }

    public String getCmpUserId() {
        return cmpUserId;
    }

    public void setCmpUserId(String cmpUserId) {
        this.cmpUserId = cmpUserId;
    }

    public String getGuacadCloudEntryId() {
        return guacadCloudEntryId;
    }

    public void setGuacadCloudEntryId(String guacadCloudEntryId) {
        this.guacadCloudEntryId = guacadCloudEntryId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }
}
