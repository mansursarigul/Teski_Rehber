package com.example.teski_rehber.teskirehber;

public class AdSoyadMobil {

        private String code = "";
        private String name = "";

        public AdSoyadMobil(String name, String code) {
            super();
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

}
