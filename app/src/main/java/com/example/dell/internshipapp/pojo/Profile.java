package com.example.dell.internshipapp.pojo;

/**
 * Created by DELL on 26-07-2017.
 */

public class Profile {
    private String name;
    private String rank;
    private String userId;
    private String profilePic;
    public  Profile(){

    }
    public Profile(
            String userId,
            String name,
            String profilePic,
            String rank)
    {
        this.userId=userId;
        this.name=name;
        this.profilePic=profilePic;
        this.rank=rank;
    }
    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString(){
         return "USERId "+userId
                 +"NAME "+name+
                 "PRo "+profilePic+
                 "rank "+rank;

    }
}
