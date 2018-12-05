package edu.neu.recipehub.objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
    public String mUserName;

    public Set<String> mRecipesFavorite;

    public User(String mUserName) {
        this.mUserName = mUserName;
        mRecipesFavorite = new HashSet<>();
    }

    /**
     * For tests ONLY.
     *
     * @return a dummy user.
     */
    public static User getDummyUser() {
        return new User("JundaYang");
    }
}
