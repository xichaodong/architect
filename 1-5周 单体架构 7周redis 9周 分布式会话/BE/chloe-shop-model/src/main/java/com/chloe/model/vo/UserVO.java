package com.chloe.model.vo;

public class UserVO {
    private String id;

    private String username;

    private String nickname;

    private String face;

    private Integer sex;

    private String userUniqueToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getUserUniqueToken() {
        return userUniqueToken;
    }

    public void setUserUniqueToken(String userUniqueToken) {
        this.userUniqueToken = userUniqueToken;
    }
}
