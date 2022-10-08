package ant.hgallgo.escuela.service;

import ant.hgallgo.escuela.entity.Acudiente;

import java.util.List;

public interface AcudienteService {

    Acudiente save(Acudiente acudiente);

    List<Acudiente> acudientes();

    List<Acudiente> paginacion(Integer pagina, Integer numeroRegistros, String campoOrdenamiento, String orden);

    List<Acudiente> obtenerAcudientesCuyoNumeroEmpiezaPorPrimerValorOrPorSegundoValor(Long primerValor, Long segundoValor);

    List<Acudiente> findByNombreContaining(String valor);

    String actualizarAcudiente(Acudiente acudiente);

    Acudiente eliminarAcudiente(Long id);

}
