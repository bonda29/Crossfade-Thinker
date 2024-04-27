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

    private Integer totalFollowers;

    @ElementCollection
    @Column(name = "image")
    @CollectionTable(name = "User_images", joinColumns = @JoinColumn(name = "owner_id"))
    private List<String> images = new ArrayList<>();

    private String accessToken;
    private String refreshToken;

}
