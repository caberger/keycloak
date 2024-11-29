package at.ac.htl.leonding.demo.features.post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TbPost {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    String title;
    String body;
    boolean published;
}
