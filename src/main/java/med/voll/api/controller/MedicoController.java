package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    }

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemMedico> buscarPorId(@PathVariable Long id) {
        Optional<Medico> medicoOptional = repository.findByIdAndAtivoTrue(id);

        if (medicoOptional.isPresent()) {
            DadosListagemMedico dadosListagemMedico = new DadosListagemMedico(medicoOptional.get());
            return ResponseEntity.ok(dadosListagemMedico);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaooMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void desativar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.desativar();
    }

}
