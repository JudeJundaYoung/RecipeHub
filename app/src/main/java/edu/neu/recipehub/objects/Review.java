package edu.neu.recipehub.objects;

public class Review {
    public User mUser;
    public String mContent;

    public Review(User mUser, String mContent) {
        this.mUser = mUser;
        this.mContent = mContent;
    }

    public static Review getDummyReview() {
        return new Review(User.getDummyUser(), "This is bullshit");
    }
}
