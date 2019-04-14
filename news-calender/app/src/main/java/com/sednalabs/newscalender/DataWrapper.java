package com.sednalabs.newscalender;


import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

        private ArrayList<Articles> myarticles;

        public DataWrapper(ArrayList<Articles> data) {
            this.myarticles = data;
        }

        public ArrayList<Articles> getParliaments() {
            return this.myarticles;
        }

    }

