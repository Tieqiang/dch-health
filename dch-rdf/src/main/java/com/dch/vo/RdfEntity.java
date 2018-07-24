package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/10.
 */
public class RdfEntity {
    private String id;
    private String name;;
    private String image;
    private String kgType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKgType() {
        return kgType;
    }

    public void setKgType(String kgType) {
        this.kgType = kgType;
    }
}
