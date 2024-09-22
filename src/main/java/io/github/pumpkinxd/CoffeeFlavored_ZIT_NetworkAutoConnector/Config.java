package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private String userID;
    private String encryptedPWD;
    private String PubKeyModulus;
    private String PubKeyExponent;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEncryptedPWD() {
        return encryptedPWD;
    }

    public void setEncryptedPWD(String encryptedPWD) {
        this.encryptedPWD = encryptedPWD;
    }

    public String getPubKeyModulus() {
        return PubKeyModulus;
    }

    public void setPubKeyModulus(String pubKeyModulus) {
        PubKeyModulus = pubKeyModulus;
    }

    public String getPubKeyExponent() {
        return PubKeyExponent;
    }

    public void setPubKeyExponent(String pubKeyExponent) {
        PubKeyExponent = pubKeyExponent;
    }


    public void saveToFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filePath), this);
    }
    public static Config loadFromFile(String filePath)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), Config.class);
        }catch (IOException e){
            e.printStackTrace();
            Config newConfig = new Config();
            newConfig.setUserID("");
            newConfig.setEncryptedPWD("");
            newConfig.setPubKeyModulus("");
            newConfig.setPubKeyExponent("");
            return newConfig;
        }

    }

}
