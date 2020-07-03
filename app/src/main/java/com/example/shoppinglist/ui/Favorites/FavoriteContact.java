package com.example.shoppinglist.ui.Favorites;

public class FavoriteContact {

    private String mail;
    private String nickname;

    public FavoriteContact(String mail, String nickname) {
        this.mail = mail;
        this.nickname = nickname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
