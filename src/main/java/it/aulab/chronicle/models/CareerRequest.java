package it.aulab.chronicle.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "career_request")
public class CareerRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable=false, length=1000)
    private String body;

    @Column
    private boolean isChecked;

    @OneToOne
    @JoinColumn(name = "user_id", unique=true)
    private User user;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
