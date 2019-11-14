package com.example.teski_rehber.teskirehber;

public class ListAdapterUser {
    private String FieldAdSoyad;
    private String FieldUnvan;
    private String FieldMobil;
    private String FieldDaire;
    private String FieldDIDNo;


    public ListAdapterUser(String fAdSoyad, String fUnvan, String fMobil, String fDaire, String fDIDNo){
        FieldAdSoyad = fAdSoyad;
        FieldUnvan = fUnvan;
        FieldMobil = fMobil;
        FieldDaire = fDaire;
        FieldDIDNo = fDIDNo;
    }

    public String getAdSoyad() { return FieldAdSoyad; }

    public void setAdSoyad(String sAdSoyad) { FieldAdSoyad = sAdSoyad; }

    public String getUnvan() { return FieldUnvan; }

    public void setUnvan(String sUnvan) { FieldUnvan = sUnvan; }

    public String getMobil() { return FieldMobil; }

    public void setMobil(String sMobil) { FieldMobil = sMobil; }

    public String getDaire() { return FieldDaire; }

    public void setDaire(String sDaire) { FieldDaire = sDaire; }

    public String getDID() { return FieldDIDNo; }

    public void setDID(String sDID) { FieldDIDNo = sDID; }

}
