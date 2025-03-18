package waa.miu.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String email;
    String password;
    String phone;
    String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Role> roles;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Property> ownedProperties;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Property> favouriteProperties;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Property> viewedProperties;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn
    private List<Offer> offers;
}
