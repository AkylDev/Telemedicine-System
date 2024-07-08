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
  private String email;
  private String specialization;
  private String schedule;

  @OneToOne(cascade = CascadeType.ALL)
  private User user;
}
