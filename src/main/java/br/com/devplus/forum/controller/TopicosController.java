package br.com.devplus.forum.controller;

import br.com.devplus.forum.controller.dto.DetalhesTopicoDto;
import br.com.devplus.forum.controller.dto.TopicoDto;
import br.com.devplus.forum.controller.form.AtualizaTopicoForm;
import br.com.devplus.forum.controller.form.TopicoForm;
import br.com.devplus.forum.modelo.Curso;
import br.com.devplus.forum.modelo.Topico;
import br.com.devplus.forum.repository.CursoRepository;
import br.com.devplus.forum.repository.TopicoRepository;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDto.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesTopicoDto> detalhe(@PathVariable("id") Long id) { //@PathVariable("id")
        Optional<Topico> topico = topicoRepository.findById(id);

//        if (topico.isPresent()) {
//            return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()))
//        } else {
//            return ResponseEntity.notFound().build();
//        }

        return topico.map(value ->
                ResponseEntity.ok(new DetalhesTopicoDto(value))).
                orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable("id") Long id, @RequestBody @Valid AtualizaTopicoForm form) {
        Optional<Topico> optional = topicoRepository.findById(id);

        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable("id") Long id, @RequestBody @Valid AtualizaTopicoForm form) {
        Optional<Topico> optional = topicoRepository.findById(id);

        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
