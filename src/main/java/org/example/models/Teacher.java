package org.example.models;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class Teacher implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String name;
    private String cpf;
    private String email;
}