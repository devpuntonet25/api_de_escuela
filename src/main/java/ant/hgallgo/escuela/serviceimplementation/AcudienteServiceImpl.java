package ant.hgallgo.escuela.serviceimplementation;

import ant.hgallgo.escuela.entity.Acudiente;
import ant.hgallgo.escuela.repository.AcudienteRepository;
import ant.hgallgo.escuela.service.AcudienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AcudienteServiceImpl implements AcudienteService {

    @Autowired
    private AcudienteRepository acudienteRepository;


    @Override
    public Acudiente save(Acudiente acudiente) {
        return acudienteRepository.save(acudiente);
    }

    @Override
    public List<Acudiente> acudientes() {
        return acudienteRepository.findAll();
    }

    @Override
    public List<Acudiente> paginacion(Integer pagina, Integer numeroRegistros, String campoOrdenamiento, String orden) {
        Pageable paginacion = PageRequest.of(pagina,  numeroRegistros);
        List<Acudiente> listaAcudientesOriginal = acudienteRepository.findAll(paginacion).getContent();
        List<Acudiente> listaAcudientesModificable = new ArrayList<>(listaAcudientesOriginal);

        switch(orden.toLowerCase()){
            case "ascendente":
                    switch(campoOrdenamiento.toLowerCase()){
                        case "id" -> listaAcudientesModificable.sort(Comparator.comparing(Acudiente::getId));
                        case "nombre" -> listaAcudientesModificable.sort(Comparator.comparing(Acudiente::getNombre));
                        case "apellido" -> listaAcudientesModificable.sort(Comparator.comparing(Acudiente::getApellido));
                    }
                    break;
            case "descendente":
                    switch (campoOrdenamiento.toLowerCase()){
                        case "id" -> listaAcudientesModificable.sort(Comparator.comparing(Acudiente::getId).reversed());
                        case "nombre" -> listaAcudientesModificable.sort(Comparator.comparing(Acudiente::getNombre).reversed());
                        case "apellido" -> listaAcudientesModificable.sort(Comparator.comparing(Acudiente::getApellido).reversed());
                    }
                break;
        }
        return listaAcudientesModificable;
    }

    @Override
    public List<Acudiente> obtenerAcudientesCuyoNumeroEmpiezaPorPrimerValorOrPorSegundoValor(Long primerValor, Long segundoValor) {
        return acudienteRepository.encontrarPorNumeroCelularStartingWithOrNumeroCelularStartingWith(primerValor, segundoValor);
    }

    @Override
    public List<Acudiente> findByNombreContaining(String valor) {
        return acudienteRepository.findByNombreContaining(valor);
    }

    @Override
    public String actualizarAcudiente(Acudiente acudiente) {
        boolean existeElAcudiente = acudienteRepository.existsById(acudiente.getId());
        if(existeElAcudiente){
            acudienteRepository.actualizarAcudiente(acudiente.getId(), acudiente.getNombre(),
                    acudiente.getApellido(), acudiente.getNumeroCelular());
            return "Se actualizó correctamente el acudiente con id " + acudiente.getId();
        }else{
            return "No se actualizó ningún acudiente porque no existe ninguno con el id " + acudiente.getId();
        }
    }

    @Override
    public Acudiente eliminarAcudiente(Long id) {
        try{
            Acudiente acudiente = acudienteRepository.findById(id).get();
            acudienteRepository.deleteById(id);
            return acudiente;
        }catch(NoSuchElementException error){
            return null;
        }

    }


}
