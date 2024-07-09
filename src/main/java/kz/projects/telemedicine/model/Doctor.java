package kz.projects.telemedicine.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_doctors")
public class Doctor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  private Specialization specialization;

  private String schedule;

  @OneToOne(cascade = CascadeType.ALL)
  private User user;
}
