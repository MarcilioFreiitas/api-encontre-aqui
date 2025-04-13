package com.encontreaqui.service;

import com.encontreaqui.model.Foto;
import com.encontreaqui.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    // Cria uma nova foto
    public Foto criarFoto(Foto foto) {
        // Inserir regras específicas de negócio, como verificação do formato da URL, se necessário.
        return fotoRepository.save(foto);
    }

    // Lista todas as fotos
    public List<Foto> listarFotos() {
        return fotoRepository.findAll();
    }

    // Busca uma foto pelo ID
    public Foto buscarPorId(Long id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada para o id: " + id));
    }

    // Remove uma foto
    public void deletarFoto(Long id) {
        Foto fotoExistente = buscarPorId(id);
        fotoRepository.delete(fotoExistente);
    }
}
