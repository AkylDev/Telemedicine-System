package kz.projects.telemedicine.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_patients")
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(unique = true)
  private String email;

  private String phone;
  private String medicalHistory;

  @OneToOne(cascade = CascadeType.ALL)
  private User user;
}
