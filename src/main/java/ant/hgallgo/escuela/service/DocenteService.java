package ant.hgallgo.escuela.service;

import ant.hgallgo.escuela.entity.Docente;

import java.util.List;

public interface DocenteService {
    List<Docente> getTodosLosDocentes();
    Docente getDocentePorId(Long id);
    List<Docente> getDocentesCuyoSalarioEsMayorOrIgualQue(Long salario);

    Docente guardarDocente(Docente docente);

    List<Docente> docentesOrdenados(String campoOrdenamiento, String orden);

    List<Docente> paginacionAndOrdenamiento(Integer pagina, Integer numeroRegistros, String campoOrdenamiento, String orden);

    Docente eliminar(Long id);

    String actualizar(Docente docente);
}
