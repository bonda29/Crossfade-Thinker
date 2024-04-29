package tech.bonda.cft.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    private String id;

    private String username;
    private String email;

    @Column(name = "total_followers")
    private Integer totalFollowers;

    @ElementCollection
    @Column(name = "image")
    @CollectionTable(name = "user_images", joinColumns = @JoinColumn(name = "owner_id"))
    private List<String> images = new ArrayList<>();

    @Column(name = "access_token", length = 294)
    private String accessToken;

    @Column(name = "refresh_token", length = 131)
    private String refreshToken;
}
