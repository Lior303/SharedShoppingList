package com.example.shoppinglist.ui.Favorites;

import androidx.annotation.Nullable;

public class FavoriteContact implements Comparable {

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof FavoriteContact) {
            FavoriteContact temp_obj = (FavoriteContact) obj;
            return this.nickname.equals(temp_obj.getNickname());
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof FavoriteContact) {
            FavoriteContact favoriteContact = (FavoriteContact) o;
            return nickname.compareTo(favoriteContact.getNickname());
        }
        return -1;
    }
}
