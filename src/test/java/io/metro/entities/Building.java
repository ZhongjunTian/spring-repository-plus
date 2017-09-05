/*
 * ________________________________________________________________________
 * METRO.IO CONFIDENTIAL
 * ________________________________________________________________________
 *
 * Copyright (c) 2017.
 * Metro Labs Incorporated
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Metro Labs Incorporated and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Metro Labs Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Metro Labs Incorporated.
 */

package io.metro.entities;

public class Building {
    private String id;
    private String accountId;
    private String name;
    private String address;
    private String address2;
    private String city;
    private String country;
    private String stateProvence;
    private String postal;
    private Double latitude;
    private Double longitude;
    private String noaaStationId;
    private Long epwDataId;
    private Integer yearBuilt;
    private PrimaryBuildingType buildingType;
    private Integer grossFloorAreaM2;
    private Integer grossFloorAreaFt2;
    private Integer grossFloorArea;
    private GrossFloorAreaUnitsType grossFloorAreaUnit;
    private String notes;

    public String getId() {
        return id;
    }

    public Building setId(String id) {
        this.id = id;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public Building setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Building setName(String primaryName) {
        this.name = primaryName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Building setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public Building setAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Building setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Building setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getStateProvence() {
        return stateProvence;
    }

    public Building setStateProvence(String stateProvence) {
        this.stateProvence = stateProvence;
        return this;
    }

    public String getPostal() {
        return postal;
    }

    public Building setPostal(String postal) {
        this.postal = postal;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Building setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Building setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getNoaaStationId() {
        return noaaStationId;
    }

    public Building setNoaaStationId(String noaaStationId) {
        this.noaaStationId = noaaStationId;
        return this;
    }

    public Long getEpwDataId() {
        return epwDataId;
    }

    public Building setEpwDataId(Long epwDataId) {
        this.epwDataId = epwDataId;
        return this;
    }

    public Integer getYearBuilt() {
        return yearBuilt;
    }

    public Building setYearBuilt(Integer yearBuilt) {
        this.yearBuilt = yearBuilt;
        return this;
    }

    public PrimaryBuildingType getBuildingType() {
        return buildingType;
    }

    public Building setBuildingType(PrimaryBuildingType buildingType) {
        this.buildingType = buildingType;
        return this;
    }

    public Integer getGrossFloorAreaM2() {
        return grossFloorAreaM2;
    }

    public Building setGrossFloorAreaM2(Integer grossFloorAreaM2) {
        this.grossFloorAreaM2 = grossFloorAreaM2;
        return this;
    }

    public Integer getGrossFloorAreaFt2() {
        return grossFloorAreaFt2;
    }

    public Building setGrossFloorAreaFt2(Integer grossFloorAreaFt2) {
        this.grossFloorAreaFt2 = grossFloorAreaFt2;
        return this;
    }

    public Integer getGrossFloorArea() {
        return grossFloorArea;
    }

    public Building setGrossFloorArea(Integer grossFloorArea) {
        this.grossFloorArea = grossFloorArea;
        return this;
    }

    public GrossFloorAreaUnitsType getGrossFloorAreaUnit() {
        return grossFloorAreaUnit;
    }

    public Building setGrossFloorAreaUnit(GrossFloorAreaUnitsType grossFloorAreaUnit) {
        this.grossFloorAreaUnit = grossFloorAreaUnit;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public Building setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public enum GrossFloorAreaUnitsType {
        SQUARE_FEET("Square Feet"),
        SQUARE_METERS("Square Meters");

        private final String value;

        private GrossFloorAreaUnitsType(String v) {
            this.value = v;
        }

        public String value() {
            return this.value;
        }

        public static GrossFloorAreaUnitsType fromValue(String v) {
            GrossFloorAreaUnitsType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                GrossFloorAreaUnitsType c = var1[var3];
                if(c.value.equals(v)) {
                    return c;
                }
            }

            throw new IllegalArgumentException(v);
        }
    }

    public enum PrimaryBuildingType {
        HOTEL("Hotel"),
        OFFICE("Office"),
        HOSPITAL("Hospital (General Medical & Surgical)"),
        APARTMENT("Multifamily Housing"),
        DATA_CENTER("Data Center"),
        PRIMARY_SCHOOL("Primary School"),
        SECONDARY_SCHOOL("Secondary School (College/University)"),
        FULL_SERVICE_RESTAURANT("Full Service Restaurant"),
        OUTPATIENT_HEALTH_CARE("Outpatient Healthcare"),
        QUICK_SERVICE_RESTAURANT("Quick Service/Fastfood Restaurant"),
        STAND_ALONE_RETAIL("Stand Alone Retail"),
        SHOPPING_MALL("Strip/Shopping Mall"),
        SUPERMARKET_GROCERY_STORE("Supermarket/Grocery Store"),
        REFRIGERATED_WAREHOUSE("Refrigerated Warehouse"),
        NON_REFRIGERATED_WAREHOUSE("Non-Refrigerated Warehouse");

        private final String value;

        private PrimaryBuildingType(String v) {
            this.value = v;
        }

        public String value() {
            return this.value;
        }

        public static PrimaryBuildingType fromValue(String v) {
            PrimaryBuildingType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                PrimaryBuildingType c = var1[var3];
                if(c.value.equals(v)) {
                    return c;
                }
            }

            throw new IllegalArgumentException(v);
        }
    }
}
