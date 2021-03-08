package br.com.devplus.forum.controller.form;

import br.com.devplus.forum.modelo.Curso;
import br.com.devplus.forum.modelo.Topico;
import br.com.devplus.forum.repository.CursoRepository;
import br.com.devplus.forum.repository.TopicoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class TopicoForm {
    @NotBlank @Length(min = 5)
    private String titulo;
    @NotBlank @Length(min = 10)
    private String mensagem;
    @NotBlank
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Topico converter(CursoRepository repository) {
        Curso curso = repository.findByNome(this.nomeCurso);

        Topico topico = new Topico();
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);
        topico.setCurso(curso);

        return topico;
    }
}
