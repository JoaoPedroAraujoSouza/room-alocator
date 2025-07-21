package org.example.repository;

import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GenericRepository <T extends Serializable> {
    private final String fileName;

    public void saveAll(List<T> objects) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(objects);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> loadAll() throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        }
    }
}
